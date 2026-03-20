package net.lab1024.sa.admin.module.business.vip.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户VIP权益表 实体类
 *
 * @Author Mxl
 * @Date 2026-03-20 15:07:19
 * @Copyright 1.0
 */

@Data
@TableName("t_vip_user")
public class VipUserEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * VIP等级
     */
    private Integer vipLevel;

    /**
     * VIP开始时间
     */
    private LocalDateTime startTime;

    /**
     * VIP过期时间
     */
    private LocalDateTime endTime;

    /**
     * 状态: 0-已过期, 1-生效中
     */
    private Integer status;

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
