
/**
* @project Web
* @author Dayong.Shen
* @package isiteam.TwitterNLP.computPerformance
* @file UserInterest.java
* 
* @date 2013-11-7-下午9:25:49
* @Copyright 2013 ISI Team of NUDT-版权所有
* 
*/
 
package isiteam.TwitterNLP.computPerformance;


/**
 * @project Web
 * @author Dayong.Shen
 * @class UserInterest
 * 
 * @date 2013-11-7-下午9:25:49
 * @Copyright 2013 ISI Team of NUDT-版权所有
 * @Version 1.0.0
 */

public class UserInterest {
	
	private String userId;
	private int total;
	private int type0;
	private int type1;
	private int type2;
	private int type3;
	private int type4;
	private int type5;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getType0() {
		return type0;
	}
	public void setType0(int type0) {
		this.type0 = type0;
	}
	public int getType1() {
		return type1;
	}
	public void setType1(int type1) {
		this.type1 = type1;
	}
	public int getType2() {
		return type2;
	}
	public void setType2(int type2) {
		this.type2 = type2;
	}
	public int getType3() {
		return type3;
	}
	public void setType3(int type3) {
		this.type3 = type3;
	}
	public int getType4() {
		return type4;
	}
	public void setType4(int type4) {
		this.type4 = type4;
	}
	public int getType5() {
		return type5;
	}
	public void setType5(int type5) {
		this.type5 = type5;
	}

	
	@Override
	public String toString()
	{
		String S = userId+" "+type0+" "+type1+" "+type2+" "+type3+" "+type4+" "+type5;
		return S;
	}
	

}
