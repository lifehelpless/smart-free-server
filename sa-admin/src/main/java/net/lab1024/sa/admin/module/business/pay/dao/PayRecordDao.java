package net.lab1024.sa.admin.module.business.pay.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.pay.domain.entity.PayRecordEntity;
import net.lab1024.sa.admin.module.business.pay.domain.form.PayRecordQueryForm;
import net.lab1024.sa.admin.module.business.pay.domain.vo.PayRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 支付流水表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Mapper
public interface PayRecordDao extends BaseMapper<PayRecordEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<PayRecordVO> queryPage(Page page, @Param("queryForm") PayRecordQueryForm queryForm);

}
