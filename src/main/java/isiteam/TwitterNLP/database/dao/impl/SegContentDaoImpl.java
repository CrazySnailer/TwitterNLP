
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file SegContentDaoImpl.java
* 
* @date 2013-6-25-上午11:18:37
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

import isiteam.TwitterNLP.database.bean.SegContent;
import isiteam.TwitterNLP.database.dao.SegContentDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class SegContentDaoImpl
 * 
 * @date 2013-6-25-上午11:18:37
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class SegContentDaoImpl implements SegContentDao {
	private static final Logger log = LoggerFactory
			.getLogger(SegContentDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public void batchSaveSegConList(final List<SegContent> segConList, final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < segConList.size(); i++) {  
		                    try {
								session.save(segConList.get(i));
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
	}//end batchSaveSegConList

	@Override
	public long getContentCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count (*) from SegContent";
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
			log.error("getContentCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<SegContent> getSegContentList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from SegContent";
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
			log.error("getSegContentList ERROR!"+e.getMessage());
			return null;
		}
	}//end getSegContentList

	@Override
	public void batchUpdateSegConList(final List<SegContent> segConList, final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < segConList.size(); i++) {  
		                    try {
								session.update(segConList.get(i));
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
		                    }  
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
		
	}//end batchUpdateSegConList

	@Override
	public List getTrainList() {
		// TODO Auto-generated method stub
		try{
			final String hql="select type, termVector from SegContent where type is not NULL";
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
			log.error("getTrainList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public List getTestList() {
		// TODO Auto-generated method stub
		try{
			final String hql="select termVector from SegContent where type is NULL";
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
			log.error("getTestList ERROR!"+e.getMessage());
			return null;
		}
	}
}
