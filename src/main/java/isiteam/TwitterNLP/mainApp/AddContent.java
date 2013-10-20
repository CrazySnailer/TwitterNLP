
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.mainApp
* @file AddContent.java
* 
* @date 2013-6-25-下午5:10:53
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.mainApp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import isiteam.TwitterNLP.database.dao.NewsDao;
import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
import isiteam.TwitterNLP.database.dao.TweetInfoDao;
import isiteam.TwitterNLP.database.dao.WebtextDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.Constant;
import isiteam.TwitterNLP.database.bean.*;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


/**
 * @project Web
 * @author Dayong.Shen
 * @class AddContent
 * 
 * 属于数据预处理阶段，将原始数据表news中的tittle、content、type存放至表SegContent中去
 *                将TwtterInfo表中的数据，合并单个用户的所有博文存放至表SegContent中去
 * 
 * @date 2013-6-25-下午5:10:53
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class AddContent {
	private static final Logger log =  LoggerFactory
			.getLogger(BuildTermDictionary.class);
	
	@Resource
	private NewsDao newsDao;
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	@Resource
	private TweetInfoDao tweetInfoDao;
	
	@Resource
	private WebtextDao webtextDao;
	
	
 	public void addNewsCont(){
		
			long count=newsDao.getNewsCount();
			
		    log.info(String.valueOf(count));
		    
		    int batchSize=1000;
		    
		    int num=(int) Math.ceil( (float) count/1000);
		    int cursor=0;
		    
		    List<Trainset> trainsetList=new ArrayList<Trainset>();
		    
		    for(int i=0;i<num;i++){
		    	cursor=i*batchSize;
		    	List<News> newsList=newsDao.getNewsList(cursor,batchSize);
		    	
		    	trainsetList.clear();
		    	
		    	for(News news:newsList){
		    		
		    		//news
		    		
		    		Trainset trainCon=new Trainset();
		    		trainCon.setTittle(news.getTitle());
		    		trainCon.setContent(news.getContent());
		    		trainCon.setType(news.getGenus());
		    		
		    		trainCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
		    		
		    		trainsetList.add(trainCon);
		    	}
		    	
		    	trainsetDao.batchSaveTrainConList(trainsetList,batchSize);   	
		    	
		    }
			
		}// end addNewsCont
	
	
	/**
	 * @function addTwitterCont
	 * 
	 * 添加Twittter数据，是以用户为单位的添加博文数据，即一个一个用户的添加
	 * 
	 * @author Dayong.Shen
	 * @date 2013-10-15-上午7:52:12
	*/
	public void addTwitterCont(){
		
		List userIdList=tweetInfoDao.getTweetsUserIdList();
		
	    log.info(userIdList.size()+" "+String.valueOf(userIdList));
	    
	    int batchSize=1000;
	    
	    //int num=(int) Math.ceil( (float) count/1000);
	    int cursor=0;
	    
	    List<Testset> testConList=new ArrayList<Testset>();
	    List<TweetInfo> Tweetslist =new ArrayList<TweetInfo>();
	    
	    StringBuilder tweStr=new StringBuilder();
	    
	    
	    for (int i = 0; i < userIdList.size(); i++) {  
            try {
            	
            	//取出用户的所有博文
    	    	
    	    	String userId=String.valueOf(userIdList.get(i));
    	    	
    	    	Tweetslist.clear();//清除
    	    	
    	    	Tweetslist =tweetInfoDao.getTweetsListbyUserId(userId);
    	    	
    	    	tweStr.delete(0, tweStr.length());//清除
    	    	
    	    	for(TweetInfo twe:Tweetslist){
    	    		tweStr.append(twe.getText());
    	    	}
    	    	
    	    	//增加SegContent实例
    	    	
    	    	Testset testCon=new Testset();
    	    	testCon.setUserId(userId);
    	    	testCon.setContent(tweStr.toString());
    	    	  		
    	    	testCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
        		
    	    	testConList.add(testCon);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}         
         
			if (i % batchSize == 0) {  
				testsetDao.batchSaveTestConList(testConList,batchSize);   
				testConList.clear();
            }//end if  
			
        }//end for	
		
	}// end addTwitterCont
	

