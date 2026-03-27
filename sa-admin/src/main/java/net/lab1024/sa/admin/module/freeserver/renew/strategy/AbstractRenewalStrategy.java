package net.lab1024.sa.admin.module.freeserver.renew.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 延期策略抽象父类 (模板方法模式)
 * @author FreeServer
 */
@Slf4j
public abstract class AbstractRenewalStrategy {

    /**
     * 返回策略类型标识 (1:图库随机, 2:网站截图 等)
     * @return Integer
     */
    public abstract Integer getMethodType();

    /**
     * 子类差异化实现：获取用于延期的图片 URL
     * @return 图片完整URL
     */
    public abstract String getImageUrl();
}