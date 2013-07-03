
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file NewsDao.java
* 
* @date 2013-6-25-上午11:17:01
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.News;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class NewsDao
 * 
 * @date 2013-6-25-上午11:17:01
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface NewsDao {

	long getNewsCount();

	List<News> getNewsList(int cursor, int batchSize);

}
