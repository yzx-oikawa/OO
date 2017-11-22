package elevator;

import java.util.regex.Pattern;

public class Request implements Move{
	private int ERorFR; //0-ER 1-FR
	private int UPorDOWN; //1-UP -1-DOWN
	private int m;//target floor when FR
	private int n;//target floor when ER
	private double T;//time when send the request
	private String name;
	private int line;
	Request(){}
	Request(int ERorFR, int UPorDown, int m, double T, String name, int line)//FR
	{
		this.ERorFR = ERorFR;
		this.UPorDOWN = UPorDown;
		this.m = m;
		this.T = T;
		this.name = name;
		this.line = line;
	}
	Request(int ERorFR, int n, double T, String name, int line)//ER
	{
		this.ERorFR = ERorFR;
		this.n = n;
		this.T = T;
		this.name = name;
		this.line = line;
	}
	public int getEF(){return ERorFR;}
	public int getState(){return UPorDOWN;}
	public int getm(){return m;}
	public int getn(){return n;}
	public int getmn(){
		if (this.getEF()==1)
			return m;
		else if (this.getEF()==0)
			return n;
		return 0;
	}
	public double getT(){return T;}
	public String getName(){return this.name;}
	public int getLine(){return line;}
	public void setLine(int line){this.line = line;}
	public void setRequest(Request r)
	{
		this.ERorFR = r.ERorFR;
		this.UPorDOWN = r.UPorDOWN;
		this.m = r.m;
		this.n = r.n;
		this.T = r.T;
		this.name = r.name;
		this.line = r.line;
	}
	public static Request ParseRequest(String input, int line)
	{
		input = input.replace(" ", "");
		//\\(FR\\,\\d{1,2}\\,(UP||DOWN)\\,\\d{1,9}\\)
		//\\(ER\\,\\d{1,2}\\,\\d{1,9}\\)
		String temp = input;
		temp = temp.replace("(", "[");
		temp = temp.replace(")", "]");
		if(Pattern.matches("^(\\(FR\\,\\+?\\d{1,2}\\,(UP||DOWN)\\,\\+?\\d{1,10}\\))$", input)==true)
		{
			input = input.replace("(", "");
			input = input.replace(")", "");
			input = input.replace("FR,","");
			if(input.indexOf("UP")!=-1)
			{
				input = input.replace("UP,","");
				String[] inputs = input.split("\\,");
				int UPorDOWN = 1;
				int m = Integer.parseInt(inputs[0]);
				double T = Double.parseDouble(inputs[1]);
				//System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m>=10 || T>2147483647)
				{
					return null;
				}
				else
				{
					Request newrequest = new Request(1,UPorDOWN,m,T,temp,line);
					return newrequest;
				}
			}
			else if(input.indexOf("DOWN")!=-1)
			{
				input = input.replace("DOWN,","");
				String[] inputs = input.split("\\,");
				int UPorDOWN = -1;
				int m = Integer.parseInt(inputs[0]);
				double T = Double.parseDouble(inputs[1]);
				//System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m==1 || m>10 || T>2147483647)
				{
					return null;
				}
				else
				{
					Request newrequest = new Request(1,UPorDOWN,m,T,temp,line);
					return newrequest;
				}
			}
		}
		else if(Pattern.matches("^(\\(ER\\,\\+?\\d{1,2}\\,\\+?\\d{1,10}\\))$", input)==true)
		{
			input = input.replace("(", "");
			input = input.replace(")", "");
			input = input.replace("ER,","");
			String[] inputs = input.split("\\,");
			int n = Integer.parseInt(inputs[0]);
			double T = Double.parseDouble(inputs[1]);
			//System.out.println(n+" "+T);
			if(n==0 || n>10 || T>2147483647)
			{
				return null;
			}
			else
			{
				Request newrequest = new Request(0,n,T,temp,line);
				return newrequest;
			}
		}
		return null;
	}
	public static int JudgeIfCarry(EleSys es, Elevator e, Request a, Request b)//b is carried by a
	{
//		System.out.println(e.getPresentFloor()+" "+e.getState()+" "+r.getState()+" "+r.getmn()+" "+rl.getRequest(i).getState()+" "+rl.getRequest(i).getmn());
//		System.out.println(r.getmn()+" "+rl.getRequest(i).getT()+" "+es.getTime());
		double Trun = Math.abs(b.getmn()-e.getPresentFloor())*0.5;
		if(es.getTime() + Trun > b.getT())
		{
			if(b.getEF()==1){//FR
				if(b.getState()==e.getState())
				{
					if(b.getState()==1){//UP
						if(e.getPresentFloor()<b.getmn()&&b.getmn()<=a.getmn())
							return 1;
					}
					else if(b.getState()==-1){//DOWN
						if(e.getPresentFloor()>b.getmn()&&b.getmn()>=a.getmn())
							return 2;
					}
				}	
			}
			else if	(b.getEF()==0){//ER
				if(e.getState()==1){//UP
					if(e.getPresentFloor()<b.getmn())
						return 3;
				}
				else if(e.getState()==-1){//DOWN
					if(e.getPresentFloor()>b.getmn())
						return 4;
				}
			}
		}
		return 0;	
	}
}
