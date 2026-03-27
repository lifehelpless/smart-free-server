package net.lab1024.sa.admin.module.freeserver.renew.strategy.impl;

import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.renew.constant.MethodRenewalTypeEnum;
import net.lab1024.sa.admin.module.freeserver.renew.strategy.AbstractRenewalStrategy;
import org.springframework.stereotype.Component;

/**
 * 策略2：主机测评网站截图延期
 */
@Component
public class WebsiteScreenRenewalStrategy extends AbstractRenewalStrategy {
    @Override
    public Integer getMethodType() { 
        return MethodRenewalTypeEnum.ZJ_PC.getType(); // 2代表截图策略
    }

    @Override
    public String getImageUrl() {
        // TODO: 触发截图工具，上传服务器，返回URL
        return "https://oss.my-domain.com/screenshot/site2.jpg";
    }
}