/**
 * @function addTwitterContByTweets
 * 
 * 添加Twittter数据，由博文来查找用户
 * 
 * @author Dayong.Shen
 * @date 2013-10-15-上午7:54:38
*/
public void addTwitterContByTweets(){
	
	//查找博文中的用户LIST
	List<String> userIdList=tweetInfoDao.getTweetsUserIdList();
	
    log.info("userIdList Count:"+userIdList.size());
    
	 
	    List<Testset> testConList=new ArrayList<Testset>();
	    List<TweetInfo> Tweetslist =new ArrayList<TweetInfo>();
	    
	    StringBuilder tweStr=new StringBuilder();
	    
	   int i=0;
	   int batchSize=50;
	    
	    for (String eUserId:userIdList) {  
            try {
            	
    	    	
    	    	Tweetslist.clear();//清除
    	    	
    	    	Tweetslist =tweetInfoDao.getTweetsListbyUserId(eUserId);
    	    	
    	    	tweStr.delete(0, tweStr.length());//清除
    	    	
    	    	for(TweetInfo twe:Tweetslist){
    	    		tweStr.append(twe.getText());
    	    	}
    	    	
    	    	//增加SegContent实例
    	    	
    	    	Testset testCon=new Testset();
    	    	testCon.setUserId(eUserId);
    	    	testCon.setContent(tweStr.toString());
    	    	testCon.setType(99);  		
    	    	testCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
        		
    	    	testConList.add(testCon);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}         
         
			if (i % batchSize == 0) {
				
				 testsetDao.batchSaveTestConList(testConList,batchSize);   
				 testConList.clear();
            }//end if  
			
			i++;
        }//end for	
	    
	    //保存剩余的
	    testsetDao.batchSaveTestConList(testConList,batchSize);   
	    testConList.clear();
		
	}// end addTwitterContByTweets
	
	
	public void addWebTextCont(){
			
		long count=webtextDao.getWebTextCount();
		
	    log.info(String.valueOf(count));
	    
	    int batchSize=1000;
	    
	    int num=(int) Math.ceil( (float) count/1000);
	    int cursor=0;
	    
	    List<Trainset> trainsetList=new ArrayList<Trainset>();
	                                         //"民运","宗教邪教","政治外交","维权异议","突发事件","两岸关系"
		String[][] typeArray=new String[][] {{"民运","宗教邪教","政外","维权异议","突发","两岸关系"}
				                             ,{"1","2","3","4","5","6"}};
		
		
		Map<String,Integer> typeMap= new  HashMap<String,Integer>();
		
		//建立Map
		for(int i=0;i<typeArray[0].length;i++){
			typeMap.put(typeArray[0][i],Integer.valueOf(typeArray[1][i]));
		}
	

	    
	    for(int i=0;i<num;i++){
	    	cursor=i*batchSize;
	    	List<Webtext> webtextList=webtextDao.getWebtextList(cursor,batchSize);
	    	
	    	trainsetList.clear();
	    	
	    	for(Webtext text:webtextList){
	    		
	    		if(typeMap.containsKey(text.getDescription())){
	    			Trainset trainsetCon=new Trainset();
		    		trainsetCon.setTittle(text.getTitle());
		    		trainsetCon.setContent(text.getContent());
		    		trainsetCon.setType(typeMap.get(text.getDescription()));
		    		trainsetCon.setPubTime(text.getPubTime());
		    		
		    		trainsetCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
		    		
		    		trainsetList.add(trainsetCon);
	    		}else{
	    			continue;
	    		}
	    		
	    		
	    		
	    		
	    	}
	    	
	    	trainsetDao.batchSaveTrainConList(trainsetList,batchSize);   	
	    	
	    }
			
		
		
	}// end addWebTextCont
	
	
	

	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-6-25-下午5:10:53
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  	PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
			log.info("正在创建数据库连接和缓冲池...");
		    AppContext.initAppCtx();
			log.info("数据库连接已连接！缓冲池已建立");
		
			AddContent addContent=(AddContent) AppContext.appCtx.getBean("addContent");
			
			addContent.addWebTextCont();

	}

}
