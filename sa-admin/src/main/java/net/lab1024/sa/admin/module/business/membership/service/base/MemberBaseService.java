package net.lab1024.sa.admin.module.business.membership.service.base;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.membership.dao.MemberOrderDao;
import net.lab1024.sa.admin.module.business.membership.dao.MemberPackageDao;
import net.lab1024.sa.admin.module.business.membership.dao.MemberPayDao;
import net.lab1024.sa.admin.module.business.membership.dao.MemberUserDao;
import org.springframework.context.ApplicationEventPublisher;

public class MemberBaseService {
    /**
     * 订单业务前缀
     */
    protected static final String ORDER_PREFIX = "ORD";
    protected static final String PAY_PREFIX = "PAY";

    @Resource
    protected MemberOrderDao memberOrderDao;

    @Resource
    protected MemberPackageDao memberPackageDao;

    @Resource
    protected MemberPayDao memberPayDao;

    @Resource
    protected ApplicationEventPublisher eventPublisher;

    @Resource
    protected MemberUserDao memberUserDao;
}
