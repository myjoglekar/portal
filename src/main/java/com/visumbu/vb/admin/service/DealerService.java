/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.model.Dealer;
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
}
