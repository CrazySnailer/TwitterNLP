
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.mainApp
* @file BuildTermDictionary.java
* 
* @date 2013-6-25-上午11:45:16
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.mainApp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


import javax.annotation.Resource;

import isiteam.TwitterNLP.database.bean.Term;
import isiteam.TwitterNLP.database.bean.TermDic;
import isiteam.TwitterNLP.database.bean.Testset;
import isiteam.TwitterNLP.database.bean.Trainset;
import isiteam.TwitterNLP.database.dao.TermDicDao;
import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.CharUtil;
import isiteam.TwitterNLP.util.Constant;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import edu.fudan.nlp.cn.ChineseTrans;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.util.exception.LoadModelException;


/**
 * @project Web
 * @author Dayong.Shen
 * @class BuildTermDictionary
 * 
 * 构造训练集和测试集的词典，具体的函数调用分别为 creatTrainDic
 * 
 * @date 2013-6-25-上午11:45:16
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class BuildTermDictionary {
	private static final Logger log =  LoggerFactory
			.getLogger(BuildTermDictionary.class);
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	@Resource
	private TermDicDao termDicDao;
	
	private static POSTagger tag;
	private static CWSTagger cws;
	private static ChineseTrans chineseTrans=new  ChineseTrans();

	//private class
	
	/**
	 * @function creatTrainDic
	 * 
	 * 创建训练集词典 （其中完成繁简转换），即把训练集一次建成词典
	 * 建立训练集的字典，其中训练集包含几个类别，字典的最终格式为
	 *     id term docFreq
	 *     id：字典的id，在这里设置的为主键
	 *     term：具体的词
	 *     docFreq：表示词的文档频，即在几个类别中的文档频
	 *     idf:表示词的逆文档频
	 * 
	 * @author Dayong.Shen
	 * @date 2011-6-29-上午10:21:23
	*/
	public void creatTrainDic(){
		
		try {		
			cws = new CWSTagger("./models/seg.m");			
			tag = new POSTagger(cws,"models/pos.m");
		}catch (LoadModelException e) {
			// TODO Auto-generated catch block
			log.error("Initial POSTagger Failed"+e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Initial POSTagger Failed"+e.getMessage());
		}
		
		tag.SetTagType("en");//使用英文标签		
	
	   /*
	    * 以上初始化分词完毕
	    */
		
		//获取训练集样本数目
		long trainDocNum=trainsetDao.getTrainCount();
		
		 int batchSize=50;		    
		 int num=(int) Math.ceil( (float) (trainDocNum)/batchSize);
		 int cursor=0;		
		 
		 //存放临时的Trainset数据
		 List<Trainset> trainsetList=new ArrayList<Trainset>();
		 
		 //每篇文档的分词结果
		 Map<String,Integer> termTFMap= new  HashMap<String,Integer>();//每篇文档的词频TF向量
		 List<Map.Entry<String, Integer>> infoIds;//每篇文档的词频向量TF的排序中间变量
		
		 String tmpStr;//分词存放句子的临时变量
				 
		 String[][] POSArray;//存放分词的数组
		 
		 StringBuilder optimalPOS=new StringBuilder();
		 StringBuilder termTFSB=new StringBuilder();
		
		 //要留下的词性标签
		 String POSType="NN VV JJ";
		 
		 //总的字典
		 Map<String,String> ToatlTermTagMap= new  HashMap<String,String>();//存放词Term的类型
		 Map<String,Integer> ToatlTermDFMap= new  HashMap<String,Integer>();//存放词Term文档频
		 
		
		 
		 for(int i=0;i<num;i++){
			 
		    	cursor=i*batchSize;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	trainsetList.clear();
		    	
		    	trainsetList=trainsetDao.getTrainConList(cursor,batchSize);
		    			    	
		    	for(int j=0;j<trainsetList.size();j++){
		    		
		    		if(trainsetList.get(j).getContent().isEmpty()){//为空，返回
		    			continue;
		    		}
		    		
		    		termTFMap.clear();//清除
		    		termTFSB.delete(0, termTFSB.length());//清除		    	
		    		optimalPOS.delete(0, optimalPOS.length());//清除
		    		
		    		 //将全角转为半角、繁体转为简体
		    		trainsetList.get(j).setContent(chineseTrans.normalize(trainsetList.get(j).getContent()));
		    		 
		    		 //过滤优化 分词与词性标注的结果		    		
		    		 //以段落或者句子为单位进行分词，下面是以换行符"\n"进行划分的文档的。		    	
		    		 StringTokenizer strToken = new StringTokenizer(trainsetList.get(j).getContent(),"\n");
		    		 
		    		 while(strToken.hasMoreTokens())
		    		   {   //利用循环来获取字符串strToken中下一个语言符号,并输出
		    			 
		    			 //过滤非中文字符
		    			 tmpStr=CharUtil.filterNonChinese(strToken.nextToken()).trim();
		    			 
			                   if(tmpStr.length()<=1){
			                	   continue;
			                   }
			                   
			                   log.info(tmpStr);

			                   POSArray= tag.tag2Array(tmpStr);//对句子的分词结果
			                   
			                   if(POSArray==null){
			                	   continue;
			                   }
			                   
			          		 for(int jj=0;jj<POSArray[0].length;jj++){//过滤不需要的词性
				    			 
				    			 if(POSArray[1][jj]==null){//类型为null
				    				 continue;
				    			 }
				    			 
				    			 //if(POSType.contains(POSArray[1][jj])){//包含所需的类型
				    				 
				    				 POSArray[0][jj]=POSArray[0][jj].trim();//去掉两边的空格
				    				 
				    				 
				    				 
				    				 if(CharUtil.ChinesePercent(POSArray[0][jj])<1||POSArray[0][jj].length()<=1){//过滤掉一些字符
				    					 /*
				    					  * 1.非汉字过滤掉
				    					  * 2.单个汉字过滤掉
				    					  */
				    					// if(CharUtil.ChinesePercent(POSArray[0][jj])<1){//过滤掉一些字符
				    						// 1.非汉字过滤掉
				    					 continue;
				    				 }
				    				 
				    				// optimalPOS.append(POSArray[0][jj]+"/"+POSArray[1][jj]+" ");
				    				 optimalPOS.append(POSArray[0][jj]+" ");
				    				 
				    				 //统计词频 
				    				 if(termTFMap.containsKey(POSArray[0][jj])){		    					 
				    					 termTFMap.put(POSArray[0][jj], termTFMap.get(POSArray[0][jj])+1);
				    					 
				    				 }else{
				    					 termTFMap.put(POSArray[0][jj], 1);
				    					 
				    					 //统计文档频 创建词典
					    				 //存储词性
				    					 ToatlTermTagMap.put(POSArray[0][jj], POSArray[1][jj]);
				    					 
				    					 //存储文档频
				    					 if(ToatlTermDFMap.containsKey(POSArray[0][jj])){
				    						 ToatlTermDFMap.put(POSArray[0][jj], ToatlTermDFMap.get(POSArray[0][jj])+1);
				    					 }else{
				    						 ToatlTermDFMap.put(POSArray[0][jj], 1);
				    					 }
				    					 
				    					 
				    				 }//end if else	
					    			 
					    		// }// end if
				    			 
				    		 }//end for
			                   
			                   
			                   

			            }//end while
		    		 
		    		
		    		 
		    		 trainsetList.get(j).setOptimalPosContent(optimalPOS.toString());
		    		 
		    		 
		    		 //添加term frequence			    		 
		    		 infoIds = new ArrayList<Map.Entry<String, Integer>>(termTFMap.entrySet());
		    		 
		    		//排序
		    		 Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
		    		     public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		    		         return (o2.getValue() - o1.getValue()); 
		    		         //return (o1.getKey()).toString().compareTo(o2.getKey());
		    		     }
		    		 }); 
		    		 
		    		//排序后
		    		 for (int k = 0; k < infoIds.size(); k++) {		    		   
		    			 termTFSB.append(infoIds.get(k).getKey() + "/" + infoIds.get(k).getValue() + " "); 
		    		 }
		    		 		    		 
		    		 trainsetList.get(j).setTermFrequence(termTFSB.toString());
		    		 
		    		 trainsetList.get(j).setInsertTime(new Timestamp(System.currentTimeMillis()));
		    		
		    	}//end for
		    	
		    	trainsetDao.batchUpdateTrainConList(trainsetList,batchSize);   
		    	
		    	log.info("ToatlTermDFMap Size: "+ToatlTermDFMap.size()+" ToatlTermTagMap Size: "+ToatlTermTagMap.size());
		    	
		    }//end for
		 
		    //建立数据库中的词典
		    List<TermDic> TermDicList=new ArrayList<TermDic>();//数据库中新词典
		    
		    for(Map.Entry<String,Integer> entry: ToatlTermDFMap.entrySet()) {  
				   log.info(entry.getKey() + ":" + entry.getValue() + "\t");  
				   
				   TermDic termDic=new TermDic();
				   termDic.setTerm(entry.getKey());				 
				   termDic.setDocCount(entry.getValue());
				   termDic.setTermTag(ToatlTermTagMap.get(entry.getKey()));
				   termDic.setInsertTime(new Timestamp(System.currentTimeMillis()));
				   
				   //计算TFIDF
				   double idf =  Math.log(trainDocNum/entry.getValue()+0.01);
				   termDic.setIdf(idf);
				   
				   TermDicList.add(termDic);
				   
				}  
		   
		   //保存字典至数据库
		   termDicDao.batchSaveTermList(TermDicList, batchSize);		
	}
	

	
	
	
	/**
	 * @function testsetSpiltWord
	 * 
	 * 对测试集进行分词和计算TDIDF，其中测试集词项的IDF由训练集给出
	 * 
	 * @author Dayong.Shen
	 * @date 2013-10-16-上午8:35:23
	*/
	public void testsetSpiltWord(){
		try {		
			cws = new CWSTagger("./models/seg.m");			
			tag = new POSTagger(cws,"models/pos.m");
		}catch (LoadModelException e) {
			// TODO Auto-generated catch block
			log.error("Initial POSTagger Failed"+e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Initial POSTagger Failed"+e.getMessage());
		}
		
		tag.SetTagType("en");//使用英文标签		
	
		
		   // 以上初始化分词完毕 
			long testDocNum=testsetDao.getTestCount();
			
			int batchSize=50;
			    
			 int num=(int) Math.ceil( (float) (testDocNum)/batchSize);
			 int cursor=0;
			
			 
			 //存放临时的Testset数据
			 List<Testset> testsetList=new ArrayList<Testset>();
			
			 String tmpStr;//分词存放句子的临时变量
					 
			 String[][] POSArray;//存放分词的数组
			 
			 Map<String,Integer> termTFMap =new HashMap<String,Integer>();//每篇文档的词频TF向量
			
			 List<Map.Entry<String, Integer>> infoIds;//每篇文档的词频向量的排序中间变量
			 
			//存放临时的TermVector
			 StringBuilder termVector=new StringBuilder();
			 Map<Integer,Double> termTFIDFMap= new  HashMap<Integer,Double>();//存放临时的每一个文档的TFIDF
			 List<Map.Entry<Integer, Double>> infoIds1;
			 
			 StringBuilder optimalPOS=new StringBuilder();
			 StringBuilder POS=new StringBuilder();
			 StringBuilder termTFSB=new StringBuilder();
			
			 //要留下的词性标签
			 String POSType="NN VV JJ";
			 
			 ComputeTFIDF computeTFIDF=new ComputeTFIDF();
			 
			 //读取特征词典
			 Map<String,Term> featureTermMap= computeTFIDF.readFeatureTermList();
			 
			 if(featureTermMap.isEmpty()){
				 log.error("No FeatureTerm List!");
				 System.exit(1);
			 }
				
			 for(int i=0;i<num;i++){
				 
			    	cursor=i*batchSize;
			    	
			    	log.info("开始取 "+cursor+" 数据");
			    	
			    	testsetList.clear();
			    	
			    	testsetList=testsetDao.getTestConList(cursor,batchSize);
			    			    	
			    	for(int j=0;j<testsetList.size();j++){
			    		
			    		if(testsetList.get(j).getContent().isEmpty()){//为空，返回
			    			continue;
			    		}
			    		
			    		termTFMap.clear();//清除每篇文档的词频TF向量
			    		termTFIDFMap.clear();
			    		termVector.delete(0, termVector.length());
			    		termTFSB.delete(0, termTFSB.length());//清除		    	
			    		optimalPOS.delete(0, optimalPOS.length());//清除
			    		POS.delete(0, POS.length());//清除
			    		
			    		
			    		 //将全角转为半角、繁体转为简体
			    		testsetList.get(j).setContent(chineseTrans.normalize(testsetList.get(j).getContent()));
			    		 
			    		 //过滤优化 分词与词性标注的结果		    		
			    		 //以段落或者句子为单位进行分词，下面是以换行符"\n"进行划分的文档的。		    	
			    		 StringTokenizer strToken = new StringTokenizer(testsetList.get(j).getContent(),"\n");
			    		 
			    		 while(strToken.hasMoreTokens())
			    		   {   //利用循环来获取字符串strToken中下一个语言符号,并输出
			    			 
			    			 //过滤非中文字符
			    			 tmpStr=CharUtil.filterNonChinese(strToken.nextToken()).trim();
			    			 
				                   if(tmpStr.length()<=1){
				                	   continue;
				                   }
				                   
				                   log.info(tmpStr);

				                   POSArray= tag.tag2Array(tmpStr);//对句子的分词结果
				                   
				                   if(POSArray==null){
				                	   continue;
				                   }
				                   
				          		 for(int jj=0;jj<POSArray[0].length;jj++){//过滤不需要的词性
					    			 
					    			 if(POSArray[1][jj]==null){//类型为null
					    				 continue;
					    			 }
					    			 
					    		//	 if(POSType.contains(POSArray[1][jj])){//包含所需的类型
					    				 
					    				 POSArray[0][jj]=POSArray[0][jj].trim();//去掉两边的空格
					    				 
					    				 
					    				 
					    				 if(CharUtil.ChinesePercent(POSArray[0][jj])<1||POSArray[0][jj].length()<=1){//过滤掉一些字符
					    					 /*
					    					  * 1.非汉字过滤掉
					    					  * 2.单个汉字过滤掉
					    					  */
					    					// if(CharUtil.ChinesePercent(POSArray[0][jj])<1){//过滤掉一些字符
					    						// 1.非汉字过滤掉
					    					 continue;
					    				 }
					    				 
					    				// optimalPOS.append(POSArray[0][jj]+"/"+POSArray[1][jj]+" ");
					    				 optimalPOS.append(POSArray[0][jj]+" ");
					    				 
					    				 //统计词频 TF
					    				 if(termTFMap.containsKey(POSArray[0][jj])){		    					 
					    					 termTFMap.put(POSArray[0][jj], termTFMap.get(POSArray[0][jj])+1);
					    					 
					    				 }else{
					    					 termTFMap.put(POSArray[0][jj], 1);
					    				 }//end if else	
						    			 
						    		// }// end if
					    			 
					    		 }//end for
				                   
				                   
				                   

				            }//end while
			    		 
			    		
			    		 
			    		 testsetList.get(j).setOptimalPosContent(optimalPOS.toString());
			    		 
			    		 
			    		 //添加term frequence			    		 
			    		 infoIds = new ArrayList<Map.Entry<String, Integer>>(termTFMap.entrySet());
			    		 
			    		//排序
			    		 Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
			    		     public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
			    		         return (o2.getValue() - o1.getValue()); 
			    		     }
			    		 }); 
			    		 
			    		//排序后
			    		 for (int k = 0; k < infoIds.size(); k++) {		    		   
			    			 termTFSB.append(infoIds.get(k).getKey() + "/" + infoIds.get(k).getValue() + " "); 
			    			 
			    			 //计算TFIDF
			    			 if(featureTermMap.containsKey(infoIds.get(k).getKey())){
			    				
			    				 POS.append(infoIds.get(k).getKey()+" ");
			    				 
			    				 int tf=Integer.valueOf(infoIds.get(k).getValue());
						    	 double idf=featureTermMap.get(infoIds.get(k).getKey()).getIdf();
						    	  
						    	 double tfidf =  tf*idf;
						    	  
						    	 termTFIDFMap.put(featureTermMap.get(infoIds.get(k).getKey()).getTermid(), tfidf);
			    				 
			    			 }
			    			 
			    		 }
			    		 		    		 
			    		 testsetList.get(j).setTermFrequence(termTFSB.toString());
			    		 
			    		 testsetList.get(j).setInsertTime(new Timestamp(System.currentTimeMillis()));
			    		 
			    		 
			    		 
			    		//归一化向量
				    		NormalVector1(termTFIDFMap);
				    		
				    		 infoIds1 = new ArrayList<Map.Entry<Integer, Double>>(termTFIDFMap.entrySet());
				    		 
				    	  		//根据Key排序
				    	  		 Collections.sort(infoIds1, new Comparator<Map.Entry<Integer, Double>>() {   
				    	  		     public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {  
				    	  		         return (o1.getKey()-o2.getKey());
				    	  		     }
				    	  		 }); 
				    	  		 
				    	  		//排序后
				    	  		 for (int k = 0; k < infoIds1.size(); k++) {		    		   
				    	  			 termVector.append(infoIds1.get(k).getKey() + ":" + infoIds1.get(k).getValue() + " "); 
				    	  		 }
			    		 
			    		 testsetList.get(j).setPosContent(POS.toString());
			    		 testsetList.get(j).setTermVector(termVector.toString());
			    		 
			 			
			    		
			    	}//end for
			    	
			    	testsetDao.batchUpdateTestConList(testsetList,batchSize);   
			    	
			    }//end for
		
	}
	
	
	public static void NormalVector1(Map<Integer,Double> TermMap)
	{
	
			double temp=0.0;
			for(Map.Entry<Integer, Double> o1:TermMap.entrySet())
			{
				temp = temp+Math.pow(o1.getValue(), 2);
			}
			
			for(Map.Entry<Integer, Double> o2:TermMap.entrySet())
			{
				 o2.setValue(o2.getValue()/Math.sqrt(temp));
			}
	
	}//end NormalVector
	
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-6-25-上午11:45:16
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  	PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
			log.info("正在创建数据库连接和缓冲池...");
		    AppContext.initAppCtx();
			log.info("数据库连接已连接！缓冲池已建立");
		
			BuildTermDictionary buildTermDic=(BuildTermDictionary) AppContext.appCtx.getBean("buildTermDictionary");
			
			//buildTermDic.creatTrainDic();
			
		
			buildTermDic.testsetSpiltWord();
			
	}

}
