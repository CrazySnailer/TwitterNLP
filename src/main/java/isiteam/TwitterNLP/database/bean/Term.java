
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.database.bean
* @file Term.java
* 
* @date 2013-10-15-下午5:23:55
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.database.bean;


/**
 * @project Web
 * @author Dayong.Shen
 * @class Term
 * 
 * @date 2013-10-15-下午5:23:55
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public class Term {
	
	
	private String term;
	private int termid;
	private int tf;
	private double idf;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getTermid() {
		return termid;
	}

	public void setTermid(int termid) {
		this.termid = termid;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}


}
