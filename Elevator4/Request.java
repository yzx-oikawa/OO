package elevator;

import java.util.regex.Pattern;

public class Request{
	private int ERorFR; //0-ER 1-FR
	private int UPorDOWN; //1-UP -1-DOWN
	private int Tf;//target floor
	private double T;//time when send the request
	private String name;
	private int line;
	private int No;
	Request(){}
	Request(int ERorFR, int UPorDown, int Tf, double T, String name, int line, int No)//FR
	{
		this.ERorFR = ERorFR;
		this.UPorDOWN = UPorDown;
		this.Tf = Tf;
		this.T = T;
		this.name = name;
		this.line = line;
		this.No = No;
	}
	Request(int ERorFR, int Tf, double T, String name, int line, int No)//ER
	{
		this.ERorFR = ERorFR;
		this.Tf = Tf;
		this.T = T;
		this.name = name;
		this.line = line;
		this.No = No;
	}
	public int getEF(){return ERorFR;}
	public int getState(){return UPorDOWN;}
	public int getmn(){return Tf;}
	public double getT(){return T;}
	public String getName(){return this.name;}
	public int getLine(){return this.line;}
	public void setLine(int line){this.line = line;}
	public int getNo(){return this.No;}
	public void setNo(int no){this.No = no;}
	public void setRequest(Request r)
	{
		this.ERorFR = r.ERorFR;
		this.UPorDOWN = r.UPorDOWN;
		this.Tf = r.Tf;
		this.T = r.T;
		this.name = r.name;
		this.line = r.line;
	}
	public static Request ParseRequest(String input, int line, Input in)
	{
		input = input.replace(" ", "");
		String temp = input;
		if(Pattern.matches("^(\\(FR\\,\\+?\\d{1,2}\\,(UP||DOWN)\\))$", input)==true)
		{
			double T = EleSys.getTime();
			input = input.replace("(", "");
			input = input.replace(")", "");
			input = input.replace("FR,","");
			if(input.indexOf("UP")!=-1)
			{
				input = input.replace("UP,","");
				String[] inputs = input.split("\\,");
				int UPorDOWN = 1;
				int m = Integer.parseInt(inputs[0]);
			//	System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m>=20 || T>2147483647)
				{
					return null;
				}
				else
				{
					temp = "["+temp+","+Double.toString(T)+"]";
					if(in.getButton(m, 4)==false){
						in.setButton(m, 4, true);
						Request newrequest = new Request(1,UPorDOWN,m,T,temp,line,0);
						return newrequest;
					}
					else if(in.getButton(m, 4)==true){
						Request newrequest = new Request(1,UPorDOWN,m,T,temp,0,0);
						return newrequest;
					}
				}
			}
			else if(input.indexOf("DOWN")!=-1)
			{
				input = input.replace("DOWN,","");
				String[] inputs = input.split("\\,");
				int UPorDOWN = -1;
				int m = Integer.parseInt(inputs[0]);
			//	System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m==1 || m>20 || T>2147483647)
				{
					return null;
				}
				else
				{
					temp = "["+temp+","+Double.toString(T)+"]";
					if(in.getButton(m, 5)==false){
						in.setButton(m, 5, true);
						Request newrequest = new Request(1,UPorDOWN,m,T,temp,line,0);
						return newrequest;
					}
					else if(in.getButton(m, 5)==true){
						Request newrequest = new Request(1,UPorDOWN,m,T,temp,0,0);
						return newrequest;
					}
				}
			}
		}
		else if(Pattern.matches("^(\\(ER\\,\\#\\+?\\d\\,\\+?\\d{1,2}\\))$", input)==true)
		{
			double T = EleSys.getTime();
			input = input.replace("(", "");
			input = input.replace(")", "");
			input = input.replace("ER,","");
			input = input.replace("#", "");
			String[] inputs = input.split("\\,");
			int no = Integer.parseInt(inputs[0]);
			int n = Integer.parseInt(inputs[1]);
		//	System.out.println(n+" "+T);
			if(n==0 || n>20 || T>2147483647 || no==0 || no>3)
			{
				return null;
			}
			else
			{
				temp = "["+temp+","+Double.toString(T)+"]";
				if(in.getButton(n, no)==false){
					in.setButton(n, no, true);
					Request newrequest = new Request(0,n,T,temp,line,no);
					return newrequest;
				}
				else if(in.getButton(n, no)==true){
					Request newrequest = new Request(0,n,T,temp,0,no);
					return newrequest;
				}
			}
		}
		return null;
	}
	
	
}
