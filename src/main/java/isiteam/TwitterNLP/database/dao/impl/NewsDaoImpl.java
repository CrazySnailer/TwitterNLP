
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file NewsDaoImpl.java
* 
* @date 2013-6-25-上午11:17:31
* @Copyright 2013 ISI Team of NUDT-版权所有
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

import isiteam.TwitterNLP.database.bean.News;
import isiteam.TwitterNLP.database.dao.NewsDao;



/**
 * @project Web
 * @author Dayong.Shen
 * @class NewsDaoImpl
 * 
 * @date 2013-6-25-上午11:17:31
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class NewsDaoImpl implements NewsDao {
	private static final Logger log = LoggerFactory
			.getLogger(NewsDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public long getNewsCount() {
		// TODO Auto-generated method stub
		
		try{
			final String hql="select count (*) from News";
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
			log.error("getNewsCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<News> getNewsList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
			try{
				final String hql="from News";
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
				log.error("getNewsList ERROR!"+e.getMessage());
				return null;
			}
		}//end getNewsList
}
