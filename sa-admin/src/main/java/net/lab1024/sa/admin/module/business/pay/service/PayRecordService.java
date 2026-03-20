package net.lab1024.sa.admin.module.business.pay.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.enums.pay.PayStatusEnum;
import net.lab1024.sa.admin.module.business.pay.dao.PayRecordDao;
import net.lab1024.sa.admin.module.business.pay.domain.entity.PayRecordEntity;
import net.lab1024.sa.admin.module.business.pay.domain.form.PayRecordAddForm;
import net.lab1024.sa.admin.module.business.pay.domain.form.PayRecordQueryForm;
import net.lab1024.sa.admin.module.business.pay.domain.vo.PayRecordVO;
import net.lab1024.sa.admin.module.business.pay.event.PaySuccessEvent;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PayRecordService {

    private static final String PAY_PREFIX = "PAY";

    @Resource
    private PayRecordDao payRecordDao;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 分页查询
     */
    public PageResult<PayRecordVO> queryPage(PayRecordQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<PayRecordVO> list = payRecordDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public String doPay(PayRecordAddForm addForm) {
        log.info("接收到支付请求, 订单号: {}", addForm.getOrderNo());

        // 1. 生成支付流水号
        String payNo = PAY_PREFIX + IdWorker.getIdStr();

        // 2. 构建并保存支付流水
        PayRecordEntity record = new PayRecordEntity();
        record.setPayNo(payNo);
        record.setOrderNo(addForm.getOrderNo());
        record.setPayChannel(addForm.getPayChannel());
        record.setPayAmount(addForm.getPayAmount());
        record.setStatus(PayStatusEnum.PAYING.getCode());

        payRecordDao.insert(record);

        // 3. TODO: 调用微信/支付宝 API 获取支付参数 (这里模拟返回一个支付链接或二维码串)
        String mockPayUrl = "https://mock-pay.com/qr/" + payNo;
        log.info("构建支付单成功, payNo: {}", payNo);

        return mockPayUrl;
    }


    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(String channel, String notifyData) {
        log.info("收到第三方支付回调, 渠道: {}, 数据: {}", channel, notifyData);

        // 1. TODO: 解析 notifyData 并验签 (此处模拟解析出参数)
        String payNo = "解析出的支付流水号";
        String thirdPartyNo = "微信或支付宝的交易单号";

        // 2. 查询本地流水
        PayRecordEntity record = payRecordDao.selectOne(Wrappers.<PayRecordEntity>lambdaQuery().eq(PayRecordEntity::getPayNo, payNo));
        if (record == null || !PayStatusEnum.PAYING.getCode().equals(record.getStatus())) {
            log.warn("支付流水不存在或已被处理, payNo: {}", payNo);
            return;
        }

        // 3. 更新支付流水状态为成功
        record.setStatus(PayStatusEnum.SUCCESS.getCode());
        record.setThirdPartyNo(thirdPartyNo);
        record.setPayTime(LocalDateTime.now());
        payRecordDao.updateById(record);

        // 4. 发布支付成功事件，通知 Order 模块发货/更新状态 (彻底解耦)
        PaySuccessEvent event = new PaySuccessEvent(this, record.getOrderNo(), payNo, thirdPartyNo, record.getPayAmount());
        eventPublisher.publishEvent(event);
        log.info("支付回调处理完毕，已发布支付成功事件, payNo: {}", payNo);
    }
}
