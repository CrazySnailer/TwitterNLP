
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file TweetInfoDao.java
* 
* @date 2013-6-25-上午11:19:42
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.TweetInfo;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TweetInfoDao
 * 
 * @date 2013-6-25-上午11:19:42
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface TweetInfoDao {

	List getTweetsUserIdList();

	List<TweetInfo> getTweetsListbyUserId(String userId);

	long getTweetsCount();

	List<TweetInfo> getTweetsList(int cursor, int batchSize);

	int getUserNum();

}
