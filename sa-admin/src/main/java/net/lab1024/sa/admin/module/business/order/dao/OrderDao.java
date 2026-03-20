package net.lab1024.sa.admin.module.business.order.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 业务订单表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<OrderVO> queryPage(Page page, @Param("queryForm") OrderQueryForm queryForm);

}
