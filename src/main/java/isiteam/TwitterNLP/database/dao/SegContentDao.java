
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file SegContentDao.java
* 
* @date 2013-6-25-上午11:18:17
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.SegContent;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class SegContentDao
 * 
 * @date 2013-6-25-上午11:18:17
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface SegContentDao {

	void batchSaveSegConList(List<SegContent> segConList, int batchSize);

	long getContentCount();

	List<SegContent> getSegContentList(int cursor, int batchSize);

	void batchUpdateSegConList(List<SegContent> segConList, int batchSize);

}
