
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file TweetInfoDaoImpl.java
* 
* @date 2013-6-25-上午11:20:02
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


import isiteam.TwitterNLP.database.bean.TweetInfo;
import isiteam.TwitterNLP.database.dao.TweetInfoDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TweetInfoDaoImpl
 * 
 * @date 2013-6-25-上午11:20:02
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class TweetInfoDaoImpl implements TweetInfoDao {
	private static final Logger log = LoggerFactory
			.getLogger(TweetInfoDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public List getTweetsUserIdList() {
		// TODO Auto-generated method stub
		try{
			final String hql="SELECT distinct userId FROM TweetInfo";
			List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);				
					List list=query.list();
					return list;
				}
			});
				
				return list;
		
		}catch(Exception e){
			log.error("getTweetsUserIdList ERROR!"+e.getMessage());
			return null;
		}
	}//getTweetsUserIdList

	@Override
	public List<TweetInfo> getTweetsListbyUserId(final String userId) {
		// TODO Auto-generated method stub
		try{
			final String hql="FROM TweetInfo where userId=:userid";
			List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);	
					query.setString("userid", userId);
					List list=query.list();
					return list;
				}
			});
				
				return list;
		
		}catch(Exception e){
			log.error("getTweetsListbyUserId ERROR!"+e.getMessage());
			return null;
		}
	}//getTweetsListbyUserId

	@Override
	public long getTweetsCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count (*) from TweetInfo";
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
			log.error("getTweetsCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<TweetInfo> getTweetsList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from TweetInfo";
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
			log.error("getTweetsList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public int getUserNum() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
