package net.lab1024.sa.admin.module.freeserver.renew.strategy.impl;

import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.renew.strategy.AbstractRenewalStrategy;
import org.springframework.stereotype.Component;

/**
 * 策略1：图库抽图延期
 */
@Component
public class GalleryRenewalStrategy extends AbstractRenewalStrategy {
    @Override
    public Integer getMethodType() { 
        return 1; // 1代表图库策略
    }

    @Override
    protected String getImageUrl(UserCloudServerEntity serverEntity) {
        // TODO: 从你的图库随机抽图并返回URL
        return "https://oss.my-domain.com/random/pic1.jpg";
    }
}