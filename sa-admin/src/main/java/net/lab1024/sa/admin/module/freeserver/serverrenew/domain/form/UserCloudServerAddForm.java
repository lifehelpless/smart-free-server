package net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户云服务器配置 新建表单
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@Data
public class UserCloudServerAddForm {

    @Schema(description = "服务器类型 0/1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "服务器类型 0/1 不能为空")
    private Integer serverType;

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号 不能为空")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码 不能为空")
    private String password;

    @Schema(description = "是否开启邮件通知 1关闭 0开启")
    private Integer enableEmail;

    @Schema(description = "是否启用  1关闭 0开启")
    private Integer enable;

}