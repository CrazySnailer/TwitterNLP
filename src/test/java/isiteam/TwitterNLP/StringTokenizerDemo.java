package isiteam.TwitterNLP;

import java.util.StringTokenizer;

public class StringTokenizerDemo {
	 public static void main(String[] args)

	    {

	            String str1 = "Hello world!This is Java code,stringTokenizer Demo.";

	            //声明并初始化字符串str1

	            String str2 = "How to use StringTokenizer?StringTokenizer?";

	            //声明并初始化字符串str2

	            StringTokenizer strT1 = new StringTokenizer(str1,"\n");

	            //创建StringTokenizer类的对象strT1,并构造字符串str1的分析器

	            //以空格符、","、"."及"!"作为定界符

	            StringTokenizer strT2 = new StringTokenizer(str2," ?");

	            //创建StringTokenizer类的对象strT2,并构造字符串str2的分析器

	            //以空格符及"?"作为定界符

	            int num1 = strT1.countTokens();

	            //获取字符串str1中语言符号的个数

	         int num2 = strT2.countTokens();

	            //获取字符串str2中语言符号的个数

	            System.out.println("str1 has "+num1+" words.They are:");

	            while(strT1.hasMoreTokens())

	            {   //利用循环来获取字符串str1中下一个语言符号,并输出

	                   String str = strT1.nextToken();

	                   System.out.print("\""+str+"\" ");

	            }

	            System.out.println("\nstr2 has "+num2+" words.They are:");

	            while(strT2.hasMoreTokens())

	            {   //利用循环来获取字符串str2中下一个语言符号,并输出

	                    String str = strT2.nextToken();

	                   System.out.print("\""+str+"\" ");

	            }

	    }

}
