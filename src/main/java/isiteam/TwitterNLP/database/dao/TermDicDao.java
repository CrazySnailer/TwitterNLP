
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file TermDicDao.java
* 
* @date 2013-6-25-上午11:18:53
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.TermDic;

import java.util.List;
import java.util.Map;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TermDicDao
 * 
 * @date 2013-6-25-上午11:18:53
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface TermDicDao {

	void updateTerm(String term,String termTag);

	void batchSaveTermList(List<TermDic> termDicList, int batchSize);

	List<TermDic> getAllTermList();

}
