/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.VbUtils;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
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

    @Autowired
    private DealerDao dealerDao;

    final static Logger log = Logger.getLogger(UserService.class);

    public VbUser create(VbUser teUser) {
        log.debug("Start function of create in UserService class - VbUser");
        List<VbUser> users = userDao.findByUserName(teUser.getUserName());
        if (users.isEmpty()) {
            teUser.setStatus("Active");
            teUser.setCreatedTime(new Date());
            teUser.setTheme("default");
            log.debug("End function of create in UserService class - VbUser");
            return (VbUser) userDao.create(teUser);
        }
        log.debug("End function of create in UserService class - VbUser");
        return null;
    }

    public VbUser read(Integer id) {
        log.debug("Start function of read in UserService class - VbUser");
        log.debug("End function of read in UserService class - VbUser");
        return (VbUser) userDao.read(VbUser.class, id);
    }

    public List<VbUser> read() {
        log.debug("Start function of read in UserService class - List<VbUser>");
        List<VbUser> users = userDao.read();
        log.debug("End function of read in UserService class - List<VbUser>");
        return users;
    }

    public VbUser update(VbUser teUser) {
        log.debug("Start function of update in UserService class - VbUser");
        log.debug("End function of update in UserService class - VbUser");
        return (VbUser) userDao.update(teUser);
    }

    public VbUser delete(Integer id) {
        log.debug("Start function of delete in UserService class - VbUser");
        VbUser teUser = read(id);
        teUser.setStatus("Deleted");
        log.debug("End function of delete in UserService class - VbUser");
        return update(teUser);
    }

    public VbUser delete(VbUser teUser) {
        log.debug("Start function of delete in UserService class - VbUser");
        log.debug("End function of delete in UserService class - VbUser");
        return (VbUser) userDao.delete(teUser);
    }

    public VbUser findByUsername(String username) {
        log.debug("Start function of findByUsername in UserService class - VbUser");
        List<VbUser> vbUsers = userDao.findByUserName(username);
        if (!vbUsers.isEmpty()) {
            log.debug("End function of findByUsername in UserService class - VbUser");
            return vbUsers.get(0);
        }
        log.debug("End function of findByUsername in UserService class - VbUser");
        return null;
    }

    public SecurityAuthBean getPermissions(LoginUserBean userBean) {
        log.debug("Start function of getPermissions in UserService class - VbUser");
        log.debug("End function of getPermissions in UserService class - VbUser");
        return VbUtils.getAuthData(userBean.getUsername(), userBean.getPassword());
    }

    public SecurityAuthBean getPermissions(String accessToken, String userGuid) {
        log.debug("Start function of getPermissions in UserService class - VbUser");
        log.debug("End function of getPermissions in UserService class - VbUser");
        return VbUtils.getAuthDataByGuid(accessToken, userGuid);
    }

    public LoginUserBean authenicate(LoginUserBean userBean) {
        log.debug("Start function of authenicate in UserService class - LoginUserBean");
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
        log.debug("End function of authenicate in UserService class - LoginUserBean");
        return loginUserBean;
    }

    private LoginUserBean toLoginUserBean(VbUser teUser) {
        log.debug("Start function of toLoginUserBean in UserService class");
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(teUser.getUserName());
        userBean.setFailLoginCount(teUser.getFailedLoginCount());
        userBean.setIsAdmin(teUser.getIsAdmin() != null && teUser.getIsAdmin() == true ? "admin" : "");
        log.debug("End function of toLoginUserBean in UserService class");
        return userBean;
    }

    public List<Dealer> getAllowedDealerByMapId(String delaerRefId) {
        log.debug("Start function of getAllowedDealerByMapId in UserService class");
        log.debug("End function of getAllowedDealerByMapId in UserService class");
        return dealerDao.getAllowedDealerByMapId(delaerRefId);
    }

    public List<Dealer> getAllowedDealerByGroupId(String groupId) {
        log.debug("Start function of getAllowedDealerByGroupId in UserService class");
        log.debug("End function of getAllowedDealerByGroupId in UserService class");
        return dealerDao.getAllowedDealerByGroupId(groupId);
    }

    public List<Dealer> getAllowedDealerByGroupName(String groupName) {
        log.debug("Start function of getAllowedDealerByGroupName in UserService class");
        log.debug("End function of getAllowedDealerByGroupName in UserService class");
        return dealerDao.getAllowedDealerByGroupName(groupName);
    }

    public List<Dealer> getAllowedDealerByOemRegionId(String oemRegionId) {
        log.debug("Start function of getAllowedDealerByOemRegionId in UserService class");
        log.debug("End function of getAllowedDealerByOemRegionId in UserService class");
        return dealerDao.getAllowedDealerByOemRegionId(oemRegionId);
    }

    public List<Dealer> getSampleDealers() {
        log.debug("Start function of getSampleDealers in UserService class");
        log.debug("End function of getSampleDealers in UserService class");
        return dealerDao.getSampleDealers();
    }

    public VbUser createNewUser(SecurityAuthBean authData) {
        log.debug("Start function of createNewUser in UserService class");
        String userId = authData.getUserId();
        String userName = authData.getUserName();
        List<VbUser> users = userDao.findByUserName(userName);
        VbUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        if (user == null) {
            log.debug("Creating user " + userName);
            user = userDao.createNewUser(userId, userName, authData.getFullName());
            userDao.initUser(user);
        }
        log.debug("End function of createNewUser in UserService class");
        return user;
    }
}
