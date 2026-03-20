//package net.lab1024.sa.admin.module.business.pay.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 支付宝配置类
// */
//@Data
//@Configuration
//@ConfigurationProperties(prefix = "alipay")
//public class AlipayConfig {
//
//    /**
//     * 支付宝分配给开发者的应用ID
//     */
//    private String appId;
//
//    /**
//     * 开发者应用私钥 (PKCS8格式)
//     */
//    private String privateKey;
//
//    /**
//     * 支付宝公钥 (用于验签)
//     */
//    private String alipayPublicKey;
//
//    /**
//     * 异步通知地址 (必须是外网可以直接访问的URL)
//     */
//    private String notifyUrl;
//
//    /**
//     * 同步跳转地址 (支付成功后前端跳回的页面)
//     */
//    private String returnUrl;
//
//    /**
//     * 支付宝网关
//     */
//    private String gatewayUrl = "https://openapi.alipay.com/gateway.do";
//
//    /**
//     * 签名类型
//     */
//    private String signType = "RSA2";
//
//    /**
//     * 字符编码格式
//     */
//    private String charset = "UTF-8";
//
//    /**
//     * 数据格式
//     */
//    private String format = "json";
//}