package net.lab1024.sa.admin.module.business.membership.dao;

import java.util.List;

import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberUserEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.user.MemberUserQueryForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberUserVO;
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
public interface MemberUserDao extends BaseMapper<MemberUserEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MemberUserVO> queryPage(Page page, @Param("queryForm") MemberUserQueryForm queryForm);

}
