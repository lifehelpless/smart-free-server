package net.lab1024.sa.admin.module.business.membership.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.common.event.OrderCompletedEvent;
import net.lab1024.sa.admin.module.business.membership.service.MemberUserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
/**
 * 监听订单完成事件，自动发放会员
 */
@Slf4j
@Component
public class OrderCompletedListener {

    @Resource
    private MemberUserService memberUserService;

    @EventListener
    public void onOrderCompleted(OrderCompletedEvent event) {
        Long userId = event.getUserId();
        Long goodsId = event.getGoodsId(); // 代表购买的套餐
        log.info("VIP模块监听到订单完成事件, 准备发放权益. userId: {}, goodsId: {}", userId, goodsId);

        // 调用发放逻辑
        try {
            memberUserService.addOrExtendVip(userId, Math.toIntExact(goodsId));
        } catch (Exception e) {
            // 生产环境这里要记录异常日志，甚至抛出告警通知管理员手动补单
            log.error("发放VIP失败! userId: {}, 错误信息: {}", userId, e.getMessage(), e);
            throw new RuntimeException("发放VIP失败，请稍后重试");
        }
    }
}