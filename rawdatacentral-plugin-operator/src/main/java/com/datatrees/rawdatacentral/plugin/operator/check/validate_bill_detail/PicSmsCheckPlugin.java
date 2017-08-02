package com.datatrees.rawdatacentral.plugin.operator.check.validate_bill_detail;

import com.datatrees.common.util.GsonUtils;
import com.datatrees.common.util.ThreadInterruptedUtil;
import com.datatrees.crawler.core.processor.AbstractProcessorContext;
import com.datatrees.crawler.core.processor.plugin.AbstractClientPlugin;
import com.datatrees.crawler.core.processor.plugin.PluginFactory;
import com.datatrees.rawdatacentral.api.CrawlerOperatorService;
import com.datatrees.rawdatacentral.common.utils.BeanFactoryUtils;
import com.datatrees.rawdatacentral.domain.constant.AttributeKey;
import com.datatrees.rawdatacentral.domain.constant.FormType;
import com.datatrees.rawdatacentral.domain.enums.DirectiveEnum;
import com.datatrees.rawdatacentral.domain.enums.ErrorCode;
import com.datatrees.rawdatacentral.domain.operator.OperatorParam;
import com.datatrees.rawdatacentral.domain.result.DirectiveResult;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import com.datatrees.rawdatacentral.share.MessageService;
import com.datatrees.rawdatacentral.share.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 详单校验-->图片和短信表单
 * 步骤:图片验证码-->短信验证码-->提交校验
 * Created by zhouxinghai on 2017/7/31
 */
public class PicSmsCheckPlugin extends AbstractClientPlugin {

    private static final Logger    logger   = LoggerFactory.getLogger(PicSmsCheckPlugin.class);

    private CrawlerOperatorService pluginService;

    private MessageService         messageService;

    private RedisService           redisService;

    private static final String    formType = FormType.VALIDATE_BILL_DETAIL;

    //超时时间120秒
    private long                   timeOut  = 120;

    {
        pluginService = BeanFactoryUtils.getBean(CrawlerOperatorService.class);
        messageService = BeanFactoryUtils.getBean(MessageService.class);
        redisService = BeanFactoryUtils.getBean(RedisService.class);
    }

    @Override
    public String process(String... args) throws Exception {
        AbstractProcessorContext context = PluginFactory.getProcessorContext();
        String websiteName = context.getWebsiteName();
        Long taskId = context.getLong(AttributeKey.TASK_ID);
        logger.info("详单-->插件启动,taskId={},websiteName={}", taskId, websiteName);
        validatePicCode(taskId, websiteName);
        return "";
    }

    /**
     * 图片验证码最大次数5次,
     * 用户输入图片验证码超时时间120秒
     * @param taskId
     * @param websiteName
     * @throws InterruptedException
     */
    public void validatePicCode(Long taskId, String websiteName) throws InterruptedException {
        int retry = 0, maxRetry = 5, errorCount = 0;
        do {
            OperatorParam param = new OperatorParam(formType, taskId, websiteName);

            HttpResult<Map<String, Object>> result = pluginService.refeshPicCode(param);
            if (!result.getStatus()) {
                //获取图片验证码失败超过3次,任务失败
                if(errorCount >= 3){
                    throw new RuntimeException(ErrorCode.REFESH_PIC_CODE_ERROR.getErrorMsg());
                }
                errorCount++;
                TimeUnit.SECONDS.sleep(10);
                continue;
            }
            String picCode = result.getData().get(AttributeKey.PIC_CODE).toString();
            //发送MQ指令(要求输入图片验证码)
            Map<String, String> data = new HashMap<>();
            data.put(AttributeKey.REMARK, picCode);
            String directiveId = messageService.sendDirective(taskId, DirectiveEnum.REQUIRE_PICTURE.getCode(),
                GsonUtils.toJson(data));
            //等待用户输入图片验证码,等待120秒

            DirectiveResult<Map<String, Object>> receiveDirective = redisService.getDirectiveResult(directiveId,
                timeOut, TimeUnit.SECONDS);
            if (null == receiveDirective) {
                logger.error("详单-->等待用户输入图片验证码超时({}秒),taskId={},websiteName={},directiveId={}", timeOut, taskId,
                    websiteName, directiveId);
                throw new RuntimeException(ErrorCode.VALIDATE_PIC_CODE_FAIL_TIMEOUT.getErrorMsg());
            }

            picCode = receiveDirective.getData().get(AttributeKey.CODE).toString();
            param.setPicCode(picCode);
            result = pluginService.validatePicCode(param);
            if (result.getStatus() || result.getResponseCode() == ErrorCode.NOT_SUPORT_METHOD.getErrorCode()) {
                redisService.addTaskShare(taskId, AttributeKey.PIC_CODE, picCode);
                //图片验证码结束,进入短信验证
                submit(taskId, websiteName);
                return;
            }
            if (ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
                logger.error("详单-->验证图片验证码-->用户刷新/取消任务. threadId={},taskId={},websiteName={}",
                    Thread.currentThread().getId(), taskId, websiteName);
                throw new InterruptedException("详单-->验证图片验证码-->用户刷新/取消任务");
            }
        } while (retry++ < 5);
        throw new RuntimeException(ErrorCode.VALIDATE_PIC_CODE_FAIL.getErrorMsg());
    }

    /**
     * 用户输入图片验证码后提交
     * 用户输入图片验证码超时时间120秒
     * @param taskId
     * @param websiteName
     * @throws InterruptedException
     */
    public void submit(Long taskId, String websiteName) throws InterruptedException {
        if (ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
            logger.error("详单-->验证短信验证码-->用户刷新/取消任务. threadId={},taskId={},websiteName={}",
                Thread.currentThread().getId(), taskId, websiteName);
            throw new InterruptedException("详单-->验证短信验证码-->用户刷新/取消任务");
        }
        OperatorParam param = new OperatorParam(formType, taskId, websiteName);
        HttpResult<Map<String, Object>> result = pluginService.refeshSmsCode(param);

        //发送MQ指令(要求输入短信验证码)
        Map<String, String> data = new HashMap<>();
        data.put(AttributeKey.REMARK, "");
        String directiveId = messageService.sendDirective(taskId, DirectiveEnum.REQUIRE_SMS.getCode(),
            GsonUtils.toJson(data));

        //等待用户输入图片验证码,等待120秒
        DirectiveResult<Map<String, Object>> receiveDirective = redisService.getDirectiveResult(directiveId, timeOut,
            TimeUnit.SECONDS);
        if (null == receiveDirective) {
            logger.error("详单-->等待用户输入图片验证码超时({}秒),taskId={},websiteName={},directiveId={}", timeOut, taskId,
                websiteName, directiveId);
            throw new RuntimeException(ErrorCode.VALIDATE_PIC_CODE_FAIL_TIMEOUT.getErrorMsg());
        }
        String smsCode = receiveDirective.getData().get(AttributeKey.CODE).toString();
        String picCode = redisService.getTaskShare(taskId, AttributeKey.PIC_CODE);
        param.setSmsCode(smsCode);
        param.setPicCode(picCode);
        result = pluginService.submit(param);
        if (!result.getStatus()) {
            //图片验证码验证失败重新验证图片验证码
            validatePicCode(taskId, websiteName);
        }
    }

}
