package net.lab1024.sa.admin.module.freeserver.renew.constant;

/**
 * @author Demo-Liu
 * @create 2020-08-05 16:30
 * @description 云信息
 */
public enum CloudInfo {


    ABEI("0",
            "阿贝云",
            "https://api.abeiyun.com",
            "https://api.abeiyun.com/www/login.php",
            "https://api.abeiyun.com/www/renew.php",
            "https://api.sanfengyun.com/www/user.php"),
    SANFENG("1",
                 "三丰云",
                 "https://api.sanfengyun.com",
                 "https://api.sanfengyun.com/www/login.php",
                 "https://api.sanfengyun.com/www/renew.php",
                    "https://api.sanfengyun.com/www/user.php");


    String type;
    String cloudName;
    String os;
    String loginUri;
    String busUri;
    String userUri;

    CloudInfo(String type, String cloudName, String os, String loginUri, String busUri, String userUri) {
        this.type = type;
        this.cloudName = cloudName;
        this.os = os;
        this.loginUri = loginUri;
        this.busUri = busUri;
        this.userUri = userUri;
    }

    public static CloudInfo getCloudInfo(String type){
        for (CloudInfo cloudInfo : values()) {
            if(type.equals(cloudInfo.type)){
                return cloudInfo;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getCloudName() {
        return cloudName;
    }

    public String getOs() {
        return os;
    }

    public String getLoginUri() {
        return loginUri;
    }

    public String getBusUri() {
        return busUri;
    }
}
