package net.lab1024.sa.admin.module.business.membership.service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.membership.constant.MemberPayStatusEnum;
import net.lab1024.sa.admin.module.business.membership.dao.MemberPayDao;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberPayEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPayVO;
import net.lab1024.sa.admin.module.business.membership.event.PaySuccessEvent;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class MemberPayService {

    private static final String PAY_PREFIX = "PAY";

    @Resource
    private MemberPayDao memberPayDao;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 分页查询
     */
    public PageResult<MemberPayVO> queryPage(MemberPayQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MemberPayVO> list = memberPayDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public String doPay(MemberPayAddForm addForm) {
        log.info("接收到支付请求, 订单号: {}", addForm.getOrderNo());

        // 1. 生成支付流水号
        String payNo = PAY_PREFIX + IdWorker.getIdStr();

        // 2. 构建并保存支付流水
        MemberPayEntity record = new MemberPayEntity();
        record.setPayNo(payNo);
        record.setOrderNo(addForm.getOrderNo());
        record.setPayChannel(addForm.getPayChannel());
        record.setPayAmount(addForm.getPayAmount());
        record.setStatus(MemberPayStatusEnum.PAYING.getCode());

        memberPayDao.insert(record);

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
