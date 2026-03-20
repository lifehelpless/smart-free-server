package net.lab1024.sa.admin.module.business.order.manager;

import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderAddForm;
import org.springframework.stereotype.Service;

/**
 * 业务订单表  Manager
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */
@Service
public class OrderManager extends ServiceImpl<OrderDao, OrderEntity> {
}
