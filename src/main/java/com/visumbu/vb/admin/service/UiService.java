/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("uiService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UiService {

    @Autowired
    private UiDao uiDao;

    public List<Product> getProduct() {
        List<Product> product = uiDao.read(Product.class);
        return product;
    }

    public List<Dashboard> getDashboards(VbUser user) {
        return uiDao.getDashboards(user);
    }

    public DashboardTabs createDashboardTabs(DashboardTabs dashboardTabs) {
        return (DashboardTabs) uiDao.create(dashboardTabs);
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        return uiDao.getDashboardTabs(dbId);
    }

    public TabWidget createTabWidget(TabWidget tabWidget) {
        return (TabWidget) uiDao.create(tabWidget);
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        return uiDao.getTabWidget(tabId);
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return uiDao.getDashboardById(dashboardId);
    }
}
