
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file UserInfoAllDao.java
* 
* @date 2013-10-22-上午8:15:28
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.UserInfoAll;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfoAllDao
 * 
 * @date 2013-10-22-上午8:15:28
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface UserInfoAllDao {

	void batchSaveUserInfoAllList(List<UserInfoAll> userAllList, int batchSize);

	List<UserInfoAll> getUserInfoListByCondition(String condition);

}
