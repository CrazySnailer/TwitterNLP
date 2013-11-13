
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.dataPreprocess
* @file DataMerge.java
* 
* @date 2013-10-22-上午8:22:16
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.dataPreprocess;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import isiteam.TwitterNLP.database.bean.Testset;
import isiteam.TwitterNLP.database.bean.TweetInfo;
import isiteam.TwitterNLP.database.bean.UserInfo930;
import isiteam.TwitterNLP.database.bean.UserInfoAll;
import isiteam.TwitterNLP.database.bean.UserInfo;
import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TweetInfoDao;
import isiteam.TwitterNLP.database.dao.UserInfo930Dao;
import isiteam.TwitterNLP.database.dao.UserInfoAllDao;
import isiteam.TwitterNLP.database.dao.UserInfoDao;
import isiteam.TwitterNLP.database.dao.UserInfosDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.CharUtil;
import isiteam.TwitterNLP.util.Constant;

import javax.annotation.Resource;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


/**
 * @project Web
 * @author Dayong.Shen
 * @class DataMerge
 * 
 * @date 2013-10-22-上午8:22:16
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class DataMerge {
	private static final Logger log =  LoggerFactory
			.getLogger(DataMerge.class);
	
	
	
	@Resource
	private UserInfoDao userInfoDao;
	
	@Resource
	private UserInfosDao  userInfosDao;
	
	@Resource
	private UserInfo930Dao userInfo930Dao;
	
	@Resource
	private UserInfoAllDao userInfoAllDao;
	
	@Resource
	private TweetInfoDao tweetInfoDao;
	
	@Resource
	private TestsetDao testsetDao;
	/**
	 * @function UserInfoMerge
	 * 
	 * 合并UserInfo数据集
	 * 
	 * @author Dayong.Shen
	 * @date 2013-10-22-上午8:26:12
	*/
 	public void UserInfoMerge(){
		
		//获取样本数目 250000
		long user1Num=userInfoDao.getUserInfoCount();
		
		log.info("总数据："+user1Num);
		
		
		int batchSize=1000;		    
		int num=(int) Math.ceil( (float) (user1Num)/batchSize);
		int cursor=0;			
		
		//保存用户的Id列表，根据这个表来合并不同的数据表
		Map<String,Integer> userIdMap=new HashMap<String,Integer>();
		
		List<UserInfoAll> userAllList=new ArrayList<UserInfoAll>();
		List<UserInfo> user1List=new ArrayList<UserInfo>();
		
		
		 for(int i=0;i<num;i++){
			 
		    	cursor=i*batchSize;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	userAllList.clear();
		    	user1List.clear();		    	
		    	
		    	user1List=userInfoDao.getDistinctUserInfoList(cursor,batchSize);
		    	
		    	for(UserInfo e:user1List){
		    		
		    		userIdMap.put(e.getUserId(), 0);
		    		
		    		UserInfoAll userInfoAllTmp=new UserInfoAll();
		    		userInfoAllTmp.setCreatedAt(e.getCreatedAt());
		    		userInfoAllTmp.setCurrentStatuscreatedAt(e.getCurrentStatuscreatedAt());
		    		userInfoAllTmp.setDescription(e.getDescription());
		    		userInfoAllTmp.setFavouritesCount(e.getFavouritesCount());
		    		userInfoAllTmp.setFollowersCount(e.getFollowersCount());
		    		userInfoAllTmp.setFriendsCount(e.getFriendsCount());
		    		userInfoAllTmp.setIsGeoEnabled(e.getIsGeoEnabled());
		    		userInfoAllTmp.setIsProtected(e.getIsProtected());
		    		userInfoAllTmp.setIsVerified(e.getIsVerified());
		    		userInfoAllTmp.setLang(e.getLang());
		    		userInfoAllTmp.setLocation(e.getLocation());
		    		userInfoAllTmp.setScreenName(e.getScreenName());
		    		userInfoAllTmp.setStatusesCount(e.getStatusesCount());
		    		userInfoAllTmp.setTimeZone(e.getTimeZone());
		    		userInfoAllTmp.setUserId(e.getUserId());
		    		
		    		userAllList.add(userInfoAllTmp);
		    	}
		    	
		    	userInfoAllDao.batchSaveUserInfoAllList(userAllList,batchSize);
		    	
		 }//end for
		 
		 
		//获取样本数目
		 long user2Num=userInfo930Dao.getUserInfoCount();
		 log.info("总数据："+user2Num);
		 num=(int) Math.ceil( (float) (user2Num)/batchSize);
		 cursor=0;	
		 List<UserInfo930> user2List=new ArrayList<UserInfo930>();
		
		 
		 
		 for(int i=0;i<num;i++){
			 
		    	cursor=i*batchSize;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	userAllList.clear();
		    	user2List.clear();		    	
		    	
		    	user2List=userInfo930Dao.getDistinctUserInfoList(cursor,batchSize);
		    	
		    	for(UserInfo930 e:user2List){
		    		
		    		//已经保存了该用户的信息
		    		if(userIdMap.containsKey(e.getUserId())){
		    			continue;
		    		}else{
		    			userIdMap.put(e.getUserId(), 0);
			    		
			    		UserInfoAll userInfoAllTmp=new UserInfoAll();
			    		userInfoAllTmp.setCreatedAt(e.getCreatedAt());
			    		userInfoAllTmp.setCurrentStatuscreatedAt(e.getCurrentStatuscreatedAt());
			    		userInfoAllTmp.setDescription(e.getDescription());
			    		userInfoAllTmp.setFavouritesCount(e.getFavouritesCount());
			    		userInfoAllTmp.setFollowersCount(e.getFollowersCount());
			    		userInfoAllTmp.setFriendsCount(e.getFriendsCount());
			    		userInfoAllTmp.setIsGeoEnabled(e.getIsGeoEnabled());
			    		userInfoAllTmp.setIsProtected(e.getIsProtected());
			    		userInfoAllTmp.setIsVerified(e.getIsVerified());
			    		userInfoAllTmp.setLang(e.getLang());
			    		userInfoAllTmp.setLocation(e.getLocation());
			    		userInfoAllTmp.setScreenName(e.getScreenName());
			    		userInfoAllTmp.setStatusesCount(e.getStatusesCount());
			    		userInfoAllTmp.setTimeZone(e.getTimeZone());
			    		userInfoAllTmp.setUserId(e.getUserId());
			    		
			    		userAllList.add(userInfoAllTmp);
		    		}
		    	
		    	}
		    	
		    	userInfoAllDao.batchSaveUserInfoAllList(userAllList,batchSize);
		    	
		 }//end for
		
	}

	
	public void TweetsMerge(){
		
		List<UserInfoAll> userInfoList=new ArrayList<UserInfoAll>();
		List<TweetInfo> tweetInfoList=new ArrayList<TweetInfo>();
		List<Testset> testsetList=new ArrayList<Testset>();
		
		String condition="friendsCount >100";
		
		userInfoList=userInfoAllDao.getUserInfoListByCondition(condition);
		
		for(UserInfoAll e:userInfoList ){
			tweetInfoList.clear();
			testsetList.clear();
			
			tweetInfoList=tweetInfoDao.getTweetsListbyUserId(e.getUserId());
			
			for(TweetInfo e1:tweetInfoList){
				Testset testSet=new Testset();
				
				if(CharUtil.ChinesePercent(e1.getText())>0.6){
					
					testSet.setContent(e1.getText());
					testSet.setPubTime(e1.getCreatedAt());
					testSet.setUserId(e1.getUserId());
					testSet.setInsertTime(new Timestamp(System.currentTimeMillis()));
					
					testsetList.add(testSet);
				}
				
				
			}
			
			testsetDao.batchSaveTestConList(testsetList, 50);
			
		}
		
	}
	
	
	public void DeleteExtraUserInfo(){
		//获取所有用户的条数
		long userNum=userInfoDao.getUserInfoCount();
		
		log.info("总数据："+userNum);
				
		int batchSize=1000;		    
		int num=(int) Math.ceil( (float) (userNum)/batchSize);
		int cursor=0;
		
		//保存用户的Id列表，根据这个表来合并不同的数据表
				Map<String,Integer> userInfoMap=new HashMap<String,Integer>();
				
				List<UserInfo> userList=new ArrayList<UserInfo>();
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//定义格式
			
								
				 for(int i=0;i<num;i++){
					 
				    	cursor=i*batchSize;
				    	
				    	log.info("开始取 "+cursor+" 数据");
				    	
				    	userList.clear();		    	
				    	
				    	userList=userInfoDao.getUserInfoList(cursor,batchSize);
				    	
				    	for(UserInfo e:userList){
				    		
				    		String key=e.getUserId()+"-"+df.format(e.getInsertTime());
				    		//log.info("Key is:"+key);
				    		
				    		if(userInfoMap.containsKey(key)){//包含，则删除
				    			
				    			userInfoDao.delete(e);
				    			
				    		}else{//不包含
				    			userInfoMap.put(key, 1);
				    		}
				    		
				    		
				    	}
				    	
				    	
				 }//end for
		
	}
	
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-10-22-上午8:22:16
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
		log.info("正在创建数据库连接和缓冲池...");
	    AppContext.initAppCtx();
		log.info("数据库连接已连接！缓冲池已建立");
	
		DataMerge dataMerge=(DataMerge) AppContext.appCtx.getBean("dataMerge");
		
		//dataMerge.TweetsMerge();
		
		dataMerge.DeleteExtraUserInfo();
	}

}
