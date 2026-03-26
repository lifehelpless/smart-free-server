package net.lab1024.sa.admin.module.business.membership.service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.membership.constant.MemberOrderStatusEnum;
import net.lab1024.sa.admin.module.business.membership.constant.MemberPayStatusEnum;
import net.lab1024.sa.admin.module.business.membership.dao.MemberPayDao;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberOrderEntity;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberPayEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberOrderVO;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPayVO;
import net.lab1024.sa.admin.module.business.membership.event.PaySuccessEvent;
import net.lab1024.sa.admin.module.business.membership.service.base.MemberBaseService;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.payment.CashierSupport;
import net.lab1024.sa.base.module.support.payment.dto.PayParam;
import net.lab1024.sa.base.module.support.payment.enums.PaymentClientEnum;
import net.lab1024.sa.base.module.support.payment.enums.PaymentMethodEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付流水表 Service
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Service
@Slf4j
public class MemberPayService extends MemberBaseService {

    @Resource
    private CashierSupport cashierSupport;

    /**
     * 分页查询
     */
    public PageResult<MemberPayVO> queryPage(MemberPayQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MemberPayVO> list = memberPayDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 创建支付数据并获取支付二维码
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String createPayQrCode(MemberPayAddForm addForm) {
        log.info("接收到支付请求, 订单号: {}", addForm.getOrderNo());

        // 1、找寻订单信息
        MemberOrderVO memberOrderVO = memberOrderDao.queryByOrderNo(addForm.getOrderNo());
        if (memberOrderVO == null) {
            throw new BusinessException("订单不存在，请刷新后尝试");
        }
        if (memberOrderVO.getStatus().equals(MemberOrderStatusEnum.WAIT_PAY.getCode())) {
            throw new BusinessException("订单已支付/取消等，不可重复操作");
        }

        // 2、寻找是否存在支付中的数据
        MemberPayVO memberPayVO = memberPayDao.queryByOrderNoAndStatus(memberOrderVO.getOrderNo(), MemberPayStatusEnum.PAYING.getCode());
        if (memberPayVO != null) {
            // 已经存在支付中数据，直接返回结果
            mockPay(addForm.getOrderNo());
            return memberPayVO.getPayNo();
        }

        // 3. 生成支付流水号
        String payNo = PAY_PREFIX + IdWorker.getIdStr();

        // 4. 构建并保存支付流水
        MemberPayEntity record = new MemberPayEntity();
        record.setPayNo(payNo);
        record.setOrderNo(addForm.getOrderNo());
        record.setPayChannel(addForm.getPayChannel());
        record.setPayAmount(memberOrderVO.getOrderAmount());
        record.setStatus(MemberPayStatusEnum.PAYING.getCode());

        int count = memberPayDao.insert(record);
        if (count <= 0) {
            throw new BusinessException("支付创建失败，请刷新后重试");
        }

        // 3. TODO: 调用微信/支付宝 API 获取支付参数 (这里模拟返回一个支付链接或二维码串)
        String mockPayUrl = (String) mockPay(addForm.getOrderNo());
        log.info("构建支付单成功, payNo: {}", payNo);

        return mockPayUrl;
    }

    private Object mockPay(String orderNo) {
        // 调用支付宝预下单接口
        PayParam payParam = new PayParam();
        payParam.setOrderNo(orderNo);
        payParam.setOrderType("ORDER");
        return cashierSupport.payment(PaymentMethodEnum.ALIPAY, PaymentClientEnum.NATIVE, null, null, payParam);
    }


    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(String channel, String notifyData) {
        log.info("收到第三方支付回调, 渠道: {}, 数据: {}", channel, notifyData);

        // 1. TODO: 解析 notifyData 并验签 (此处模拟解析出参数)
        String payNo = "解析出的支付流水号";
        String thirdPartyNo = "微信或支付宝的交易单号";

        // 2. 查询本地流水
        MemberPayEntity record = memberPayDao.selectOne(Wrappers.<MemberPayEntity>lambdaQuery().eq(MemberPayEntity::getPayNo, payNo));
        if (record == null || !MemberPayStatusEnum.PAYING.getCode().equals(record.getStatus())) {
            log.warn("支付流水不存在或已被处理, payNo: {}", payNo);
            return;
        }

        // 3. 更新支付流水状态为成功
        record.setStatus(MemberPayStatusEnum.SUCCESS.getCode());
        record.setThirdPartyNo(thirdPartyNo);
        record.setPayTime(LocalDateTime.now());
        memberPayDao.updateById(record);

        // 4. 发布支付成功事件，通知 Order 模块发货/更新状态 (彻底解耦)
        PaySuccessEvent event = new PaySuccessEvent(this, record.getOrderNo(), payNo, thirdPartyNo, record.getPayAmount());
        eventPublisher.publishEvent(event);
        log.info("支付回调处理完毕，已发布支付成功事件, payNo: {}", payNo);
    }
}
