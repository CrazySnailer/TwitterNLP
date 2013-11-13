
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.dao
* @file UserInfo930.java
* 
* @date 2013-10-22-上午8:13:51
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.dao;

import isiteam.TwitterNLP.database.bean.UserInfo930;

import java.util.List;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInfo930
 * 
 * @date 2013-10-22-上午8:13:51
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public interface UserInfo930Dao {

	long getUserInfoCount();

	List<UserInfo930> getDistinctUserInfoList(int cursor, int batchSize);

}
