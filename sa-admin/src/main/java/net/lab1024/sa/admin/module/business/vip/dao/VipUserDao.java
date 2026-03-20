package net.lab1024.sa.admin.module.business.vip.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipUserEntity;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipUserQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户VIP权益表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 15:07:19
 * @Copyright 1.0
 */

@Mapper
public interface VipUserDao extends BaseMapper<VipUserEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<VipUserVO> queryPage(Page page, @Param("queryForm") VipUserQueryForm queryForm);

}
