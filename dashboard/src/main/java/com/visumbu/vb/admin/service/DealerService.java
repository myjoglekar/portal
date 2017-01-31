/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.common.base.CaseFormat;
import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.bean.DealerAccountBean;
import com.visumbu.vb.model.Dealer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("dealerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DealerService {

    @Autowired
    private DealerDao dealerDao;

    public Dealer read(Integer id) {
        return (Dealer) dealerDao.read(Dealer.class, id);
    }

    public List<Dealer> read() {
        List<Dealer> dealer = dealerDao.read(Dealer.class);
        return dealer;
    }

    public Map<String, String> getDealerAccountDetails(String dealerId) {
        System.out.println(dealerId);
        List<DealerAccountBean> dealerAccountDetails = dealerDao.getDealerAccountDetails(dealerId);
        Map<String, String> accountMap = new HashMap<>();
        for (Iterator<DealerAccountBean> iterator = dealerAccountDetails.iterator(); iterator.hasNext();) {
            DealerAccountBean accountBean = iterator.next();
            String serviceName = accountBean.getServiceName();
            if(serviceName.equalsIgnoreCase("none")) {
                if(accountBean.getProductName().toLowerCase().indexOf("seo") >= 0){
                    serviceName = "seo";
                }
                if(accountBean.getProductName().indexOf("Reputation Management") > 0){
                    serviceName = "reputation";
                }
                if(accountBean.getProductName().indexOf("Social Impact") > 0){
                    serviceName = "socialimpact";
                }
            }
            String accountName = serviceName + " " + accountBean.getSourceName() + " Account Id" ;
            String profileName = serviceName + " " + accountBean.getSourceName() + " Profile Id" ;
            accountMap.put("dealerId", accountBean.getDealerId());
            accountMap.put("dealerMapId", accountBean.getDealerMapId());
            accountMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, accountName.toLowerCase().replace(" ", "_")), accountBean.getAccountId());
            accountMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, profileName.toLowerCase().replace(" ", "_")), accountBean.getProfileId());
        }
        return accountMap;
    }

}
