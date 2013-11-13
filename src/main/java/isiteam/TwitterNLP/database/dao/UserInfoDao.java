
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file UserInfo.java
* 
* @date 2013-10-22-上午8:14:09
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.UserInfo;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfo
 * 
 * @date 2013-10-22-上午8:14:09
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface UserInfoDao {

	long getUserInfoCount();

	

	List<UserInfo> getDistinctUserInfoList(int cursor, int batchSize);



	List<UserInfo> getUserInfoList(int cursor, int batchSize);



	void delete(UserInfo e);

}
