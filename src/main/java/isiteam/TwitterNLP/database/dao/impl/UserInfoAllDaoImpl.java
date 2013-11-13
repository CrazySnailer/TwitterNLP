
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file UserInfoAllDaoImpl.java
* 
* @date 2013-10-22-上午8:17:27
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

import isiteam.TwitterNLP.database.bean.UserInfoAll;
import isiteam.TwitterNLP.database.dao.UserInfoAllDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfoAllDaoImpl
 * 
 * @date 2013-10-22-上午8:17:27
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class UserInfoAllDaoImpl implements UserInfoAllDao {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoAllDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public void batchSaveUserInfoAllList(final List<UserInfoAll> userAllList,
			final int batchSize) {
		// TODO Auto-generated method stub
		
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < userAllList.size(); i++) {  
		                    try {
								session.save(userAllList.get(i));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								//e1.printStackTrace();
							}         
		                 
							if (i % batchSize == 0) {  
		                        try {
									session.flush();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}  
		                        session.clear();  
		                    }//end if  
		                }			       
				       try {
						session.flush();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
	                   session.clear();
				       return null; 
				}
			});
		
	}

	@Override
	public List<UserInfoAll> getUserInfoListByCondition(String condition) {
		// TODO Auto-generated method stub
		try{
			final String hql="from UserInfoAll where "+condition;
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
			log.error("getUserInfoListByCondition ERROR!"+e.getMessage());
			return null;
		}
	}
}
