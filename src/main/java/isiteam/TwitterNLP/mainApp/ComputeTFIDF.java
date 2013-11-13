
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.mainApp
* @file IFIDF.java
* 
* @date 2013-7-4-上午8:36:29
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.mainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import isiteam.TwitterNLP.database.bean.Term;
import isiteam.TwitterNLP.database.bean.TermDic;
import isiteam.TwitterNLP.database.bean.Trainset;
import isiteam.TwitterNLP.database.dao.TermDicDao;
import isiteam.TwitterNLP.database.dao.TestsetDao;
import isiteam.TwitterNLP.database.dao.TrainsetDao;
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
 * @class IFIDF
 * 
 *  计算文档的IFIDF
 * 
 * @date 2013-7-4-上午8:36:29
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */
@Controller
public class ComputeTFIDF {
	private static final Logger log =  LoggerFactory
			.getLogger(ComputeTFIDF.class);
	
	@Resource
	private TrainsetDao trainsetDao;
	
	@Resource
	private TestsetDao testsetDao;
	
	@Resource
	private TermDicDao termDicDao;
	
	
	
   
/**
 * @function getFeatureTermListByCategory
 * 
 * 获取每个训练集的每个类别中的特征词，这些特征词的数量由FeatureNum决定，
 * 获取特征词的依据是根据其TFIDF的排序进行筛选的，取top-k，k为设定的FeatureNum，
 * 其中TF的计算，为所有文档中TF最大的，DF为训练集中的文档频
 * 每个类别获取的特征词写入type.feature文件夹下面，type为类别的编号
 * 
 * @author Dayong.Shen
 * @date 2013-10-15-上午9:12:00
*/
public void getFeatureTermListByCategory(int type,int FeatureNum){
	   
	  
	   //int FeatureNum=2000;//每个类别中特征词的个数
	   
	    long count=trainsetDao.getTrainCountByType(type);
	   
	   //long count=50;
		
		log.info("Type: "+type+"总数: "+String.valueOf(count));
		
		int batchSize=200;
		int start=0;
		     
		int num=(int) Math.ceil( (float) (count-start)/batchSize);
	    int cursor=0;
		 
	    //存放临时的Trainset List
		List<Trainset> trainsetList=new ArrayList<Trainset>();
		
		String[] tepSpiltStr;		
		 
		 Map<String,Integer> categoryTermTFMap= new  HashMap<String,Integer>();//存放所有文档中TF最大的
		 Map<String,Double> categoryTermTFIDFMap= new  HashMap<String,Double>();
		 Map<String,Double> categoryFeatureTermTFIDFMap= new  HashMap<String,Double>();
		 List<Map.Entry<String, Double>> infoIds2;
		
		//加载词典		
		List<TermDic> termDicList=termDicDao.getAllTermList();
		
		 //总的字典MAP
		 Map<String,Integer> TermDicIdMap= new  HashMap<String,Integer>();
		 Map<String,Double> TermDicIDFMap= new  HashMap<String,Double>();
		 
		 
		for(TermDic tepTerm:termDicList){
			TermDicIdMap.put(tepTerm.getTerm(),tepTerm.getId());
			TermDicIDFMap.put(tepTerm.getTerm(), tepTerm.getIdf());
		}
		
		//构建字典完成，释放termDicList
		termDicList.clear();
		
		 
		for(int i=0;i<num;i++){
			 
	    	cursor=i*batchSize+start;
	    	
	    	log.info("开始取 "+cursor+" 数据");
	    	
	    	trainsetList.clear();
	    	
	    	trainsetList=trainsetDao.getTrainConList(cursor,batchSize,type);
	    			    	
	    	try{
	    		
	    		for(int j=0;j<trainsetList.size();j++){
		    		
		    		if(trainsetList.get(j).getTermFrequence().isEmpty()){//为空，返回
		    			continue;
		    		}
		    		
		    		
		    		
		    		//警察/2 感觉/1 维持/1 一边/1 遗忘/1 表现/1 示威者/1 称赞/1 秩序/1 没有/1 伤亡/1 
		    		
		    		tepSpiltStr=trainsetList.get(j).getTermFrequence().split(" ");
		    		
		    		for(String str:tepSpiltStr){
		    			  int index = str.indexOf("/");
					      if (index == -1)
					        continue;
					      
					      String word = str.substring(0, index).trim();
					      String termFre = str.substring(index+1, str.length()).toString();	
					      if (word == null) 
					        continue;
					      
					      if(categoryTermTFMap.containsKey(word)){//如果类别词典中包含该词					    	  
					    	 
					    	  if(Integer.valueOf(termFre)>categoryTermTFMap.get(word)){
					    		  categoryTermTFMap.put(word, Integer.valueOf(termFre));
					    	  }
					    	  
					    	  // categoryTermTFMap.put(word, Integer.valueOf(termFre)+categoryTermTFMap.get(word));
					      }else{
					    	  categoryTermTFMap.put(word, Integer.valueOf(termFre));
					      }
					      
		    		}//end for tepSpiltStr
		    		
		    	}//end for segConList
	    		
	    	}catch(Exception e){
	    		log.error("Loop segConList : "+e.getMessage());
	    	}    	
	  
	    }//end for num
		
        //计算每个类别中词的TFIDF        
        for(Map.Entry<String,Integer> entry : categoryTermTFMap.entrySet()){
        	
        	if(TermDicIDFMap.containsKey(entry.getKey()))
	  	      {
	  	    	 int tf=entry.getValue();
	  	    	 double idf=TermDicIDFMap.get(entry.getKey());
	  	    	  
	  	    	 double tfidf =  tf*idf;
	  	    	  
	  	    	categoryTermTFIDFMap.put(entry.getKey(),tfidf);
	  	    	  
	  	      }        	
        }
        
      //归一化向量
        NormalVector2(categoryTermTFIDFMap);
      
        infoIds2= new ArrayList<Map.Entry<String, Double>>(categoryTermTFIDFMap.entrySet());
      //排序
 		 Collections.sort(infoIds2, new Comparator<Map.Entry<String, Double>>() {   
 		     public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {      
 		    	 if ((o2.getValue() - o1.getValue())>0)  
 		            return 1;  
 		          else if((o2.getValue() - o1.getValue())==0)  
 		            return 0;  
 		          else   
 		            return -1;  
 		     }
 		 }); 
 		 
 		//排序后，取前top-k，k为FeatureNum个作为特征词
 		 for (int k = 0; k < FeatureNum; k++) {		    		   
 			categoryFeatureTermTFIDFMap.put(infoIds2.get(k).getKey() ,infoIds2.get(k).getValue() ); 
 			FileUtil.appendText(String.valueOf(type)+".feature", infoIds2.get(k).getKey() +" "+infoIds2.get(k).getValue()+System.getProperty("line.separator"));
 		 } 		 
	   
   }//end GetFeatureTermListByCategory
   
   
  
   
/**
 * @function calculatePerDocTFIDF
 * 
 * 计算最终的TDIDF，由于最终的特证词库已在FinalTermList.txt，因此我们只需计算最终的文档中特征词库中包含词的TFIDF
 * 而其他的非特征词则过滤掉，以达到降维的目的。
 * 
 * @author Dayong.Shen
 * @date 2013-10-15-上午9:21:55
*/
public void calculatePerDocTFIDF(){
	    int type=0;	   
	   
	    long count=trainsetDao.getTrainCount();
		
		log.info("Type: "+type+"总数: "+String.valueOf(count));
		
		int batchSize=50;
		int start=0;
		     
		int num=(int) Math.ceil( (float) (count-start)/batchSize);
	    int cursor=0;
		 
	    //存放临时的Trainset List
		List<Trainset> trainsetList=new ArrayList<Trainset>();
		
		//存放临时的TermVector
		 StringBuilder termVector=new StringBuilder();
		 String[] tepSpiltStr;
		 StringBuilder SpiltDoc=new StringBuilder();
		 Map<Integer,Double> tmpTermMap= new  HashMap<Integer,Double>();//存放临时的每一个文档的TFIDF
		 List<Map.Entry<Integer, Double>> infoIds1;
		
		 Map<String,Term> featureTermMap= readFeatureTermList();
		
		 
		for(int i=0;i<num;i++){
			 
	    	cursor=i*batchSize+start;
	    	
	    	log.info("开始取 "+cursor+" 数据");
	    	
	    	trainsetList.clear();
	    	
	    	trainsetList=trainsetDao.getTrainConList(cursor,batchSize);
	    			    	
	    	try{
	    		
	    		for(int j=0;j<trainsetList.size();j++){
		    		
		    		if(trainsetList.get(j).getTermFrequence().isEmpty()){//为空，返回
		    			continue;
		    		}
		    		
		    		 termVector.delete(0, termVector.length());
		    	     tmpTermMap.clear();
		    	     SpiltDoc.delete(0, SpiltDoc.length());
		    		
		    		//警察/2 感觉/1 维持/1 一边/1 遗忘/1 表现/1 示威者/1 称赞/1 秩序/1 没有/1 伤亡/1 
		    		
		    		tepSpiltStr=trainsetList.get(j).getTermFrequence().split(" ");
		    		
		    		for(String str:tepSpiltStr){
		    			  int index = str.indexOf("/");
					      if (index == -1)
					        continue;
					      
					      String word = str.substring(0, index).trim();
					      String termFre = str.substring(index+1, str.length()).toString();	
					      if (word == null) 
					        continue;
					      
					      
					      if(featureTermMap.containsKey(word))
					      {
					    	  SpiltDoc.append(word);
					    	  SpiltDoc.append(" ");
					    	  
					    	  int tf=Integer.valueOf(termFre);
					    	  double idf=featureTermMap.get(word).getIdf();
					    	  
					    	  double tfidf =  tf*idf;
					    	  
					    	  tmpTermMap.put(featureTermMap.get(word).getTermid(), tfidf);
					    	  
					      }else{
					    	  continue;
					      }
					      
		    		}//end for tepSpiltStr
		    		
		    		//归一化向量
		    		NormalVector1(tmpTermMap);
		    		
		    		 infoIds1 = new ArrayList<Map.Entry<Integer, Double>>(tmpTermMap.entrySet());
		    		 
		    	  		//根据Key排序
		    	  		 Collections.sort(infoIds1, new Comparator<Map.Entry<Integer, Double>>() {   
		    	  		     public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
		    	  		         //return (o2.getValue() - o1.getValue()); 
		    	  		         return (o1.getKey()-o2.getKey());
		    	  		     }
		    	  		 }); 
		    	  		 
		    	  		//排序后
		    	  		 for (int k = 0; k < infoIds1.size(); k++) {		    		   
		    	  			 termVector.append(infoIds1.get(k).getKey() + ":" + infoIds1.get(k).getValue() + " "); 
		    	  		 }
		    	  	
		    	  		trainsetList.get(j).setPosContent(SpiltDoc.toString());
		    	  		trainsetList.get(j).setTermVector(termVector.toString());
		    		
		    	}//end for segConList
	    		
	    		trainsetDao.batchUpdateTrainConList(trainsetList,batchSize);  
	    		
	    	}catch(Exception e){
	    		log.error("Loop segConList : "+e.getMessage());
	    	}    	
	  
	    }//end for num
		
		
	   
   }//end calculateFinalTFIDF
  
 
 
   
   
