/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.FavouriteDao;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetTag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Service("favService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FavouriteService {
    
    
    @Autowired
    private FavouriteDao favouriteDao;
    
    @Autowired
    private UiDao uiDao;

    public Boolean removeFav(Integer widgetId, VbUser user) {
        Tag tag = favouriteDao.findTagName("Favourite");
        WidgetTag widgetTag = favouriteDao.findWidgetTagByUserNTag(tag.getId(), widgetId, user);
        if (widgetTag != null) {
            favouriteDao.delete(widgetTag);
            return true;
        }
        return false;
    }

    public Boolean setFav(Integer widgetId, VbUser user) {
        Tag tag = favouriteDao.findTagName("Favourite");
        if (tag == null) {
            tag = new Tag();
            tag.setFavName("Favourite");
            tag.setDescription("Favourite Widgets");
            tag = (Tag) favouriteDao.create(tag);
        }
        WidgetTag widgetTag = favouriteDao.findWidgetTagByUserNTag(tag.getId(), widgetId, user);
        if (widgetTag != null) {
            return false;
        }
        widgetTag = new WidgetTag();
        widgetTag.setTagId(tag);
        widgetTag.setWidgetId(uiDao.getTabWidgetById(widgetId));
        widgetTag.setUserId(user);
        favouriteDao.create(widgetTag);
        return true;
    }

    public List<TabWidget> getAllFav(VbUser user) {
        Tag tag = favouriteDao.findTagName("Favourite");
        return favouriteDao.findAllWidgetsByTag(user, tag);
    }

    public List getWidgetTagByName(String favName) {
        return favouriteDao.getWidgetTagByName(favName);
    }
    
}
