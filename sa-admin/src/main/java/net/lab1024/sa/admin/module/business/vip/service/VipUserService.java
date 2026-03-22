package net.lab1024.sa.admin.module.business.vip.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.enums.vip.VipStatusEnum;
import net.lab1024.sa.admin.module.business.vip.dao.VipPackageDao;
import net.lab1024.sa.admin.module.business.vip.dao.VipUserDao;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipPackageEntity;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipUserEntity;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipUserQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipUserVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户VIP权益表 Service
 *
 * @Author Mxl
 * @Date 2026-03-20 15:07:19
 * @Copyright 1.0
 */

@Service
@Slf4j
public class VipUserService {

    @Resource
    private VipUserDao vipUserDao;

    @Resource
    private VipPackageDao vipPackageDao;

    /**
     * 分页查询
     */
    public PageResult<VipUserVO> queryPage(VipUserQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<VipUserVO> list = vipUserDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrExtendVip(Long userId, Integer vipLevel) {
        log.info("开始发放VIP权益, userId: {}, vipLevel: {}", userId, vipLevel);

        // 查询会员信息
        VipPackageEntity vipPackage = vipPackageDao.selectOne(Wrappers.<VipPackageEntity>lambdaQuery().eq(VipPackageEntity::getVipLevel, vipLevel));
        if (vipPackage == null) {
            throw new BusinessException("会员配置错误，请联系管理员");
        }
        // 1. 查询用户当前VIP记录
        VipUserEntity userVip = vipUserDao.selectOne(Wrappers.<VipUserEntity>lambdaQuery().eq(VipUserEntity::getUserId, userId));

        LocalDateTime now = LocalDateTime.now();
        // 增加会员日期
        int addDays = vipPackage.getDurationDays() <= 0 ? 999999 : vipPackage.getDurationDays();

        if (userVip == null) {
            // 2. 首次开通
            userVip = new VipUserEntity();
            userVip.setUserId(userId);
            userVip.setVipLevel(vipLevel);
            userVip.setStartTime(now);
            userVip.setEndTime(now.plusDays(addDays));
            userVip.setStatus(VipStatusEnum.VALID.getCode());
            int count = vipUserDao.insert(userVip);
            if (count <= 0) {
                throw new RuntimeException("开通VIP失败，请稍后重试"); // 实际开发替换为全局自定义异常 SmartException
            }
            log.info("首次开通VIP成功, userId: {}", userId);
        } else {
            // 3. 已经有记录，进行续费或升级
            // 严谨逻辑：如果已经过期，从当前时间算；如果还没过期，在原来的到期时间上累加
            LocalDateTime baseTime = userVip.getEndTime().isAfter(now) ? userVip.getEndTime() : now;

            // 升级逻辑：如果买的等级比现在高，覆盖等级。如果是续费，等级不变。
            if (vipLevel > userVip.getVipLevel()) {
                userVip.setVipLevel(vipLevel);
            }

            userVip.setEndTime(baseTime.plusDays(addDays));
            userVip.setStatus(VipStatusEnum.VALID.getCode());
            int count = vipUserDao.updateById(userVip);
            if (count <= 0) {
                throw new RuntimeException("开通VIP失败，请稍后重试"); // 实际开发替换为全局自定义异常 SmartException
            }
            log.info("续期VIP成功, userId: {}, 新到期时间: {}", userId, userVip.getEndTime());
        }
    }


}
