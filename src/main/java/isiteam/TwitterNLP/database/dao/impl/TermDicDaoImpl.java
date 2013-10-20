
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file TermDicDaoImpl.java
* 
* @date 2013-6-25-上午11:19:16
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import isiteam.TwitterNLP.database.bean.TermDic;
import isiteam.TwitterNLP.database.dao.TermDicDao;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TermDicDaoImpl
 * 
 * @date 2013-6-25-上午11:19:16
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class TermDicDaoImpl implements TermDicDao {
	private static final Logger log = LoggerFactory
			.getLogger(TermDicDaoImpl.class);
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public void updateTerm(final String term, final String termTag) {
		// TODO Auto-generated method stub
		
		try{
			final String hql="from TermDic where term=:myterm";
			final List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);	
					query.setString("myterm", term);
					List list=query.list();
					return list;
				}
			});
			
			if(list.size()==0){//数据库中没有
				TermDic tempTerm=new TermDic();
				
				tempTerm.setTerm(term);
				tempTerm.setTermTag(termTag);
				tempTerm.setInsertTime(new Timestamp(System.currentTimeMillis()));
				tempTerm.setDocCount(1);
				
				this.getHibernateTemplate().save(tempTerm);				
			}else{//更新
				
				final String UpdateHql="update TermDic set docCount=:docNum where term=:myterm";
				
				this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
		        	@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
		        		
		        		  Query query = session.createQuery(UpdateHql);
		        		  query.setInteger("docNum", ((TermDic) list.get(0)).getDocCount()+1);		        	
		        		  query.setString("myterm", term);
		        			  
		        		  return query.executeUpdate();
	
					}
				});
				
			}
				
				
		
		
		}catch(Exception e){
			log.error("updateTerm ERROR!"+e.getMessage());
		
		}
		
		
	}


	/**
	* 删除表数据
	* @param tableName 表名
	*/		     
	@SuppressWarnings("unchecked")
	public void truncate(final String tableName) {
		    	this.getHibernateTemplate().execute(new HibernateCallback() {
		            public Object doInHibernate(Session session)
		                    throws HibernateException, SQLException {
		                session.createSQLQuery("truncate table " + tableName).executeUpdate();
	                return new ArrayList();
		            }
		        });
		 
		    }
	
	
	@Override
	public void batchSaveTermList(final  List<TermDic> termDicList,final int batchSize) {
		// TODO Auto-generated method stub
		
		//清空termDic数据表
		 truncate("termDic");
		 
		 this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
	        	@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
				       for (int i = 0; i < termDicList.size(); i++) {  
		                    try {
								session.save(termDicList.get(i));
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
	}//end batchSaveTermMapList

	@Override
	public List<TermDic> getAllTermList() {
		// TODO Auto-generated method stub
		try{
			final String hql="from TermDic";
			final List list=this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query=session.createQuery(hql);	
					List list=query.list();
					return list;
				}
			});
			
		return list;
		
		}catch(Exception e){
			log.error("getAllTermList ERROR!"+e.getMessage());
			return null;
		}
		
	}

}
