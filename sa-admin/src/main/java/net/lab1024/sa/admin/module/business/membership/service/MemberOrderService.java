package net.lab1024.sa.admin.module.business.membership.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.membership.constant.MemberOrderStatusEnum;
import net.lab1024.sa.admin.module.business.membership.dao.MemberOrderDao;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberOrderEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.order.MemberOrderAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.order.MemberOrderQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberOrderVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务订单表 Service
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@Service
@Slf4j
public class MemberOrderService {

    /**
     * 订单业务前缀
     */
    private static final String ORDER_PREFIX = "ORD";

    @Resource
    private MemberOrderDao memberOrderDao;

    /**
     * 分页查询
     */
    public PageResult<MemberOrderVO> queryPage(MemberOrderQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MemberOrderVO> list = memberOrderDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 创建订单
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String createBizOrder(MemberOrderAddForm addForm) {
        Long requestUserId = SmartRequestUtil.getRequestUserId();
        // 1. 基础校验 (如果是复杂的商品库存/VIP套餐校验，应该在这里调用相关Service)
        log.info("开始创建订单, userId: {}, goodsId: {}", requestUserId, addForm.getGoodsId());

        // 2. 生成业务订单号 (使用 MyBatis-Plus 的雪花算法生成唯一且趋势递增的 ID)
        String orderNo = ORDER_PREFIX + IdWorker.getIdStr();

        // 3. 构建订单实体
        MemberOrderEntity order = new MemberOrderEntity();
        order.setOrderNo(orderNo);
        order.setUserId(requestUserId);
        order.setGoodsId(addForm.getGoodsId());
        order.setGoodsName(addForm.getGoodsName());
        order.setOrderAmount(addForm.getOrderAmount());
        // 0-待支付 (通常定义在枚举类中，如 MemberOrderStatusEnum.WAIT_PAY.getCode())
        order.setStatus(MemberOrderStatusEnum.WAIT_PAY.getCode());

        // 4. 入库保存 (此处继承自 ServiceImpl，直接调用 save)
        int count = memberOrderDao.insert(order);
        if (count <= 0) {
            log.error("订单创建失败, 订单号: {}", orderNo);
            throw new RuntimeException("订单创建失败，请稍后重试"); // 实际开发替换为全局自定义异常 SmartException
        }

        log.info("订单创建成功, 订单号: {}", orderNo);
        return orderNo;
    }


    public MemberOrderEntity selectOne(LambdaQueryWrapper<MemberOrderEntity> queryWrapper) {
        return memberOrderDao.selectOne(queryWrapper);
    }

    public int updateById(MemberOrderEntity order) {
        return memberOrderDao.updateById(order);
    }
}
