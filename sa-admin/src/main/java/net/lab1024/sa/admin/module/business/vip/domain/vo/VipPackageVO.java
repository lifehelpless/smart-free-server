package net.lab1024.sa.admin.module.business.vip.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * VIP会员套餐表 列表VO
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@Data
public class VipPackageVO {


    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "套餐名称 (如: 普通会员包月、超级会员包年)")
    private String name;

    @Schema(description = "绑定的会员等级: VipLevelEnum")
    private Integer vipLevel;

    @Schema(description = "会员可用时间(天数)，如: 30, 90, 365")
    private Integer durationDays;

    @Schema(description = "会员可添加配置数量上限 (核心权益)")
    private Integer configLimit;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "实际售卖价格")
    private BigDecimal actualPrice;

    @Schema(description = "排序码(降序排列)")
    private Integer sortCode;

    @Schema(description = "状态: 1-下架, 0-上架")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
