package net.lab1024.sa.admin.module.freeserver.renew.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.renew.constant.MethodRenewalTypeEnum;
import net.lab1024.sa.admin.module.freeserver.renew.strategy.AbstractRenewalStrategy;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

/**
 * 策略1：图库抽图延期
 */
@Component
@Slf4j
public class GalleryRenewalStrategy extends AbstractRenewalStrategy {

    // 模拟配置常量，后续可移至 yml
    private static final String SOURCE_DIR = "/data/oss/raw_images/";
    private static final String TARGET_DIR = "/data/oss/server_covers/";
    private static final String DOMAIN = "https://oss.my-domain.com/server_covers/";

    @Override
    public Integer getMethodType() { 
        return MethodRenewalTypeEnum.IMAGE.getType(); // 1代表图库策略
    }

    @Override
    public String getImageUrl() {
        return this.copyAndGenerateRandomImage();
    }

    /**
     * 拷贝至新目录
     * @return
     */
    public String copyAndGenerateRandomImage() {
        File sourceDir = new File(SOURCE_DIR);
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (files == null || files.length == 0) {
            return null;
        }

        // 1. 随机选择一个文件
        File sourceFile = files[new Random().nextInt(files.length)];
        String extension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."));

        // 2. 生成新文件名：随机数(4位) + 时间戳
        String newFileName = String.format("%04d", new Random().nextInt(10000))
                + System.currentTimeMillis() + extension;

        File targetFile = new File(TARGET_DIR, newFileName);

        try {
            // 确保目标目录存在
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 3. 执行拷贝
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return DOMAIN + newFileName;
        } catch (IOException e) {
            log.error("文件拷贝失败", e);
            throw new BusinessException("系统资源处理异常");
        }
    }
}