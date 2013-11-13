
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file TestsetDao.java
* 
* @date 2013-10-15-下午2:51:06
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.Testset;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TestsetDao
 * 
 * @date 2013-10-15-下午2:51:06
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface TestsetDao {

	long getTestCount();

	

	void batchSaveTestConList(List<Testset> testConList, int batchSize);

	List getTestList(int cursor, int batchSize);



	List<Testset> getTestConList(int cursor, int batchSize);
	
	List<Testset> getTestConList(int cursor, int batchSize, int type);


	void batchUpdateTestConList(List<Testset> testsetList, int batchSize);



	long getTestCountByType(int type);



	
}
