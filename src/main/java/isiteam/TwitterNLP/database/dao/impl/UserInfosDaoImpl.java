
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao.impl
* @file UserInfosDaoImpl.java
* 
* @date 2013-11-12-下午4:34:17
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao.impl;

import isiteam.TwitterNLP.database.dao.UserInfosDao;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfosDaoImpl
 * 
 * @date 2013-11-12-下午4:34:17
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Repository
public class UserInfosDaoImpl implements UserInfosDao{
	private static final Logger log = LoggerFactory
			.getLogger(UserInfosDaoImpl.class);
	
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
	 * @date 2013-11-12-下午4:34:17
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