/**
 * @function getTotalFeatureTermList
 * 
 * @param filePath
 * @param fileName
 * 
 * 
 * 合并每个候选类别下的特征词，形成总的特征词库
 * 
 * 即对每个类别的特征词文本 1.feature 2.feature 3.feature ... 进行读取,合并其中相同的词，
 * 形成最终的特征词表featureTermList.txt
 * 
 * 特征词表结构为：id term idf
 *        其中     id：特征词的id编号
 *             term：特征词
 *             idf：特征词的idf 
 * 
 * @author Dayong.Shen
 * @date 2013-10-15-上午9:18:21
*/
public void getTotalFeatureTermList(String filePath,String fileName){
		
	    //存放TotalFeatureTerm的Map
	    Map<String,Integer> featureTermMap= new  HashMap<String,Integer>();
	   
	    filePath=filePath+File.separator +fileName;
		
		final String extend_name = ".feature";
		
		File dir = new File(filePath);
		if(!dir.isDirectory()){
			log.info("不是有效的文件夹目录!"+filePath);
			System.exit(-1);			
		}
		// 所有的文件和目录名
	    String[] children=null;
	    
	    // 可以指定返回文件列表的过滤条件,返回那些以extend_name开头的文件名
	    FilenameFilter filter = new FilenameFilter() {
	      public boolean accept(File dir, String name) {
	        return name.endsWith(extend_name);
	      }
	    };
	    children = dir.list(filter);
	    
	    for (int i = 0; i < children.length; i++) {
	      // 文件名
	    	log.info(children[i]);
	      
	      	File file = new File(filePath+File.separator +children[i]);
	      	
	      
	        InputStream is = null;
	        OutputStream os = null;
	     
	        try {
	        	
	            if (file.exists()) {
	            	 BufferedReader br = new BufferedReader(new java.io.FileReader(file));  
	                // StringBuffer sb = new StringBuffer();  
	                 String line = br.readLine();  
	                 
	                 String[] tepSpiltStr;
	                 
	                 while (line != null) {  
	                  //   sb.append(line);
	                	 tepSpiltStr= line.split(" ");
	                	 featureTermMap.put(tepSpiltStr[0], 0);
	                     
	                     line = br.readLine();  
	                 }  
	                 br.close();  
	              
	            }
	          
	           
	           
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	            //System.exit(-1);
	        } finally {
	            if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException ignore) {
	                }
	            }
	            if (os != null) {
	                try {
	                    os.close();
	                } catch (IOException ignore) {
	                }
	            }
	        }
	      
	    }//end for
		
		//加载总词典		
		List<TermDic> termDicList=termDicDao.getAllTermList();
		
		int termNum=1;//记词编号的
		for(TermDic tepTerm:termDicList){
			if(featureTermMap.containsKey(tepTerm.getTerm())){
								
				//将最终的TermList写入到Txt文件中去
				FileUtil.appendText("featureTermList.txt", tepTerm.getTerm()+" "+termNum+" "+tepTerm.getIdf()+System.getProperty("line.separator"));
				
				termNum++;
			}
		}
	
		//构建字典完成，释放termDicList
		termDicList.clear();
		
		
	}//end GetTotalFeatureTermList
   

    public Map<String,Term> readFeatureTermList(){
    	
    	Map<String,Term> featureTermMap=new HashMap<String,Term>();
    	
    	File file = new File(Constant.data_PATH+File.separator +"featureTermList.txt");
    	
    	 try {  
             BufferedReader br = new BufferedReader(new java.io.FileReader(file));          
             String line = br.readLine(); 
             String[] tepSpiltStr;
             
             while (line != null) {             
                 
                 tepSpiltStr= line.split(" ");
                 
                 Term tmpTerm=new Term();
                 
                 tmpTerm.setTerm(tepSpiltStr[0]);
                 tmpTerm.setIdf(Double.valueOf(tepSpiltStr[2]));
                 tmpTerm.setTermid(Integer.valueOf(tepSpiltStr[1]));
                 
                 featureTermMap.put(tepSpiltStr[0], tmpTerm);
                 line = br.readLine(); 
             }  
             br.close();  
           
         } catch (IOException e) {  
         	log.error("readText ERROR!"+e.getMessage());   
            
         }  
    	
		return featureTermMap;    	
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
	
	public static void NormalVector2(Map<String,Double> TermMap)
	{
	
			double temp=0.0;
			for(Map.Entry<String, Double> o1:TermMap.entrySet())
			{
				temp = temp+Math.pow(o1.getValue(), 2);
			}
			
			for(Map.Entry<String, Double> o2:TermMap.entrySet())
			{
				 o2.setValue(o2.getValue()/Math.sqrt(temp));
			}
	
	}//end NormalVector
   
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-7-4-上午8:36:29
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		    PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
			log.info("正在创建数据库连接和缓冲池...");
		    AppContext.initAppCtx();
			log.info("数据库连接已连接！缓冲池已建立");
		
			ComputeTFIDF computeTFIDF=(ComputeTFIDF) AppContext.appCtx.getBean("computeTFIDF");
		
			int FeatureNum=2000;//每个类别中特征词的个数
			
			//计算每个类别下的特征词
			for(int i=0;i<=5;i++){
				computeTFIDF.getFeatureTermListByCategory(i,FeatureNum);
			}
			
		
			
			//合并所有类别的特征词，构成特征词库
			computeTFIDF.getTotalFeatureTermList(Constant.data_PATH, "");
			
			
			
			//根据特征词库计算每篇文档的TFIDF
			computeTFIDF.calculatePerDocTFIDF();

	}

}
