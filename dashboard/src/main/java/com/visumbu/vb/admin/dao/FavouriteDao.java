/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetTag;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Transactional
@Repository("favDao")
public class FavouriteDao extends BaseDao{
    
    @Autowired
    private ReportDao reportDao;

    public Tag findTagName(String favName) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t where t.favName = :favName");
        query.setParameter("favName", favName);
        List<Tag> tagList = (List<Tag>) query.list();
        if (tagList != null && !tagList.isEmpty()) {
            return tagList.get(0);
        }
        return null;
    }

    public WidgetTag findWidgetTagByUserNTag(Integer tagId, Integer widgetId, VbUser user) {
        String queryStr = "select t from WidgetTag t where t.tagId.id = :tagId and t.widgetId.id = :widgetId and t.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagId", tagId);
        query.setParameter("widgetId", widgetId);
        query.setParameter("user", user);
        List<WidgetTag> widgetTagList = (List<WidgetTag>) query.list();
        if (widgetTagList != null && !widgetTagList.isEmpty()) {
            return widgetTagList.get(0);
        }
        return null;
    }

    public List<TabWidget> findAllWidgetsByTag(VbUser user, Tag tag) {
        String queryStr = "select w.widgetId from WidgetTag w where w.userId = :user and w.tagId = :tag";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tag", tag);
        query.setParameter("user", user);
        return query.list();
    }

    public List getWidgetTagByName(String favName) {
        String queryStr = "select t from WidgetTag t where t.tagId.favName = :favName";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("favName", favName);
        
        List<WidgetTag> widgetTag = query.list();
        for (Iterator<WidgetTag> iterator = widgetTag.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next().getWidgetId();
            tabWidget.setColumns(reportDao.getColumns(tabWidget));
        }
        return widgetTag;    
    }
    
}
