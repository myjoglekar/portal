/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.VbUtils;
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

    public VbUser create(VbUser teUser) {
        List<VbUser> users = userDao.findByUserName(teUser.getUserName());
        if (users.isEmpty()) {
            teUser.setStatus("Active");
            teUser.setCreatedTime(new Date());
            teUser.setTheme("default");
            return (VbUser) userDao.create(teUser);
        }
        return null;
    }

    public VbUser read(Integer id) {
        return (VbUser) userDao.read(VbUser.class, id);
    }

    public List<VbUser> read() {
        List<VbUser> users = userDao.read();
        return users;
    }

    public VbUser update(VbUser teUser) {
        return (VbUser) userDao.update(teUser);
    }

    public VbUser delete(Integer id) {
        VbUser teUser = read(id);
        teUser.setStatus("Deleted");
        return update(teUser);
    }

    public VbUser delete(VbUser teUser) {
        return (VbUser) userDao.delete(teUser);
    }

    public VbUser findByUsername(String username) {
        List<VbUser> vbUsers = userDao.findByUserName(username);
        if (!vbUsers.isEmpty()) {
            return vbUsers.get(0);
        }
        return null;
    }

    public SecurityAuthBean getPermissions(LoginUserBean userBean) {
        return VbUtils.getAuthData(userBean.getUsername(), userBean.getPassword());
    }

    public SecurityAuthBean getPermissions(String accessToken) {
        return VbUtils.getAuthData(accessToken);
    }

    public LoginUserBean authenicate(LoginUserBean userBean) {
        List<VbUser> users = userDao.findByUserName(userBean.getUsername());
        LoginUserBean loginUserBean = null;
        if (!users.isEmpty()) {
            VbUser user = users.get(0);
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

    private LoginUserBean toLoginUserBean(VbUser teUser) {
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(teUser.getUserName());
        userBean.setFailLoginCount(teUser.getFailedLoginCount());
        userBean.setIsAdmin(teUser.getIsAdmin() != null && teUser.getIsAdmin() == true ? "admin" : "");
        return userBean;
    }
}
