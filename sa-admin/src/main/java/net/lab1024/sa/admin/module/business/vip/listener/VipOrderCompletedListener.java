package net.lab1024.sa.admin.module.business.vip.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.common.event.OrderCompletedEvent;
import net.lab1024.sa.admin.enums.vip.VipLevelEnum;
import net.lab1024.sa.admin.module.business.vip.service.VipUserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 监听订单完成事件，自动发放会员
 */
@Slf4j
@Component
public class VipOrderCompletedListener {

    @Resource
    private VipUserService vipUserService;

    @EventListener
    public void onOrderCompleted(OrderCompletedEvent event) {
        Long userId = event.getUserId();
        Long goodsId = event.getGoodsId(); // 代表购买的套餐
        log.info("VIP模块监听到订单完成事件, 准备发放权益. userId: {}, goodsId: {}", userId, goodsId);

        // TODO: 实际项目中这里应该查一张 `t_vip_package` (套餐表) 拿到对应增加的天数和等级
        // 这里为了示例，我们直接做个 Mock 映射：
        Integer vipLevel = VipLevelEnum.NORMAL_VIP.getCode();
        Integer addDays = 30; // 默认加30天(月卡)

        // 举例：假如商品ID是2，代表超级会员年卡
        if (Objects.equals(goodsId, Long.valueOf(VipLevelEnum.SUPER_VIP.getCode()))) {
            vipLevel = VipLevelEnum.SUPER_VIP.getCode();
            addDays = 365;
        }

        // 调用发放逻辑
        try {
            vipUserService.addOrExtendVip(userId, vipLevel, addDays);
        } catch (Exception e) {
            // 生产环境这里要记录异常日志，甚至抛出告警通知管理员手动补单
            log.error("发放VIP失败! userId: {}, 错误信息: {}", userId, e.getMessage(), e);
            throw new RuntimeException("发放VIP失败，请稍后重试");
        }
    }
}