
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.mainApp
* @file TextLableSVM.java
* 
* @date 2013-7-4-上午11:02:31
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.mainApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
import isiteam.TwitterNLP.libsvm.svm_predict;
import isiteam.TwitterNLP.libsvm.svm_train;
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
 * @class TextLableSVM
 * 
 * @date 2013-7-4-上午11:02:31
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class TextLableSVM {
	private static final Logger log =  LoggerFactory
			.getLogger(TextLableSVM.class);
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	private String trainFileName="train.txt";
	
	private String testFileName="test.txt";
	
	
	public void buildTrainData(){
		
		
		StringBuilder trainStrBd= new StringBuilder();
		
		String[][] typeArray=new String[][] {{"民运2","民运","日本","中东北非","两岸关系","京沪闽","其他国家","军事","分裂","台湾","大陆其他","宗教邪教","政外","朝鲜半岛","涉华舆情","港澳","突发","维权异议","美日欧","藏疆","经贸","其他","v体育","v文化娱乐","v社会生活","v科教","v财经"}
                                            ,{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"}};

		Map<String,Integer> typeMap= new  HashMap<String,Integer>();
		
		//建立Map
		for(int i=0;i<typeArray[0].length;i++){
			typeMap.put(typeArray[0][i],Integer.valueOf(typeArray[1][i]));
		}
				
		List trainList=trainsetDao.getTrainList();		
		
		int i=0,batchSize=1000;
		for (Iterator it = trainList.iterator(); it.hasNext(); ) {
			
			i++;
			
			Object[] columns = (Object[]) it.next();
			
		//	log.info(columns[0]+" "+columns[1]);
			
			trainStrBd.append(typeMap.get(columns[0])+" "+columns[1]+System.getProperty("line.separator"));
			
			if (i % batchSize == 0) {
				FileUtil.appendText(trainFileName, trainStrBd.toString());
				trainStrBd.delete(0, trainStrBd.length());
			}//end if			
		}//end for trainList
		    //存储最后剩余的
			FileUtil.appendText(trainFileName, trainStrBd.toString());
			trainStrBd.delete(0, trainStrBd.length());
			
	}//end buildTrainData
	
	
	public void buildTestData(){
		
		long count=testsetDao.getTestCount();
			
		log.info("总数: "+String.valueOf(count));
		
		int batchSize=1000;
		int start=0;
		int cursor=0;
	     
		int num=(int) Math.ceil( (float) (count-start)/batchSize);
		
		StringBuilder testStrBd= new StringBuilder();
				
		List testList=new ArrayList();	
		
		for(int j=0;j<num;j++){
			cursor=j*batchSize+start;
			
			log.info("开始取 "+cursor+" 数据");
			testList.clear();
			testList=testsetDao.getTestList(cursor,batchSize);	
			
			int i=0;
			for (Object ob: testList ) {
				
				i++;
				
//				log.info("ob Content "+(String) ob);
				
				testStrBd.append(99+" "+(String) ob+System.getProperty("line.separator"));
				
				if (i % batchSize == 0) {
					FileUtil.appendText(testFileName, testStrBd.toString());
					testStrBd.delete(0, testStrBd.length());
				}//end if			
			}//end for trainList
			
		    //存储最后剩余的
			FileUtil.appendText(testFileName, testStrBd.toString());
			testStrBd.delete(0, testStrBd.length());
			
		}// end for
		
		
			
	}//end buildTrainData
	
	
	public void SVMStart(){
		
	    buildTrainData();
		
		//buildTestData();
				
		String[] trainArgs = {"data/"+trainFileName,"data/train.model"};//directory of training file
	
		String[] testArgs = {"data/"+testFileName, "data/train.model", "data/result.txt"};//directory of test file, model file, result file

		try {
			svm_train.main(trainArgs);
			svm_predict.main(testArgs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
	}
	
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-7-4-上午11:02:32
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
		log.info("正在创建数据库连接和缓冲池...");
	    AppContext.initAppCtx();
		log.info("数据库连接已连接！缓冲池已建立");
	
		TextLableSVM textLableSVM=(TextLableSVM) AppContext.appCtx.getBean("textLableSVM");
		
		textLableSVM.SVMStart();
	}

}
