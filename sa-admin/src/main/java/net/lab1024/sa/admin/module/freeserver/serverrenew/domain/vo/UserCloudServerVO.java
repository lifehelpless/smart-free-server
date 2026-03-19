package net.lab1024.sa.admin.module.freeserver.serverrenew.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户云服务器配置 列表VO
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@Data
public class UserCloudServerVO {


    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "服务器类型 0阿贝云/1三丰云")
    private Integer serverType;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否启用")
    private Integer enable;

    @Schema(description = "是否开启邮件通知 1关闭 0开启")
    private Integer enableEmail;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建用户ID")
    private Long createId;

    @Schema(description = "修改用户ID")
    private Long updateId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
