/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.model.WaUser;
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
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {

    @Autowired
    private UserDao userDao;

    public WaUser create(WaUser teUser) {
        List<WaUser> users = userDao.findByUserName(teUser.getUserName());
        if (users.isEmpty()) {
            teUser.setStatus("Active");
            teUser.setCreatedTime(new Date());
            teUser.setTheme("default");
            return (WaUser) userDao.create(teUser);
        }
        return null;
    }

    public WaUser read(Integer id) {
        return (WaUser) userDao.read(WaUser.class, id);
    }

    public List<WaUser> read() {
        List<WaUser> users = userDao.read();
        return users;
    }

    public WaUser update(WaUser teUser) {
        return (WaUser) userDao.update(teUser);
    }

    public WaUser delete(Integer id) {
        WaUser teUser = read(id);
        teUser.setStatus("Deleted");
        return update(teUser);
    }

    public WaUser delete(WaUser teUser) {
        return (WaUser) userDao.delete(teUser);
    }

    public LoginUserBean authenicate(LoginUserBean userBean) {
        List<WaUser> users = userDao.findByUserName(userBean.getUsername());
        LoginUserBean loginUserBean = null;
        if (!users.isEmpty()) {
            WaUser user = users.get(0);
            if (user.getPassword().equals(userBean.getPassword())) {
                user.setFailedLoginCount(0);
                user.setLastLoginTime(new Date());
                loginUserBean = toLoginUserBean(user);
                loginUserBean.setAuthenticated(Boolean.TRUE);
            } else {
                user.setFailedLoginCount(user.getFailedLoginCount() + 1);
                loginUserBean = toLoginUserBean(user);
                loginUserBean.setAuthenticated(Boolean.FALSE);
                loginUserBean.setErrorMessage("Invalid Username or Password");
            }
        }
        if (loginUserBean == null) {
            loginUserBean = new LoginUserBean();
            loginUserBean.setErrorMessage("Invalid Login");
        }
        return loginUserBean;
    }

    private LoginUserBean toLoginUserBean(WaUser teUser) {
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(teUser.getUserName());
        userBean.setFailLoginCount(teUser.getFailedLoginCount());
        userBean.setIsAdmin(teUser.getIsAdmin() != null && teUser.getIsAdmin() == true? "admin" : "");
        return userBean;
    }
}
