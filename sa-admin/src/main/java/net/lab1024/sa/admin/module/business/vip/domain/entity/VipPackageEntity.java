package net.lab1024.sa.admin.module.business.vip.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * VIP会员套餐表 实体类
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@Data
@TableName("t_vip_package")
public class VipPackageEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 套餐名称 (如: 普通会员包月、超级会员包年)
     */
    private String name;

    /**
     * 绑定的会员等级: VipLevelEnum
     */
    private Integer vipLevel;

    /**
     * 会员可用时间(天数)，如: 30, 90, 365
     */
    private Integer durationDays;

    /**
     * 会员可添加配置数量上限 (核心权益)
     */
    private Integer configLimit;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 实际售卖价格
     */
    private BigDecimal actualPrice;

    /**
     * 排序码(降序排列)
     */
    private Integer sortCode;

    /**
     * 状态: 0-下架, 1-上架
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
