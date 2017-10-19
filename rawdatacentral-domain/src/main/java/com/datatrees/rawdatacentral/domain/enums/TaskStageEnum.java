package com.datatrees.rawdatacentral.domain.enums;

public enum TaskStageEnum {

    INIT("INIT", "初始化"),
    LOGIN_SUCCESS("LOGIN_SUCCESS", "登录成功"),
    CRAWLER_START("CRAWLER_START", "爬虫启动"),;
    private String status;
    private String remark;

    TaskStageEnum(String status, String remark) {
        this.status = status;
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }
}