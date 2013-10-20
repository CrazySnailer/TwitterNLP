
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file TrainsetDaoImpl.java
* 
* @date 2013-10-15-下午2:52:49
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

import isiteam.TwitterNLP.database.bean.Trainset;
import isiteam.TwitterNLP.database.dao.TrainsetDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TrainsetDaoImpl
 * 
 * @date 2013-10-15-下午2:52:49
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class TrainsetDaoImpl implements TrainsetDao {
	private static final Logger log = LoggerFactory
			.getLogger(TrainsetDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public long getTrainCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count(*) from Trainset";
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
			log.error("getTrainCount ERROR!"+e.getMessage());
			return 0;
		}
	}


	@Override
	public List<Trainset> getTrainConList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from Trainset";
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
			log.error("getTrainConList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public void batchUpdateTrainConList(final List<Trainset> trainsetList,
			final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < trainsetList.size(); i++) {  
		                    try {
								session.update(trainsetList.get(i));
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
	}

	@Override
	public List getTrainList() {
		// TODO Auto-generated method stub
		try{
			final String hql="select type, termVector from Trainset";
			
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
	public long getTrainCountByType(int type) {
		// TODO Auto-generated method stub
		try{
			final String hql="select count (*) from Trainset where type="+type;
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
			log.error("getTrainCountByType ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Trainset> getTrainConList(final int cursor, final int batchSize, int type) {
		// TODO Auto-generated method stub
		try{
			final String hql="from Trainset where type="+type;
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
			log.error("getTrainConList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public void batchSaveTrainConList(final List<Trainset> trainsetList, final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < trainsetList.size(); i++) {  
		                    try {
								session.save(trainsetList.get(i));
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
}
