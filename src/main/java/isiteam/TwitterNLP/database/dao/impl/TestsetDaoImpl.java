
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file TestsetDaoImpl.java
* 
* @date 2013-10-15-下午2:53:18
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

import isiteam.TwitterNLP.database.bean.Testset;
import isiteam.TwitterNLP.database.dao.TestsetDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TestsetDaoImpl
 * 
 * @date 2013-10-15-下午2:53:18
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class TestsetDaoImpl implements TestsetDao {
	private static final Logger log = LoggerFactory
			.getLogger(TestsetDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public long getTestCount() {
		// TODO Auto-generated method stub
		try{
			final String hql="select count(*) from Testset";
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
			log.error("getTestCount ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List getTestList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="select userId,termVector from Testset";
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
			log.error("getTestList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public void batchSaveTestConList(final List<Testset> testConList, final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < testConList.size(); i++) {  
		                    try {
								session.save(testConList.get(i));
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
	public List<Testset> getTestConList(final int cursor, final int batchSize) {
		// TODO Auto-generated method stub
		try{
			final String hql="from Testset";
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
			log.error("getTestConList ERROR!"+e.getMessage());
			return null;
		}
	}

	@Override
	public void batchUpdateTestConList(final List<Testset> testsetList,final int batchSize) {
		// TODO Auto-generated method stub
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < testsetList.size(); i++) {  
		                    try {
								session.update(testsetList.get(i));
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
	public long getTestCountByType(int type) {
		// TODO Auto-generated method stub
		try{
			final String hql="select count (*) from Testset where type="+type;
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
			log.error("getTestCountByType ERROR!"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Testset> getTestConList(final int cursor, final int batchSize, int type) {
		// TODO Auto-generated method stub
		try{
			final String hql="from Testset where type="+type;
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
			log.error("getTestConList ERROR!"+e.getMessage());
			return null;
		}
	}
	
	

}
