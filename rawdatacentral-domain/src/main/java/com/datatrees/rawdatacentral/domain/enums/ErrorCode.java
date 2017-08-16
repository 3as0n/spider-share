package com.datatrees.rawdatacentral.domain.enums;

import java.util.HashSet;
import java.util.Set;

public enum ErrorCode {

    NOT_SUPORT_METHOD(-10,"方法不支持"),

    LOGIN_FAIL(-100,"登陆失败,请重试"),
    LOGIN_UNEXPECTED_RESULT(-101,"登陆失败,请重试"),
    LOGIN_ERROR(-102,"登陆失败,请重试"),

    EMPTY_TASK_ID(-110,"taskId不能为空"),
    EMPTY_FORM_TYPE(-111,"formType不能为空"),
    EMPTY_WEBSITE_NAME(-112,"websiteName不能为空"),

    EMPTY_MOBILE(-200,"手机号不能为空"),
    EMPTY_PASSWORD(-210,"密码不能为空"),
    EMPTY_PIC_CODE(-220,"图片验证码不能为空"),
    EMPTY_SMS_CODE(-230,"短信验证码不能为空"),

    VALIDATE_PHONE_FAIL(-241,"手机号码与运营商归属地不符，请重新输入"),
    VALIDATE_PASSWORD_FAIL(-240, "您的账户名与密码不匹配，请重新输入"),

    REFESH_PIC_CODE_ERROR(-250,"图片验证码刷新失败,请重试"),

    VALIDATE_PIC_CODE_FAIL(-260,"请输入正确的图片验证码"),
    VALIDATE_PIC_CODE_UNEXPECTED_RESULT(-261,"请输入正确的图片验证码"),
    VALIDATE_PIC_CODE_ERROR(-262,"请输入正确的图片验证码"),
    VALIDATE_PIC_CODE_TIMEOUT(-263,"图片验证码,验证超时"),

    REFESH_SMS_FAIL(-270,"短信验证码发送失败,请重试"),
    REFESH_SMS_UNEXPECTED_RESULT(-271,"短信验证码发送失败,请重试"),
    REFESH_SMS_ERROR(-272,"短信验证码发送失败,请重试"),

    VALIDATE_SMS_FAIL(-280,"短信验证码不正确或已过期,请重新获取"),
    VALIDATE_SMS_UNEXPECTED_RESULT(-281,"短信验证码不正确或已过期,请重新获取"),
    VALIDATE_SMS_ERROR(-282,"短信验证码不正确或已过期,请重新获取"),
    VALIDATE_SMS_TIMEOUT(-283,"图片验证码,验证超时"),


    VALIDATE_FAIL(-290,"校验失败,请重试"),
    VALIDATE_UNEXPECTED_RESULT(-291,"校验失败,请重试"),
    VALIDATE_ERROR(-292,"校验失败,请重试"),

    TASK_INIT_ERROR(-300, "初始化失败"),


    RESULT_SEND_ERROR(202, "Result send error!"),


    CONFIG_ERROR(304, "Config error!"),

    TASK_INTERRUPTED_ERROR(306, "Thread Interrupted!"),

    LOGIN_TIMEOUT_ERROR(308, "Login Time Out!"),


    COOKIE_INVALID(404, "Cookie invalid!"),

    GIVE_UP_RETRY(406, "Give up retry!"),

    MESSAGE_DROP(408, "Message drop!"),

    INIT_QUEUE_FAILED_ERROR_CODE(502, "The LinkQueue initialization failed!"),

    NO_ACTIVE_PROXY(503, "can't get vaild proxy"),

    BLOCKED_ERROR_CODE(504, "Access block, the system will early quit"), // Access block

    TASK_TIMEOUT_ERROR_CODE(506, "The task is timeout"), // Task time out

    QUEUE_FULL_ERROR_CODE(508, "The link queue is full"),

    NO_RESULT_ERROR_CODE(510, "There is no result"), // NO_RESULT_ERROR_CODE

    NOT_EMPTY_ERROR_CODE(512, "Field not empty"),

    RESPONSE_EMPTY_ERROR_CODE(514, "Response not empty"),

    UNKNOWN_REASON(520, "Unknown Reason.");



    private int errorCode;
    private String errorMessage;

    private ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMsg() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.valueOf(this.errorCode);
    }

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        for(ErrorCode e : ErrorCode.values()){
            if(set.contains(e.getErrorCode())){
                throw new RuntimeException("repeat errorCode "+e.getErrorCode());
            }
            set.add(e.getErrorCode());
        }
    }
}
