package net.lab1024.sa.admin.module.freeserver.renew.strategy;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.renew.manager.PostponeManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 延期策略抽象父类 (模板方法模式)
 * @author FreeServer
 */
@Slf4j
public abstract class AbstractRenewalStrategy {

    @Autowired
    protected PostponeManager postponeManager;

    /**
     * 返回策略类型标识 (1:图库随机, 2:网站截图 等)
     * @return Integer
     */
    public abstract Integer getMethodType();

    /**
     * 子类差异化实现：获取用于延期的图片 URL
     * @param serverEntity 云服务器配置信息
     * @return 图片完整URL
     */
    protected abstract String getImageUrl(UserCloudServerEntity serverEntity);

    /**
     * 模板方法：主执行链路 (加 final 关键字，防止子类篡改核心流程)
     */
    public final boolean executeRenewal(UserCloudServerEntity serverEntity) {
        log.info("开始执行延期任务，云服务ID: {}", serverEntity.getId());
        
        // 1. 获取延期凭证图片URL (调用子类具体实现)
        String imageUrl = getImageUrl(serverEntity);
        if (imageUrl == null || imageUrl.isEmpty()) {
            log.error("获取延期图片失败，云服务ID: {}", serverEntity.getId());
            return false;
        }

        // 2. 执行共用逻辑：上传并提交延期
        return commonUploadAndSubmit(serverEntity, imageUrl);
    }

    /**
     * 共用逻辑：使用提取到的 imageUrl 进行统一的 API 延期操作
     */
    private boolean commonUploadAndSubmit(UserCloudServerEntity serverEntity, String imageUrl) {
        log.info("提取到图片URL: {}, 准备提交给阿贝云/三丰云...", imageUrl);
        try {
            // TODO: 调用你现有的 postponeManager 里的方法
            // 例如：postponeManager.doRenew(serverEntity.getIp(), imageUrl);
            return true; 
        } catch (Exception e) {
            log.error("延期提交异常, ID: {}", serverEntity.getId(), e);
            return false;
        }
    }
}