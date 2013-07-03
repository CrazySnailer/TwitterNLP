
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
import java.util.List;

import javax.annotation.Resource;

import isiteam.TwitterNLP.database.dao.NewsDao;
import isiteam.TwitterNLP.database.dao.SegContentDao;
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
	private SegContentDao segContentDao;
	
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
		    
		    List<SegContent> segConList=new ArrayList<SegContent>();
		    
		    for(int i=0;i<num;i++){
		    	cursor=i*batchSize;
		    	List<News> newsList=newsDao.getNewsList(cursor,batchSize);
		    	
		    	segConList.clear();
		    	
		    	for(News news:newsList){
		    		
		    		//news
		    		
		    		SegContent segCon=new SegContent();
		    		segCon.setTittle(news.getTitle());
		    		segCon.setContent(news.getContent());
		    		segCon.setType("民运");
		    		
		    		segCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
		    		
		    		segConList.add(segCon);
		    		
		    	}
		    	
		    	segContentDao.batchSaveSegConList(segConList,batchSize);   	
		    	
		    }
				
			
			
		}// end addNewsCont
	
	public void addTwitterCont(){
		
		List userIdList=tweetInfoDao.getTweetsUserIdList();
		
	    log.info(userIdList.size()+" "+String.valueOf(userIdList));
	    
	    int batchSize=1000;
	    
	    //int num=(int) Math.ceil( (float) count/1000);
	    int cursor=0;
	    
	    List<SegContent> segConList=new ArrayList<SegContent>();
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
    	    	
    	    	SegContent segCon=new SegContent();
    	    	segCon.setUserId(userId);
    	    	segCon.setContent(tweStr.toString());
    	    	  		
        		segCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
        		
        		segConList.add(segCon);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}         
         
			if (i % batchSize == 0) {  
				 segContentDao.batchSaveSegConList(segConList,batchSize);   
				 segConList.clear();
            }//end if  
			
        }//end for	
		
	}// end addTwitterCont
	
	
	public void addWebTextCont(){
			
		long count=webtextDao.getWebTextCount();
		
	    log.info(String.valueOf(count));
	    
	    int batchSize=1000;
	    
	    int num=(int) Math.ceil( (float) count/1000);
	    int cursor=0;
	    
	    List<SegContent> segConList=new ArrayList<SegContent>();
	    
	    for(int i=0;i<num;i++){
	    	cursor=i*batchSize;
	    	List<Webtext> webtextList=webtextDao.getWebtextList(cursor,batchSize);
	    	
	    	segConList.clear();
	    	
	    	for(Webtext text:webtextList){
	    		
	    		//news
	    		
	    		SegContent segCon=new SegContent();
	    		segCon.setTittle(text.getTitle());
	    		segCon.setContent(text.getContent());
	    		if(text.getDescription().contains("民运")){
	    			segCon.setType("民运2");
	    		}else{
	    			segCon.setType(text.getDescription());
	    		}
	    		
	    		
	    		segCon.setPubTime(text.getPubTime());
	    		
	    		segCon.setInsertTime(new Timestamp(System.currentTimeMillis()));
	    		
	    		segConList.add(segCon);
	    		
	    	}
	    	
	    	segContentDao.batchSaveSegConList(segConList,batchSize);   	
	    	
	    }
			
		
		
	}// end addTwitterCont
	
	
	

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
