
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file UserInfo930DaoImpl.java
* 
* @date 2013-10-22-上午8:17:05
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

import isiteam.TwitterNLP.database.bean.UserInfo930;
import isiteam.TwitterNLP.database.dao.UserInfo930Dao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfo930DaoImpl
 * 
 * @date 2013-10-22-上午8:17:05
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class UserInfo930DaoImpl implements UserInfo930Dao {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfo930DaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-10-22-上午8:17:05
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public long getUserInfoCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count(distinct userId) from UserInfo930";
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
	public List<UserInfo930> getDistinctUserInfoList(final int cursor,final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from UserInfo930 group by userId";
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

}
