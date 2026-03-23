package net.lab1024.sa.admin.module.business.membership.domain.vo;

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
public class MemberPackageVO {


    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "套餐名称 (如: 普通会员包月、超级会员包年)")
    private String name;

    @Schema(description = "绑定的会员等级: 等级权重: 数值越大等级越高")
    private Integer vipLevel;

    @Schema(description = "会员可用时间(天数)，如: 30, 90, 365")
    private Integer durationDays;

    @Schema(description = "会员可添加服务配置数量上限 (核心权益)")
    private Integer serviceLimit;

    @Schema(description = "实际售卖价格")
    private BigDecimal price;

    @Schema(description = "排序码")
    private Integer sortCode;

    @Schema(description = "参数类型: 1内置 2自定义")
    private Integer type;

    @Schema(description = "状态: 1-下架, 0-上架")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
