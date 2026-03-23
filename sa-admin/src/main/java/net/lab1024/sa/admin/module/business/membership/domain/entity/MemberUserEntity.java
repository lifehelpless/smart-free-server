package net.lab1024.sa.admin.module.business.membership.domain.entity;

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
public class MemberUserEntity {

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
     * 当前套餐ID
     */
    private Long packageId;

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
    private LocalDateTime expireTime;

    /**
     * 当前服务额度
     */
    private Integer serviceLimit;

    /**
     * 最后一次支付单号
     */
    private String lastOrderNo;

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
