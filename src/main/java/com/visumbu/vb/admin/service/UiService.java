/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.model.VbUser;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("uiService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UiService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UiDao uiDao;
    public List getDashboard(VbUser user) {
        return uiDao.getDashboards(user);
    }
}
