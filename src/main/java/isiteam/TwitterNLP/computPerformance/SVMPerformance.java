
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.computPerformance
* @file SVMPerformance.java
* 
* @date 2013-11-7-下午4:11:38
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.computPerformance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import isiteam.TwitterNLP.util.Constant;
import isiteam.TwitterNLP.util.FileUtil;


/**
 * @project Web
 * @author Dayong.Shen
 * @class SVMPerformance
 * 
 * @date 2013-11-7-下午4:11:38
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public class SVMPerformance {
	private static Log log = LogFactory.getLog(SVMPerformance.class);
	
	public void ConfusionMatrix(){
		
		int[][] ConfusionMatrix=new int[6][6];             //定义一个float类型的2维数组
		
		for(int i=0;i<6;i++){
			for(int j=0;j<6;j++){
			ConfusionMatrix[i][j]=0;
			}
		}
		
		String realTag=FileUtil.readText("testTag.txt");  
		String classTag=FileUtil.readText("result.txt");  
		
		String [] realTagArray=realTag.split(" ");
		String [] classTagArray=classTag.split(" ");
		
		if(realTagArray.length!=classTagArray.length){
			log.error("realTagArray.length!=classTagArray.length ");
			System.exit(1);
		}
	     for(int i=0;i<realTagArray.length;i++){

	    	 ConfusionMatrix[Integer.valueOf(realTagArray[i])][Integer.valueOf(classTagArray[i])]=ConfusionMatrix[Integer.valueOf(realTagArray[i])][Integer.valueOf(classTagArray[i])]+1;
	     }
	     
	     StringBuilder ConfusionMatrixStr=new StringBuilder();
	     int sampleNum=0;
	     for(int i=0;i<6;i++){
				for(int j=0;j<6;j++){
					   if(j==5)
					     ConfusionMatrixStr.append(ConfusionMatrix[i][j]+";");
					   else
						 ConfusionMatrixStr.append(ConfusionMatrix[i][j]+", ");
					   sampleNum=sampleNum+ConfusionMatrix[i][j];
					 }	
				ConfusionMatrixStr.append(System.getProperty("line.separator"));
			}
	     log.info("sampleNum: "+sampleNum);
	     log.info(ConfusionMatrixStr.toString());
	}
	
public static void userInterest(){
		
		Map<String,UserInterest> interestMap=new HashMap<String,UserInterest>();             //定义一个float类型的2维数组
		
		
		String realTag=FileUtil.readText("testTag.txt");  
		String classTag=FileUtil.readText("result.txt");  
		
		String [] realTagArray=realTag.split(" ");
		String [] classTagArray=classTag.split(" ");
		
		int type0=0,type1=0,type2=0,type3=0,type4=0,type5=0;
		
		if(realTagArray.length!=classTagArray.length){
			log.error("realTagArray.length!=classTagArray.length ");
			System.exit(1);
		}
	     for(int i=0;i<realTagArray.length;i++){
	    	 if(!interestMap.containsKey(realTagArray[i])){
	    		 UserInterest tmp=new UserInterest();
	    		 tmp.setUserId(realTagArray[i]);
	    		 if(classTagArray[i].equalsIgnoreCase("0")){
	    			 tmp.setType0(1);
	    		     type0++;
	    		 }
	    		 else if(classTagArray[i].equalsIgnoreCase("1")){
	    			 tmp.setType1(1);
	    			 type1++;
	    		 }
	    		 else if(classTagArray[i].equalsIgnoreCase("2")){
	    			 tmp.setType2(1);
	    			 type2++;
	    		 }
	    		 else if(classTagArray[i].equalsIgnoreCase("3")){
	    			 tmp.setType3(1);
	    			 type3++;
	    		 }
	    		 else if(classTagArray[i].equalsIgnoreCase("4")){
	    			 tmp.setType4(1);
	    			 type4++;
	    		 }
	    		 else if(classTagArray[i].equalsIgnoreCase("5")){
	    			 tmp.setType5(1);
	    			 type5++;
	    		 }
	    			 
	    		 interestMap.put(tmp.getUserId(), tmp);
	    	 }else{
	    		 
	    		 if(classTagArray[i].equalsIgnoreCase("0")){
	    			 interestMap.get(realTagArray[i]).setType0(interestMap.get(realTagArray[i]).getType0()+1);
	    		      type0++;
    		      }
	    		 else if(classTagArray[i].equalsIgnoreCase("1")){
	    			 interestMap.get(realTagArray[i]).setType1(interestMap.get(realTagArray[i]).getType1()+1);
	    			 type1++;
   		         }
	    		 else if(classTagArray[i].equalsIgnoreCase("2")){
	    			 interestMap.get(realTagArray[i]).setType2(interestMap.get(realTagArray[i]).getType2()+1);
	    			 type2++;
   		        }
	    		 else if(classTagArray[i].equalsIgnoreCase("3")){
	    			 interestMap.get(realTagArray[i]).setType3(interestMap.get(realTagArray[i]).getType3()+1);
	    			 type3++;
   		        }
	    		 else if(classTagArray[i].equalsIgnoreCase("4")){
	    			 interestMap.get(realTagArray[i]).setType4(interestMap.get(realTagArray[i]).getType4()+1);
	    			 type4++;
   		        }
	    		 else if(classTagArray[i].equalsIgnoreCase("5")){
	    			 interestMap.get(realTagArray[i]).setType5(interestMap.get(realTagArray[i]).getType5()+1);
	    			 type5++;
   		       }
	    		 
	    	 }
	     }
	     
	    
	     log.info("All Type Distribution: "+" "+type0+" "+type1+" "+type2+" "+type3+" "+type4+" "+type5);
	     
	     for(Map.Entry<String,UserInterest> entry : interestMap.entrySet()){
	    	 FileUtil.appendText("userInterest.txt", entry.getValue().toString()+System.getProperty("line.separator"));
	     }
	     
	     
	     int usertype0=0,usertype1=0,usertype2=0,usertype3=0,usertype4=0,usertype5=0;
	     
	     for(Map.Entry<String,UserInterest> entry : interestMap.entrySet()){
	    	 if(entry.getValue().getType0()>0){
	    		 usertype0++;
	    	 }
	    	 if(entry.getValue().getType1()>0){
	    		 usertype1++;
	    	 }
	    	 if(entry.getValue().getType2()>0){
	    		 usertype2++;
	    	 }
	    	 if(entry.getValue().getType3()>0){
	    		 usertype3++; 
	    	 }
	    	 if(entry.getValue().getType4()>0){
	    		 usertype4++;
	    	 }
	    	 if(entry.getValue().getType5()>0){
	    		 usertype5++;
	    	 }
	    	 
	     }
	     
	     log.info("All user Type Distribution: "+" "+usertype0+" "+usertype1+" "+usertype2+" "+usertype3+" "+usertype4+" "+usertype5);
	     
	    
	}
	
	/**
	 * @function main
	 * 
	 * @param args
	 * @author Dayong.Shen
	 * @date 2013-11-7-下午4:11:38
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configureAndWatch(Constant.LOG4J_PATH);
		
		userInterest();
		
	}

}
