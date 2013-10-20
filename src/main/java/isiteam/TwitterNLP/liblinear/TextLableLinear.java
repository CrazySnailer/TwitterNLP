
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.liblinear
* @file TextLableLinear.java
* 
* @date 2013-7-9-上午10:30:00
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.liblinear;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.Constant;
import isiteam.TwitterNLP.util.FileUtil;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Predict;
import de.bwaldvogel.liblinear.Train;


/**
 * @project Web
 * @author Dayong.Shen
 * @class TextLableLinear
 * 
 * @date 2013-7-9-上午10:30:00
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class TextLableLinear {
	private static final Logger log =  LoggerFactory
			.getLogger(TextLableLinear.class);
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	private String trainFileName="train.txt";
	
	private String testFileName="test.txt";
	
	
	public void buildTrainData(){
		
		StringBuilder trainStrBd= new StringBuilder();

		List trainList=trainsetDao.getTrainList();
		
		/**
		Problem prob = new Problem();
        prob.bias = -1;//If bias >= 0, we assume that one additional feature is added to the end of each data instance. 
        prob.l = trainList.size();//the number of training data
        prob.n = 1296856;//the number of feature (including the bias feature if bias >= 0).
        prob.x = new FeatureNode[prob.l][];//`x' is an array of pointers, each of which points to a sparse representation (array of feature_node) of one training vector.
        prob.y = new double[prob.l];//`y' is an array containing the target values. (integers in classification, real numbers in regression)
		
         for (int i = 0; i < prob.l; i++) {
	            prob.x[i] = new FeatureNode[] {};
	            prob.y[i] = i;
           }
         */
        
		int i=0,batchSize=1000;
		for (Iterator it = trainList.iterator(); it.hasNext(); ) {
			
			i++;
			
			Object[] columns = (Object[]) it.next();
			
		   //log.info(columns[0]+" "+columns[1]);
			
			
			
			//筛选训练集			
			if(columns[1]!=null&columns[1]!=""){
				if(((String)columns[1]).split(" ").length<10){
					continue;
				}
				trainStrBd.append(columns[0]+" "+columns[1]+System.getProperty("line.separator"));
				
				if (i % batchSize == 0) {
					FileUtil.appendText(trainFileName, trainStrBd.toString());
					trainStrBd.delete(0, trainStrBd.length());
				}//end if	
			}
			
			
					
		}//end for trainList
		    //存储最后剩余的
			FileUtil.appendText(trainFileName, trainStrBd.toString());
			trainStrBd.delete(0, trainStrBd.length());
			
	}//end buildTrainData
	
	public void buildSelectedTrainData(){
		
		StringBuilder trainStrBd= new StringBuilder();
		
		/*
		String[][] typeArray=new String[][] {{"民运2","民运","日本","中东北非","两岸关系","京沪闽","其他国家","军事","分裂","台湾","大陆其他","宗教邪教","政外","朝鲜半岛","涉华舆情","港澳","突发","维权异议","美日欧","藏疆","经贸","台湾","其他","v体育","v文化娱乐","v社会生活","v科教","v财经"}
				                             ,{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"}};
		
		
		Map<String,Integer> typeMap= new  HashMap<String,Integer>();
		
		//建立Map
		for(int i=0;i<typeArray[0].length;i++){
			typeMap.put(typeArray[0][i],Integer.valueOf(typeArray[1][i]));
		}
		
		*/
				
		List trainList=trainsetDao.getTrainList();
		
		/**
		Problem prob = new Problem();
        prob.bias = -1;//If bias >= 0, we assume that one additional feature is added to the end of each data instance. 
        prob.l = trainList.size();//the number of training data
        prob.n = 1296856;//the number of feature (including the bias feature if bias >= 0).
        prob.x = new FeatureNode[prob.l][];//`x' is an array of pointers, each of which points to a sparse representation (array of feature_node) of one training vector.
        prob.y = new double[prob.l];//`y' is an array containing the target values. (integers in classification, real numbers in regression)
		
         for (int i = 0; i < prob.l; i++) {
	            prob.x[i] = new FeatureNode[] {};
	            prob.y[i] = i;
           }
         */
        
		int i=0,batchSize=1000;
		for (Iterator it = trainList.iterator(); it.hasNext(); ) {
			
			i++;
			
			Object[] columns = (Object[]) it.next();
			
		//	log.info(columns[0]+" "+columns[1]);
			
			if(columns[1]!=null&columns[1]!=""){
				trainStrBd.append(columns[0]+" "+columns[1]+System.getProperty("line.separator"));
			}
			
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
				
				Object[] columns = (Object[]) ob;				
				
				if(columns[1]!=null&columns[1]!=""){
					if(((String)columns[1]).split(" ").length<10){
						continue;
					}
					testStrBd.append(columns[0]+" "+(String) columns[1]+System.getProperty("line.separator"));
					
					if (i % batchSize == 0) {
						FileUtil.appendText(testFileName, testStrBd.toString());
						testStrBd.delete(0, testStrBd.length());
					}//end if	
				}
				
						
			}//end for trainList
			
		    //存储最后剩余的
			FileUtil.appendText(testFileName, testStrBd.toString());
			testStrBd.delete(0, testStrBd.length());
			
		}// end for
		
		
			
	}//end buildTrainData
	
	
	public void SVMStart(){
		
	   // buildTrainData();
		
		//buildTestData();
		
		 double C;
		
	//	 for(int i=-5;i<15;i++)
	//	  {
			 
		//	C=Math.pow(2, i); "-s","2","-c",String.valueOf(C),
				
			String[] trainArgs = {"-s","7","data/"+trainFileName,"data/train.model"};//directory of training file
		
			String[] testArgs = {"-b","1" ,"data/"+testFileName, "data/train.model", "data/result.txt"};//directory of test file, model file, result file
	
			Train train=new Train();
			Predict predict=new Predict();
			
			//check_parameter();
			try {
				train.main(trainArgs);
				predict.main(testArgs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			} catch (InvalidInputDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	//        }// end for C
		 
		
	}
	
	public void CrossValidation() {
		
		  buildTrainData();
		
		  Train train = new Train();
		  int nr_fold = 10;
		  double C;
			
			 for(int i=-5;i<15;i++)
			  {
				 
				C=Math.pow(2, i);
			//这里进行交叉验证，计算精确度
				 
			 String[] trainArgs = {"-s","0","-c",String.valueOf(C),"-v",String.valueOf(nr_fold),"-q","data/"+trainFileName,"data/train.model"};
						 
			 try {
				 System.out.println("C is : "+C);
				train.main(trainArgs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidInputDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
		
	}

	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-7-9-上午10:30:00
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//liblinear调用手册为 https://github.com/CrazySnailer/liblinear-java
		
		PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
		log.info("正在创建数据库连接和缓冲池...");
	    AppContext.initAppCtx();
		log.info("数据库连接已连接！缓冲池已建立");
	
		TextLableLinear textLableLinear=(TextLableLinear) AppContext.appCtx.getBean("textLableLinear");
		
		//交叉验证
		//textLableLinear.CrossValidation();
		
		//训练
		textLableLinear.SVMStart();
		
		
	}


}
