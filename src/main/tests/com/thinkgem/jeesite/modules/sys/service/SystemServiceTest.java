package com.thinkgem.jeesite.modules.sys.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Amysue on 2016/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-shiro.xml"})
public class SystemServiceTest {
    @Autowired
    private SystemService systemService;

    @Test
    public void updatePasswordById() throws Exception {
        String newPwd    = "123456";
        String loginName = "user";
        for (long id = 1; id <= 14; id++) {
            systemService.updatePasswordById(id, loginName, newPwd);
        }

    }

}