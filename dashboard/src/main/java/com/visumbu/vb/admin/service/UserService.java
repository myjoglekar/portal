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
        log.debug("Calling create function with return type VbUser with parameter teUser " + teUser);
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
        log.debug("Calling read function with return type VbUser");
        return (VbUser) userDao.read(VbUser.class, id);
    }

    public List<VbUser> read() {
        log.debug("Calling read function with return type List contains VbUser Objects");
        List<VbUser> users = userDao.read();
        return users;
    }

    public VbUser update(VbUser teUser) {
        log.debug("Calling update function with return type VbUser");
        return (VbUser) userDao.update(teUser);
    }

    public VbUser delete(Integer id) {
        log.debug("Calling delete function with return type VbUser with parameter id " + id);
        VbUser teUser = read(id);
        teUser.setStatus("Deleted");
        return update(teUser);
    }

    public VbUser delete(VbUser teUser) {
        log.debug("Calling delete function with return type VbUser with parameter teUser " + teUser);
        return (VbUser) userDao.delete(teUser);
    }

    public VbUser findByUsername(String username) {
        log.debug("Calling findByUsername function with return type VbUser with parameter username " + username);
        List<VbUser> vbUsers = userDao.findByUserName(username);
        if (!vbUsers.isEmpty()) {
            return vbUsers.get(0);
        }
        return null;
    }

    public SecurityAuthBean getPermissions(LoginUserBean userBean) {
        log.debug("Calling getPermissions function with return type securityAuthBean with parameter userBean " + userBean);
        return VbUtils.getAuthData(userBean.getUsername(), userBean.getPassword());
    }

    public SecurityAuthBean getPermissions(String accessToken, String userGuid) {
        log.debug("Calling getPermissions function with return type securityAuthBean with parameters accessToken " + accessToken + " and userGuid " + userGuid);
        return VbUtils.getAuthDataByGuid(accessToken, userGuid);
    }

    public LoginUserBean authenicate(LoginUserBean userBean) {
        log.debug("Calling authenicate function with return type authenicate with parameter userBean " + userBean);
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
        log.debug("Calling toLoginUserBean function with return type LoginUserBean with parameter teUser " + teUser);
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(teUser.getUserName());
        userBean.setFailLoginCount(teUser.getFailedLoginCount());
        userBean.setIsAdmin(teUser.getIsAdmin() != null && teUser.getIsAdmin() == true ? "admin" : "");
        return userBean;
    }

    public List<Dealer> getAllowedDealerByMapId(String delearRefId) {
        log.debug("Calling getAllowedDealerByMapId function with return type List contains Dealer objects with parameter teUser " + delearRefId);
        return dealerDao.getAllowedDealerByMapId(delearRefId);
    }

    public List<Dealer> getAllowedDealerByGroupId(String groupId) {
        log.debug("Calling getAllowedDealerByGroupId function with return type List contains Dealer objects with parameter groupId " + groupId);
        return dealerDao.getAllowedDealerByGroupId(groupId);
    }

    public List<Dealer> getAllowedDealerByGroupName(String groupName) {
        log.debug("Calling getAllowedDealerByGroupName function with return type List contains Dealer objects with parameter groupName " + groupName);
        return dealerDao.getAllowedDealerByGroupName(groupName);
    }

    public List<Dealer> getAllowedDealerByOemRegionId(String oemRegionId) {
        log.debug("Calling getAllowedDealerByOemRegionId function with return type List contains Dealer objects with parameter oemRegionId " + oemRegionId);
        return dealerDao.getAllowedDealerByOemRegionId(oemRegionId);
    }

    public List<Dealer> getSampleDealers() {
        log.debug("Calling getSampleDealers function with return type List");
        return dealerDao.getSampleDealers();
    }

    public VbUser createNewUser(SecurityAuthBean authData) {
        log.debug("Calling createNewUser function with return type VbUser with parameter authData " + authData);
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
        return user;
    }
}
