
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.libsvm
* @file LibSVMTest.java
* 
* @date 2013-7-3-下午3:39:36
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.libsvm;

import libsvm.*;

/**
 * @project Web
 * @author Dayong.Shen
 * @class LibSVMTest
 * 
 * @date 2013-7-3-下午3:39:36
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

import java.io.IOException;



public class LibSVMTest {

	/**JAVA test code for LibSVM
	 * @author yangliu
	 * @throws IOException 
	 * @blog http://blog.csdn.net/yangliuy
	 * @mail yang.liu@pku.edu.cn
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Test for svm_train and svm_predict
		//svm_train: 
		//	  param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//	  param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		String[] trainArgs = {"data/UCI-breast-cancer-tra","data/UCI-breast-cancer-tra.model"};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {"data/UCI-breast-cancer-test", modelFile, "data/UCI-breast-cancer-result"};//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		
		//Test for cross validation
		//String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation
		//modelFile = svm_train.main(crossValidationTrainArgs);
		//System.out.print("Cross validation is done! The modelFile is " + modelFile);
	}

}