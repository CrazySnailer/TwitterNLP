
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.RAE
* @file RAEData.java
* 
* @date 2013-11-1-下午9:35:40
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.RAE;

import java.util.ArrayList;
import java.util.List;

import isiteam.TwitterNLP.database.bean.Testset;
import isiteam.TwitterNLP.database.bean.Trainset;
import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
import isiteam.TwitterNLP.liblinear.TextLableLinear;
import isiteam.TwitterNLP.mainApp.ComputeTFIDF;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.Constant;
import isiteam.TwitterNLP.util.FileUtil;

import javax.annotation.Resource;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


/**
 * @project Web
 * @author Dayong.Shen
 * @class RAEData
 * 
 * @date 2013-11-1-下午9:35:40
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class RAEData {
	private static final Logger log =  LoggerFactory
			.getLogger(RAEData.class);
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	private String trainFileName="train.txt";
	
	private String testFileName="test.txt";
	
	
	public void creatRAETrainData(int type){
		//计算每个类别下的特征词
	
			long count=trainsetDao.getTrainCountByType(type);
			
			log.info("Type: "+type+"总数: "+String.valueOf(count));
			
			int batchSize=50;
			int start=0;
			     
			int num=(int) Math.ceil( (float) (count-start)/batchSize);
		    int cursor=0;
		    
		    //存放临时的Trainset List
			List<Trainset> trainsetList=new ArrayList<Trainset>();
			
			for(int i=0;i<num;i++){
				 
		    	cursor=i*batchSize+start;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	trainsetList.clear();
		    	
		    	trainsetList=trainsetDao.getTrainConList(cursor,batchSize,type);
		    	
		    	try{
		    		
		    		for(int j=0;j<trainsetList.size();j++){
			    		
			    		if(trainsetList.get(j).getOptimalPosContent().split(" ").length<=4){//为空，返回
			    			continue;
			    		}
			    		

			 			FileUtil.appendText(String.valueOf(type)+".txt",trainsetList.get(j).getOptimalPosContent() +System.getProperty("line.separator"));
			    		
			    		
		    		}//end for trainsetList
		    		
		    	}catch(Exception e){
		    		log.error("Loop trainset : "+e.getMessage());
		    	}    
		    	
			}
		
	}
	
	public void creatRAETeastData(int type){
		//计算每个类别下的特征词	
			long count=testsetDao.getTestCountByType(type);
			
			log.info("Type: "+type+"总数: "+String.valueOf(count));
			
			int batchSize=50;
			int start=0;
			     
			int num=(int) Math.ceil( (float) (count-start)/batchSize);
		    int cursor=0;
		    
		    //存放临时的Testset List
			List<Testset> testsetList=new ArrayList<Testset>();
			
			for(int i=0;i<num;i++){
				 
		    	cursor=i*batchSize+start;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	testsetList.clear();
		    	
		    	testsetList=testsetDao.getTestConList(cursor, batchSize,type);
		    	
		    	try{
		    		
		    		for(int j=0;j<testsetList.size();j++){
			    		
			    		if(testsetList.get(j).getOptimalPosContent().split(" ").length<=4){//为空，返回
			    			continue;
			    		}
			    		

			 			FileUtil.appendText("test_"+String.valueOf(type)+".txt",testsetList.get(j).getOptimalPosContent() +System.getProperty("line.separator"));
			    		
			    		
		    		}//end for trainsetList
		    		
		    	}catch(Exception e){
		    		log.error("Loop testsetList : "+e.getMessage());
		    	}    
		    	
			}
		    
		    
		
	}
	
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-11-1-下午9:35:40
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
	    log.info("正在创建数据库连接和缓冲池...");
		AppContext.initAppCtx();
		log.info("数据库连接已连接！缓冲池已建立");
		
		RAEData rAEData=(RAEData) AppContext.appCtx.getBean("RAEData");
		
		for(int i=0;i<=5;i++){
			//rAEData.creatRAETrainData(i);
			rAEData.creatRAETeastData(i);
		}

	}

}
