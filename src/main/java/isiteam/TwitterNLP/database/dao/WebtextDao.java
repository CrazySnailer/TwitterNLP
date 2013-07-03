
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file WebtextDao.java
* 
* @date 2011-6-28-上午9:08:06
* @Copyright 2011 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.Webtext;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class WebtextDao
 * 
 * @date 2011-6-28-上午9:08:06
 * @Copyright 2011 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface WebtextDao {

	long getWebTextCount();

	List<Webtext> getWebtextList(int cursor, int batchSize);

}
