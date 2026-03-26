package net.lab1024.sa.admin.module.business.membership.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberPayEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPayVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 支付流水表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Mapper
public interface MemberPayDao extends BaseMapper<MemberPayEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MemberPayVO> queryPage(Page page, @Param("queryForm") MemberPayQueryForm queryForm);

    MemberPayVO queryByOrderNoAndStatus(@Param("orderNo") String orderNo, @Param("status") Integer status);

}
