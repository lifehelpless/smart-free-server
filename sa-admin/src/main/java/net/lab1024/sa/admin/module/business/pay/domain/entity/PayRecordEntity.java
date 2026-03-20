package net.lab1024.sa.admin.module.business.pay.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 支付流水表 实体类
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Data
@TableName("t_pay_record")
public class PayRecordEntity {

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 支付流水号(发给第三方的单号)
     */
    private String payNo;

    /**
     * 关联业务订单号
     */
    private String orderNo;

    /**
     * 支付渠道: WECHAT, ALIPAY
     */
    private String payChannel;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付状态: 0-支付中, 1-支付成功, 2-支付失败
     */
    private Integer status;

    /**
     * 第三方支付交易号
     */
    private String thirdPartyNo;

    /**
     * 支付成功时间
     */
    private LocalDateTime payTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
