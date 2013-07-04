
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import isiteam.TwitterNLP.database.bean.SegContent;
import isiteam.TwitterNLP.database.bean.TermDic;
import isiteam.TwitterNLP.database.dao.SegContentDao;
import isiteam.TwitterNLP.database.dao.TermDicDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.Constant;

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
	private SegContentDao segContentDao;
	
	@Resource
	private TermDicDao termDicDao;
	
	
	
   public void calculateTFIDF(){
	   
	    long count=segContentDao.getContentCount();
		
		log.info("总数: "+String.valueOf(count));
		
		int batchSize=50;
		int start=67199;
		     
		int num=(int) Math.ceil( (float) (count-start)/batchSize);
	    int cursor=0;
		 
		List<SegContent> segConList=new ArrayList<SegContent>();
		
		 StringBuilder termVector=new StringBuilder();
		 String[] tepSpiltStr;
		 Map<Integer,Double> tmpTermMap= new  HashMap<Integer,Double>();
		 List<Map.Entry<Integer, Double>> infoIds;
		
		
		//加载词典		
		List<TermDic> termDicList=termDicDao.getAllTermList();
		
		 //构建词典      //总的字典
		 Map<String,Integer> ToatlTermIdMap= new  HashMap<String,Integer>();
		 Map<String,Integer> ToatlTermFreMap= new  HashMap<String,Integer>();
		 
		 
		for(TermDic tepTerm:termDicList){
			ToatlTermIdMap.put(tepTerm.getTerm(),tepTerm.getId());
			ToatlTermFreMap.put(tepTerm.getTerm(), tepTerm.getDocCount());
		}
		
		//构建字典完成，释放termDicList
		termDicList.clear();
		
		 
		for(int i=0;i<num;i++){
			 
	    	cursor=i*batchSize+start;
	    	
	    	log.info("开始取 "+cursor+" 数据");
	    	
	    	segConList.clear();
	    	
	    	segConList=segContentDao.getSegContentList(cursor,batchSize);
	    			    	
	    	for(int j=0;j<segConList.size();j++){
	    		
	    		if(segConList.get(j).getTermFrequence().isEmpty()){//为空，返回
	    			continue;
	    		}
	    		
	    		termVector.delete(0, termVector.length());
	    		tmpTermMap.clear();
	    		
	    		//警察/2 感觉/1 维持/1 一边/1 遗忘/1 表现/1 示威者/1 称赞/1 秩序/1 没有/1 伤亡/1 
	    		
	    		tepSpiltStr=segConList.get(j).getTermFrequence().split(" ");
	    		
	    		for(String str:tepSpiltStr){
	    			  int index = str.indexOf("/");
				      if (index == -1)
				        continue;
				      
				      String word = str.substring(0, index).trim();
				      String termFre = str.substring(index+1, str.length()).toString();	
				      if (word == null) 
				        continue;
				      
				      
				      if(ToatlTermIdMap.containsKey(word)&&ToatlTermFreMap.containsKey(word))
				      {
				    	  int tf,df;
				    	  
				    	  tf=Integer.valueOf(termFre);
				    	  df=ToatlTermFreMap.get(word);
				    	  
				    	  double tfidf =  tf*Math.log(count/df);
				    	  
				    	  tmpTermMap.put(ToatlTermIdMap.get(word), tfidf);
				    	  
				      }
				      
				      
				      
	    		}//end for tepSpiltStr
	    		
	    		 infoIds = new ArrayList<Map.Entry<Integer, Double>>(tmpTermMap.entrySet());
	    		 
		    		//排序
		    		 Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {   
		    		     public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
		    		         //return (o2.getValue() - o1.getValue()); 
		    		         return (o1.getKey()-o2.getKey());
		    		     }
		    		 }); 
		    		 
		    		//排序后
		    		 for (int k = 0; k < infoIds.size(); k++) {		    		   
		    			 termVector.append(infoIds.get(k).getKey() + ":" + infoIds.get(k).getValue() + " "); 
		    		 }
		    		 
		    		 segConList.get(j).setTermVector(termVector.toString());
	    		
	    	}//end for segConList
	    	
	    	segContentDao.batchUpdateSegConList(segConList,batchSize);   
	    	
	    	//log.info("ToatlTermFreMap Size: "+ToatlTermFreMap.size()+" ToatlTermTagMap Size: "+ToatlTermTagMap.size());
	    }//end for num
		 
		 
	   
   }//end calculateTFIDF
   
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
			
			computeTFIDF.calculateTFIDF();
	}

}
