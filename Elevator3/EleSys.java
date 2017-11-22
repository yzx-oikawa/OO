package elevator;

import java.util.Scanner;

public class EleSys {
	private double SysTime;
	public void addTime(double t)
	{
		this.SysTime += t;
	}
	public double getTime()
	{
		return this.SysTime;	
	}
	public static void main(String args[])
	{
		EleSys elesys = new EleSys();
		
		int num_of_floor = 10;
		Floor f = new Floor(num_of_floor);
		Elevator e = new Elevator(1);
		Requestlist RL = new Requestlist();

		Scanner s = new Scanner(System.in);
		String input = "";
		input = s.nextLine();
		int line = 1;
		Request temp = new Request();
		while (!input.equals("run") && line<=1001)
		{
			if(line==1001)
			{
				System.out.println("There are more than 1000 lines of request.");
			}
			else
			{	
				temp = Request.ParseRequest(input,line);
				input = input.replace("(", "[");
				input = input.replace(")", "]");
				if(temp!=(null))
				{	
					RL.Add(temp);
				}
				else
					//System.out.println("Line "+line+ " of input is wrong.");
					System.out.println("INVALID "+input);
				input = s.nextLine();
			}	
			line++;
		}

		if(RL.getsize()==0)
			System.out.println("There is no valid move.");
		else
		{
			if(RL.getRequest(0).getEF()!=1 || RL.getRequest(0).getm()!=1 || RL.getRequest(0).getState()!=1||RL.getRequest(0).getT()!=0){
				System.out.println("INVALID "+RL.getRequest(0).getName());
				System.out.println("The first valid request is not (FR,1,UP,0)");
				System.out.println("The move is cancelled.");
			}
			else
			{
				//Remove the lines which contain time error.
				for(int i=0;i<RL.getsize()-1;i++)
				{
					if(RL.getRequest(i).getT()>RL.getRequest(i+1).getT())
					{
						System.out.println("INVALID "+RL.getRequest(i+1).getName()+" (TimeError)");
						RL.Remove(i+1);
						i--;
					}
				}
			
				//Remove the lines which happen simultaneously or have the same effect.
				for(int i=0;i<RL.getsize();i++)
				{
					for(int j=i+1;j<RL.getsize();j++)
					{
						if (NewDispatch.JudgeIfSimul(RL, i, j)==1)
						{
							RL.Remove(j);
							j--;
						}
					}
				}
			
				while(RL.getsize()!=0)
				{
					if(e.getCRsize()!=0)//get the first carried request as MainReq
					{
					//	System.out.println("size of carried request = "+e.getCRsize());
						e.setMainReq(e.getCR().get(0));
				//		System.out.println("MainfromCR: "+e.getMainReq().getName());
						e.getCR().remove(0);
					}
					else//get the first request as MainReq
					{
						e.setMainReq(RL.getRequest(0));
				//		System.out.println("Main: "+e.getMainReq().getName());
						if(RL.getRequest(0).getT()>elesys.getTime())
							elesys.SysTime = RL.getRequest(0).getT();
						RL.Remove(0);
					}
					e.setState();
					//get all carried request
					e.setcarriedReq(elesys, RL, e);
					for(int k=0;k<e.getCRsize();k++)
						for(int l=k+1;l<e.getCRsize();l++)
							e.sortCR(k, l);
					NewDispatch.EleRun(elesys, RL, e);
				}
				
				
			}
		}
	}
}

