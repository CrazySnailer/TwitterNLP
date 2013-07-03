
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




import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import isiteam.TwitterNLP.database.bean.News;
import isiteam.TwitterNLP.database.bean.SegContent;
import isiteam.TwitterNLP.database.bean.TermDic;
import isiteam.TwitterNLP.database.dao.SegContentDao;
import isiteam.TwitterNLP.database.dao.TermDicDao;
import isiteam.TwitterNLP.util.AppContext;
import isiteam.TwitterNLP.util.CharUtil;
import isiteam.TwitterNLP.util.Constant;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.ChineseTrans;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.util.exception.LoadModelException;


/**
 * @project Web
 * @author Dayong.Shen
 * @class BuildTermDictionary
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
	private SegContentDao segContentDao;
	
	@Resource
	private TermDicDao termDicDao;
	
	private static POSTagger tag;
	private static CWSTagger cws;
	private static ChineseTrans chineseTrans=new  ChineseTrans();

	//private class
	
	/**
	 * @function creatDictionary
	 * 
	 * 创建词典 （其中完成繁简转换）
	 * 
	 * @author Dayong.Shen
	 * @date 2011-6-29-上午10:21:23
	*/
	public void creatDictionary(){
		
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
		
		long count=segContentDao.getContentCount();
		
		log.info("总数"+String.valueOf(count));
		
		 int batchSize=50;
		    
		 int num=(int) Math.ceil( (float) (count)/batchSize);
		 int cursor=0;
		 
		 List<SegContent> segConList=new ArrayList<SegContent>();
		// String POSStr;
		 String tmpStr;
				 
		 String[][] POSArray;
		 StringBuilder optimalPOS=new StringBuilder();
		 StringBuilder termFrequence=new StringBuilder();
		
		 
		 String POSType="NN VV JJ";
		 
		 //总的字典
		 Map<String,String> ToatlTermTagMap= new  HashMap<String,String>();
		 Map<String,Integer> ToatlTermFreMap= new  HashMap<String,Integer>();
		 
		 //每篇文档的HashMap
		 Map<String,Integer> TermFreMap= new  HashMap<String,Integer>();
		 List<Map.Entry<String, Integer>> infoIds;
		 
		 
		 
		 for(int i=0;i<num;i++){
			 
		    	cursor=i*batchSize;
		    	
		    	log.info("开始取 "+cursor+" 数据");
		    	
		    	segConList.clear();
		    	
		    	segConList=segContentDao.getSegContentList(cursor,batchSize);
		    			    	
		    	for(int j=0;j<segConList.size();j++){
		    		
		    		if(segConList.get(j).getContent().isEmpty()){//为空，返回
		    			continue;
		    		}
		    		
		    		TermFreMap.clear();//清除
		    		termFrequence.delete(0, termFrequence.length());//清除		    	
		    		optimalPOS.delete(0, optimalPOS.length());//清除
		    		
		    		 //将全角转为半角、繁体转为简体
		    		 segConList.get(j).setContent(chineseTrans.normalize(segConList.get(j).getContent()));
		    		 
		    		 //过滤优化 分词与词性标注的结果		    		
		    		 //若文章太长，可考虑以段落或者句子为单位进行分词		    	
		    		 StringTokenizer strToken = new StringTokenizer(segConList.get(j).getContent(),"\n");
		    		 
		    		 while(strToken.hasMoreTokens())
		    		   {   //利用循环来获取字符串strToken中下一个语言符号,并输出
		    			 
		    			 tmpStr=CharUtil.filterNonChinese(strToken.nextToken()).trim();
		    			 
			                   if(tmpStr.length()<=1){
			                	   continue;
			                   }
			                   
			                   log.info(tmpStr);

			                   POSArray= tag.tag2Array(tmpStr);
			                   
			                   if(POSArray==null){
			                	   continue;
			                   }
			                   
			          		 for(int jj=0;jj<POSArray[0].length;jj++){
				    			 
				    			 if(POSArray[1][jj]==null){//类型为null
				    				 continue;
				    			 }
				    			 
				    			 if(POSType.contains(POSArray[1][jj])){//包含所需的类型
				    				 
				    				 POSArray[0][jj]=POSArray[0][jj].trim();//去掉两边的空格
				    				 
				    				 
				    				 
				    				 if(CharUtil.ChinesePercent(POSArray[0][jj])<1||POSArray[0][jj].length()<=1){//过滤掉一些字符
				    					 /*
				    					  * 1.非汉字过滤掉
				    					  * 2.单个汉字过滤掉
				    					  */
				    					 continue;
				    				 }
				    				 
				    				 optimalPOS.append(POSArray[0][jj]+"/"+POSArray[1][jj]+" ");
				    				 
				    				 //统计词频 
				    				 if(TermFreMap.containsKey(POSArray[0][jj])){		    					 
				    					 TermFreMap.put(POSArray[0][jj], TermFreMap.get(POSArray[0][jj])+1);
				    					 
				    				 }else{
				    					 TermFreMap.put(POSArray[0][jj], 1);
				    					 
				    					 //统计文档频 创建词典
					    				 //存储词性
				    					 ToatlTermTagMap.put(POSArray[0][jj], POSArray[1][jj]);
				    					 
				    					 //存储文档频
				    					 if(ToatlTermFreMap.containsKey(POSArray[0][jj])){
				    						 ToatlTermFreMap.put(POSArray[0][jj], ToatlTermFreMap.get(POSArray[0][jj])+1);
				    					 }else{
				    						 ToatlTermFreMap.put(POSArray[0][jj], 1);
				    					 }
				    					 
				    					 
				    				 }//end if else	
					    			 
					    		 }// end if
				    			 
				    		 }//end for
			                   
			                   
			                   

			            }//end while
		    		 
		    		
		    		 
		    		 segConList.get(j).setOptimalPosContent(optimalPOS.toString());
		    		 
		    		 /*
		    		  * 添加term frequence		    		   
		    		  */
		    		 
		    		 infoIds = new ArrayList<Map.Entry<String, Integer>>(TermFreMap.entrySet());
		    		 
		    		//排序
		    		 Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
		    		     public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		    		         return (o2.getValue() - o1.getValue()); 
		    		         //return (o1.getKey()).toString().compareTo(o2.getKey());
		    		     }
		    		 }); 
		    		 
		    		//排序后
		    		 for (int k = 0; k < infoIds.size(); k++) {		    		   
		    		     termFrequence.append(infoIds.get(k).getKey() + "/" + infoIds.get(k).getValue() + " "); 
		    		 }
		    		 
		    		
		    		 
		    		 segConList.get(j).setTermFrequence(termFrequence.toString());
		    		 
		    		 segConList.get(j).setInsertTime(new Timestamp(System.currentTimeMillis()));
		    		 
		    		 
		    	  /* //POS 分词与词性标注
		    		 POSStr=tag.tag(segConList.get(j).getContent());
		    		//存储 分词与词性标注的结果
			    	segConList.get(j).setPosContent(POSStr); 
			    	*/
		    	
		    		
		    	}//end for
		    	
		    	segContentDao.batchUpdateSegConList(segConList,batchSize);   
		    	
		    	log.info("ToatlTermFreMap Size: "+ToatlTermFreMap.size()+" ToatlTermTagMap Size: "+ToatlTermTagMap.size());
		    	
		    }//end for
		 
		    List<TermDic> TermDicList=new ArrayList<TermDic>();
			
			for(Map.Entry<String,Integer> entry: ToatlTermFreMap.entrySet()) {  
				   log.info(entry.getKey() + ":" + entry.getValue() + "\t");  
				   
				   TermDic termDic=new TermDic();
				   termDic.setTerm(entry.getKey());
				   termDic.setDocCount(entry.getValue());
				   termDic.setTermTag(ToatlTermTagMap.get(entry.getKey()));
				   termDic.setInsertTime(new Timestamp(System.currentTimeMillis()));
				   
				   TermDicList.add(termDic);
				   
				}  
		 
		 
		   termDicDao.batchSaveTermList(TermDicList, batchSize);
		
		
	}
	
	

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
			
			buildTermDic.creatDictionary();
			

	}

}
