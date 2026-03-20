package net.lab1024.sa.admin.module.business.vip.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * VIP会员套餐表 新建表单
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@Data
public class VipPackageAddForm {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键ID 不能为空")
    private Long id;

    @Schema(description = "套餐名称 (如: 普通会员包月、超级会员包年)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐名称 (如: 普通会员包月、超级会员包年) 不能为空")
    private String name;

    @Schema(description = "绑定的会员等级: VipLevelEnum", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "绑定的会员等级: VipLevelEnum 不能为空")
    private Integer vipLevel;

    @Schema(description = "会员可用时间(天数)，如: 30, 90, 365", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会员可用时间(天数)，如: 30, 90, 365 不能为空")
    private Integer durationDays;

    @Schema(description = "会员可添加配置数量上限 (核心权益)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会员可添加配置数量上限 (核心权益) 不能为空")
    private Integer configLimit;

    @Schema(description = "原价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原价 不能为空")
    private BigDecimal originalPrice;

    @Schema(description = "实际售卖价格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际售卖价格 不能为空")
    private BigDecimal actualPrice;

    @Schema(description = "排序码(降序排列)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序码(降序排列) 不能为空")
    private Integer sortCode;

    @Schema(description = "状态: 1-下架, 0-上架", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态: 1-下架, 0-上架 不能为空")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间 不能为空")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "修改时间 不能为空")
    private LocalDateTime updateTime;

}