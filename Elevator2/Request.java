package elevator;

import java.util.regex.Pattern;

public class Request {
	private int ERorFR; //0-ER 1-FR
	private int UPorDOWN; //1-UP -1-DOWN
	private int m;//target floor when FR
	private int n;//target floor when ER
	private double T;//time when send the request
	Request(){}
	Request(int ERorFR, int UPorDown, int m, double T)//FR
	{
		this.ERorFR = ERorFR;
		this.UPorDOWN = UPorDown;
		this.m = m;
		this.T = T;
	}
	Request(int ERorFR, int n, double T)//ER
	{
		this.ERorFR = ERorFR;
		this.n = n;
		this.T = T;
	}
	public int getEF(){return ERorFR;}
	public int getUD(){return UPorDOWN;}
	public int getm(){return m;}
	public int getn(){return n;}
	public double getT(){return T;}
	/*public static void main(String args[])
	{
		Request r1 = new Request(0,1,1,1.1);
		Request r2 = new Request(1,3,3.4);
		System.out.println("r1: "+r1.ERorFR+" "+r1.UPorDOWN+" "+r1.m+" "+r1.T);
		System.out.println("r2: "+r2.ERorFR+" "+r2.n+" "+r2.T);
	}*/
	public static Request ParseRequest(String input)
	{
		input = input.replace(" ", "");
		//\\(FR\\,\\d{1,2}\\,(UP||DOWN)\\,\\d{1,9}\\)
		//\\(ER\\,\\d{1,2}\\,\\d{1,9}\\)
		if(Pattern.matches("^(\\(FR\\,\\d{1,2}\\,(UP||DOWN)\\,\\d{1,9}\\))$", input)==true)
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
				int T = Integer.parseInt(inputs[1]);
				//System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m>=10)
				{
					return null;
				}
				else
				{
					Request newrequest = new Request(1,UPorDOWN,m,(double)T);
					return newrequest;
				}
			}
			else if(input.indexOf("DOWN")!=-1)
			{
				input = input.replace("DOWN,","");
				String[] inputs = input.split("\\,");
				int UPorDOWN = -1;
				int m = Integer.parseInt(inputs[0]);
				int T = Integer.parseInt(inputs[1]);
				//System.out.println(UPorDOWN+" "+m+" "+T);
				if(m==0 || m==1 || m>10)
				{
					return null;
				}
				else
				{
					Request newrequest = new Request(1,UPorDOWN,m,(double)T);
					return newrequest;
				}
			}

		}
		else if(Pattern.matches("^(\\(ER\\,\\d{1,2}\\,\\d{1,9}\\))$", input)==true)
		{
			input = input.replace("(", "");
			input = input.replace(")", "");
			input = input.replace("ER,","");
			String[] inputs = input.split("\\,");
			int n = Integer.parseInt(inputs[0]);
			int T = Integer.parseInt(inputs[1]);
			//System.out.println(n+" "+T);
			if(n==0 || n>10)
			{
				return null;
			}
			else
			{
				Request newrequest = new Request(0,n,(double)T);
				return newrequest;
			}
		}
		//System.out.println("The input is wrong.");
		return null;
	}
}
