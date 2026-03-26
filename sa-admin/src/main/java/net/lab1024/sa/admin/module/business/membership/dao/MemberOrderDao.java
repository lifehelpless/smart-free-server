package net.lab1024.sa.admin.module.business.membership.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberOrderEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.order.MemberOrderQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 业务订单表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@Mapper
public interface MemberOrderDao extends BaseMapper<MemberOrderEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MemberOrderVO> queryPage(Page page, @Param("queryForm") MemberOrderQueryForm queryForm);

    MemberOrderVO queryByOrderNo(@Param("orderNo") String orderNo);

}
