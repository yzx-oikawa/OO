package poly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Poly {
	//属性
	private int num_of_term; //项数
	private int sign = 1;//多项式之间的符号 1+ 2-
	private Term [] Terms;
	//构造函数
	Poly(int num_of_term,int sign,Term [] Terms)
	{
		this.num_of_term = num_of_term;
		this.sign = sign;
		this.Terms = Terms;
	}
	//调用函数
	public int getSign(){return this.sign;}
	public int getNum(){return this.num_of_term;}
	public Term getTermAt(int i){return this.Terms[i];}
	//解析多项式
	public static Poly ParsePoly(String inputs,int index)
	{
		int sign = 1;
		int num_of_term = 0;
		Term[] Terms = new Term[1001];
		inputs = inputs.replace(" ", "");
		inputs = inputs.replace("	", "");
		//System.out.println(inputs);
		//正则表达式匹配
		boolean b;
		if(index==0)
			b = Pattern.matches("^((\\+|-)?\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\})$", inputs);	
		else
			b = Pattern.matches("^((\\+|-)\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\})$", inputs);	
		
		if(b) //如果匹配成功
		{
				if(inputs.charAt(0)=='-')
			{	
				sign = 2;
				inputs = inputs.substring(1, inputs.length());//去掉第一个字符
			}
			else if(inputs.charAt(0)=='+')
			{
				sign = 1;
				inputs = inputs.substring(1, inputs.length());
			}
			inputs = inputs.replace("{", "");
			inputs = inputs.replace("(", "");
			inputs = inputs.replace(")", "");
			inputs = inputs.replace("}", "");
			String[] Term = inputs.split("\\,");//在","处分割，形成项
			for (int i=0;i<Term.length;i=i+2)
			{
				//System.out.println(Integer.parseInt(Term[i])+",");
				Terms[i/2] = new Term(Integer.parseInt(Term[i]),Integer.parseInt(Term[i+1]));
				//Interger.parseInt函数：取字符串中的整数
				//Terms[0]: co=Term[0], deg=Term[1]
				//Terms[1]: co=Term[2], deg=Term[3]
				num_of_term++;
				//System.out.println(Terms[i/2].getCo()+"x^"+Terms[i/2].getDeg());
			}
			if(num_of_term>50)
			{
				System.out.println("There are more than 50 terms in one poly!");
				return null;
			}
			for (int i=0;i<num_of_term&&num_of_term>1;i++)
			{
				for(int j=i+1;j<num_of_term;j++)
				{
					if(Terms[i].getDeg()==Terms[j].getDeg())
					{
						System.out.println("The degree in the poly is repeated!");
						return null;
					}
				}
			}
			Poly a = new Poly(num_of_term,sign,Terms);
			return a;
		}
		else //匹配不成功
		{
			System.out.println("The Polys are not in the right state.");
			return null;
		}
	}
	//在多项式的第index项插入项（如果已有项则覆盖）
	public void setTerm(int index,Term term)
	{
		this.Terms[index] = term;
	}
	//两项相加
	public static Poly ADD(Poly A,Poly B)
	{
		int i,j,flag;
		if(A.getSign()==2)
		{
			//把负号内移
			for(i=0;i<A.num_of_term;i++)
				A.getTermAt(i).Inverse();
		}
		if(B.getSign()==2)
		{
			//把负号内移
			for(i=0;i<B.num_of_term;i++)
				B.getTermAt(i).Inverse();
		}
		//现在开始加
		Poly result = A;
		result.sign = 1;
		for(i=0;i<B.num_of_term;i++)
		{
			flag = 0;
			for(j=0;j<A.num_of_term;j++)
			{
				//如果B中的项在A中有指数相同的项，则将两项相加
				if(A.getTermAt(j).getDeg()==B.getTermAt(i).getDeg())
				{
					int addCo = A.getTermAt(j).getCo()+B.getTermAt(i).getCo();
					Term t = new Term(addCo, A.getTermAt(j).getDeg());
					result.setTerm(j,t);
					flag = 1;
					break;
				}
			}
			if (flag == 0)//如果没有，则在最后插入B中的项
			{
				Term t = new Term(B.getTermAt(i).getCo(), B.getTermAt(i).getDeg());
				result.setTerm(result.num_of_term,t);
				result.num_of_term++;
			}
		}
		return result;
	}
	//冒泡排序时交换两个项
	public void swap(int i, int j)
	{
		Term t = new Term(0,0);
		t = this.Terms[i];
		this.Terms[i]=this.Terms[j];
		this.Terms[j]= t;
	}
	//输出
	public static void output(Poly A)
	{
		for(int i = 0;i<A.getNum();i++)
		{
			for(int j = i+1;j<A.getNum();j++)
			{
				if(A.getTermAt(i).getDeg()>A.getTermAt(j).getDeg())
					A.swap(i, j);
			}
		}
		System.out.println("The answer:");
		System.out.print("{");
		
		int k = 0;
		while(A.getTermAt(k)!=null&&A.getTermAt(k).getCo()==0)
			k++;
		if(k<A.num_of_term)
		{
			System.out.print("("+A.getTermAt(k).getCo()+","+A.getTermAt(k).getDeg()+")");
			k++;
			for(;k<A.num_of_term;k++)
			{
				if (A.getTermAt(k).getCo()!=0)
					System.out.print(",("+A.getTermAt(k).getCo()+","+A.getTermAt(k).getDeg()+")");
			}
		}
		
		System.out.print("}\n");
	}
	
	public static void main(String args[])
	{
		Poly[] a = new Poly[21];
		int flag = 0;
		//读入字符串
		Scanner s = new Scanner(System.in);
		String input ;	
		String[] inputs;
		System.out.println("Please enter the Polys:");
		input = s.nextLine();
		input = input.replace(" ", "");
		input = input.replace("	", ""); //Tab
		//boolean b = Pattern.matches("^((\\+|-)?\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\}((\\+|-)\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\}){0,})$", input);	
		//System.out.println(b);
	
			inputs = input.split("\\}");//将输入的字符串在"}"处分割，形成多项式
			for(int i = 0;i<inputs.length;i++)
			{
				inputs[i] = inputs[i]+"}";
			}
			if(inputs.length>20)
				System.out.println("You have input more than 20 Polys!");
			else
			{
				for (int i = 0;i < inputs.length;i++)
				{				
					if (ParsePoly(inputs[i],i)!=null)//逐个解析多项式
						a[i]=ParsePoly(inputs[i],i);
					else
					{
						flag = 1;
						break;
					}
				}
				/*for (int i = 0;i < inputs.length;i++)
				{
					System.out.println("Poly "+i+":");
					System.out.println("Its signal is "+((a[i].getSign()==1)?"+":"-"));
					System.out.println("It has "+a[i].getNum()+" terms.");
					for(int j = 0;j<a[i].getNum();j++)
					{
						System.out.print(a[i].getTermAt(j).getCo()+"x^"+a[i].getTermAt(j).getDeg()+","+"\n");
					}
				}*/
				if (flag==0)
				{
					for(int i = 1;i<inputs.length;i++)
					{
						a[0] = ADD(a[0],a[i]);
					}
					output(a[0]);
				}
			}	
		
	}
}