package net.lab1024.sa.admin.module.freeserver.instance.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户云服务器配置 实体类
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@Data
@TableName("t_user_cloud_server")
public class UserCloudServerEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务器类型 0阿贝云/1三丰云
     */
    private Integer serverType;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 是否开启邮件通知 1关闭 0开启
     */
    private Integer enableEmail;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户ID
     */
    private Long createId;

    /**
     * 修改用户ID
     */
    private Long updateId;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
