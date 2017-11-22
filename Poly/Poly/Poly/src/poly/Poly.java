package poly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Poly {
	//����
	private int num_of_term; //����
	private int sign = 1;//����ʽ֮��ķ��� 1+ 2-
	private Term [] Terms;
	//���캯��
	Poly(int num_of_term,int sign,Term [] Terms)
	{
		this.num_of_term = num_of_term;
		this.sign = sign;
		this.Terms = Terms;
	}
	//���ú���
	public int getSign(){return this.sign;}
	public int getNum(){return this.num_of_term;}
	public Term getTermAt(int i){return this.Terms[i];}
	//��������ʽ
	public static Poly ParsePoly(String inputs,int index)
	{
		int sign = 1;
		int num_of_term = 0;
		Term[] Terms = new Term[1001];
		inputs = inputs.replace(" ", "");
		inputs = inputs.replace("	", "");
		//System.out.println(inputs);
		//������ʽƥ��
		boolean b;
		if(index==0)
			b = Pattern.matches("^((\\+|-)?\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\})$", inputs);	
		else
			b = Pattern.matches("^((\\+|-)\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\})$", inputs);	
		
		if(b) //���ƥ��ɹ�
		{
				if(inputs.charAt(0)=='-')
			{	
				sign = 2;
				inputs = inputs.substring(1, inputs.length());//ȥ����һ���ַ�
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
			String[] Term = inputs.split("\\,");//��","���ָ�γ���
			for (int i=0;i<Term.length;i=i+2)
			{
				//System.out.println(Integer.parseInt(Term[i])+",");
				Terms[i/2] = new Term(Integer.parseInt(Term[i]),Integer.parseInt(Term[i+1]));
				//Interger.parseInt������ȡ�ַ����е�����
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
		else //ƥ�䲻�ɹ�
		{
			System.out.println("The Polys are not in the right state.");
			return null;
		}
	}
	//�ڶ���ʽ�ĵ�index����������������򸲸ǣ�
	public void setTerm(int index,Term term)
	{
		this.Terms[index] = term;
	}
	//�������
	public static Poly ADD(Poly A,Poly B)
	{
		int i,j,flag;
		if(A.getSign()==2)
		{
			//�Ѹ�������
			for(i=0;i<A.num_of_term;i++)
				A.getTermAt(i).Inverse();
		}
		if(B.getSign()==2)
		{
			//�Ѹ�������
			for(i=0;i<B.num_of_term;i++)
				B.getTermAt(i).Inverse();
		}
		//���ڿ�ʼ��
		Poly result = A;
		result.sign = 1;
		for(i=0;i<B.num_of_term;i++)
		{
			flag = 0;
			for(j=0;j<A.num_of_term;j++)
			{
				//���B�е�����A����ָ����ͬ������������
				if(A.getTermAt(j).getDeg()==B.getTermAt(i).getDeg())
				{
					int addCo = A.getTermAt(j).getCo()+B.getTermAt(i).getCo();
					Term t = new Term(addCo, A.getTermAt(j).getDeg());
					result.setTerm(j,t);
					flag = 1;
					break;
				}
			}
			if (flag == 0)//���û�У�����������B�е���
			{
				Term t = new Term(B.getTermAt(i).getCo(), B.getTermAt(i).getDeg());
				result.setTerm(result.num_of_term,t);
				result.num_of_term++;
			}
		}
		return result;
	}
	//ð������ʱ����������
	public void swap(int i, int j)
	{
		Term t = new Term(0,0);
		t = this.Terms[i];
		this.Terms[i]=this.Terms[j];
		this.Terms[j]= t;
	}
	//���
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
		//�����ַ���
		Scanner s = new Scanner(System.in);
		String input ;	
		String[] inputs;
		System.out.println("Please enter the Polys:");
		input = s.nextLine();
		input = input.replace(" ", "");
		input = input.replace("	", ""); //Tab
		//boolean b = Pattern.matches("^((\\+|-)?\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\}((\\+|-)\\{((\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\))(,\\(((\\+|-)?\\d{1,6},\\+?\\d{1,6})\\)?){0,})\\}){0,})$", input);	
		//System.out.println(b);
	
			inputs = input.split("\\}");//��������ַ�����"}"���ָ�γɶ���ʽ
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
					if (ParsePoly(inputs[i],i)!=null)//�����������ʽ
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