package net.lab1024.sa.admin.module.business.order.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.enums.order.OrderStatusEnum;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.service.OrderService;
import net.lab1024.sa.admin.module.business.pay.event.PaySuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 监听支付成功事件，更新订单状态
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private OrderService orderService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onPaySuccess(PaySuccessEvent event) {
        String orderNo = event.getOrderNo();
        log.info("订单模块监听到支付成功事件, 开始处理订单: {}", orderNo);

        // 1. 查询订单
        OrderEntity order = orderService.selectOne(Wrappers.<OrderEntity>lambdaQuery().eq(OrderEntity::getOrderNo, orderNo));
        if (order == null) {
            log.error("未找到对应订单: {}", orderNo);
            return;
        }

        // 2. 幂等性校验 (防止重复消费)
        if (!OrderStatusEnum.WAIT_PAY.getCode().equals(order.getStatus())) {
            log.warn("订单状态异常或已处理, 当前状态: {}, 订单号: {}", order.getStatus(), orderNo);
            return;
        }

        // 3. 金额校验 (严谨的系统必须校验支付金额和订单金额是否一致)
        if (order.getOrderAmount().compareTo(event.getPayAmount()) != 0) {
            log.error("支付金额与订单金额不符! 订单金额: {}, 支付金额: {}", order.getOrderAmount(), event.getPayAmount());
            // 记录异常，可能需要人工介入或自动退款
            return;
        }

        // 4. 更新订单状态为已支付
        order.setStatus(OrderStatusEnum.PAID.getCode());
        orderService.updateById(order);

        log.info("订单状态已更新为[已支付], orderNo: {}", orderNo);
        
        // 5. TODO: (下一步骤) Order 模块继续发布 OrderCompletedEvent，通知 VIP 模块增加权益时长！
    }
}