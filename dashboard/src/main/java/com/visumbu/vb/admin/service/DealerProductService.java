/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerProductDao;
import com.visumbu.vb.model.DealerProduct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("dealerProductService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DealerProductService {
    @Autowired
    private DealerProductDao dealerProductDao;
    
    public List<DealerProduct> read(){
        List<DealerProduct> dealerProduct = dealerProductDao.read(DealerProduct.class);        
        return dealerProduct;
    }
}
