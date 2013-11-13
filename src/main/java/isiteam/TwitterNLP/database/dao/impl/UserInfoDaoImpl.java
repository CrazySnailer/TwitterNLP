
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file UserInfoDaoImpl.java
* 
* @date 2013-10-22-上午8:17:50
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

import isiteam.TwitterNLP.database.bean.UserInfo;
import isiteam.TwitterNLP.database.dao.UserInfoDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfoDaoImpl
 * 
 * @date 2013-10-22-上午8:17:50
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class UserInfoDaoImpl implements UserInfoDao {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public long getUserInfoCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count(userId) from UserInfo";
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
			log.error("getUserInfoCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<UserInfo> getDistinctUserInfoList(final int cursor,final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from UserInfo group by userId";
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
			log.error("getDistinctUserInfoList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public List<UserInfo> getUserInfoList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from UserInfo";
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
			log.error("getUserInfoList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public void delete(UserInfo e) {
		// TODO Auto-generated method stub
		try{
				this.getHibernateTemplate().delete(e);
		}catch(Exception e1){
			log.error("delete ERROR!"+e1.getMessage());
			
		}
	}
}
