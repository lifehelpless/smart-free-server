package net.lab1024.sa.admin.module.business.membership.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 业务订单表 列表VO
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@Data
public class MemberOrderVO {


    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单号(业务主键)")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "商品/服务ID(如某VIP套餐ID)")
    private Long goodsId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "订单总金额")
    private BigDecimal orderAmount;

    @Schema(description = "订单状态: 0-待支付, 1-已支付, 2-已取消")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    // ========前端展示字段
    @Schema(description = "用戶名称")
    private String userName;

}
