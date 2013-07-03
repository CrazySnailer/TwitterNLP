
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file WebtextDaoImpl.java
* 
* @date 2011-6-28-上午9:09:11
* @Copyright 2011 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import isiteam.TwitterNLP.database.bean.Webtext;
import isiteam.TwitterNLP.database.dao.WebtextDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class WebtextDaoImpl
 * 
 * @date 2011-6-28-上午9:09:11
 * @Copyright 2011 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class WebtextDaoImpl implements WebtextDao {
	private static final Logger log = LoggerFactory
			.getLogger(WebtextDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public long getWebTextCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count (*) from Webtext";
			List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);				
					List list=query.list();
					return list;
				}
			});
				
				return (Long) list.get(0);
		
		
		}catch(Exception e){
			log.error("getWebTextCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Webtext> getWebtextList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from Webtext";
			List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);
					query.setFirstResult(cursor);
					query.setMaxResults(batchSize);
					List list=query.list();
					return list;
				}
			});
				
				return list;
		
		}catch(Exception e){
			log.error("getWebtextList ERROR!"+e.getMessage());
			return null;
		}
	}

}
