
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file TrainsetDao.java
* 
* @date 2013-10-15-下午2:52:14
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.Trainset;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TrainsetDao
 * 
 * @date 2013-10-15-下午2:52:14
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface TrainsetDao {

	long getTrainCount();
	long getTrainCountByType(int type);
	
	List<Trainset> getTrainConList(int cursor, int batchSize);
	List<Trainset> getTrainConList(int cursor, int batchSize, int type);

	void batchUpdateTrainConList(List<Trainset> trainsetList, int batchSize);
	void batchSaveTrainConList(List<Trainset> trainsetList, int batchSize);
	
	List getTrainList();
}
