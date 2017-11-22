package elevator;

import java.util.ArrayList;

public class NewDispatch extends Dispatch{ 
	NewDispatch(){};
	public static String toString(int n)
	{
		if (n==1)
			return "UP";
		else if (n==-1)
			return "DOWN";
		else if (n==0)
			return "STILL";
		return " ";
	}
	public static ArrayList<Request> getAll(EleSys es, Requestlist rl, Elevator e)
	{
		ArrayList<Request> list = new ArrayList<Request>();
		Request r = e.getMainReq();
		for(int i=0;i<rl.getsize();i++)
		{
			if (Request.JudgeIfCarry(es, e, r, rl.getRequest(i))>0)
			{
				list.add(rl.getRequest(i));
		//		System.out.println(" "+Request.JudgeIfCarry(es, e, r, rl.getRequest(i)));
				rl.Remove(i);
				i--;
			}
		}
		return list;
	}
	public static void EleRun(EleSys es, Requestlist rl, Elevator e)
	{
		
		
		
		int flag = 0;
		Request r = e.getMainReq();
		while(e.getPresentFloor()!=e.getTargetFloor())
		{
			flag = 0;
		//	System.out.println("State of elevator: "+e.toString());
		//	System.out.println("size of Carried Request:"+e.getCRsize());
			for(int i=0;i<e.getCRsize();i++)//check if carried request is finished
			{
				if(e.getPresentFloor()==e.getcarriedReq(i).getmn())
				{
					System.out.print(e.getcarriedReq(i).getName()+"/");
					System.out.println(/*"CR:+*/"("+e.getcarriedReq(i).getmn()+","+NewDispatch.toString(e.getState())+","+(e.getState()==0?es.getTime()+1:es.getTime())+")");
		//			System.out.println(rl.getsize());
					e.getCR().remove(i);
					i--;
					flag = 1;
				}
			}
			e.ChangePresentFloor(e, r, 1);
			es.addTime(0.5);
			if(flag==1)
			{
				es.addTime(1);
		//		for(int h=0;h<e.getCRsize();h++)
		//		System.out.println("CRbefore: "+e.getcarriedReq(h).getName());
				e.getCR().addAll(NewDispatch.getAll(es, rl, e));
				for(int k=0;k<e.getCRsize();k++)
					for(int l=k+1;l<e.getCRsize();l++)
						e.sortCR(k, l);
		//		for(int h=0;h<e.getCRsize();h++)
		//		System.out.println("CRafter: "+e.getcarriedReq(h).getName());
			}
		}
		System.out.print(e.getMainReq().getName()+"/");
		System.out.println("("+e.getMainReq().getmn()+","+NewDispatch.toString(e.getState())+","+(e.getState()==0?es.getTime()+1:es.getTime())+")");
		if(e.getPresentFloor()==e.getTargetFloor())//the main request has the same effect as a carried request
		{
			for(int i=0;i<e.getCRsize();i++)//check if carried request is finished
			{
				if(e.getPresentFloor()==e.getcarriedReq(i).getmn())
				{
					System.out.print(e.getcarriedReq(i).getName()+"/");
					System.out.println(/*"CR:*/"("+e.getcarriedReq(i).getmn()+","+NewDispatch.toString(e.getState())+","+(e.getState()==0?es.getTime()+1:es.getTime())+")");
		//			System.out.println(rl.getsize());
					e.getCR().remove(i);
					i--;
					flag = 1;
				}
			}
			if (flag==1)
			{
				e.getCR().addAll(NewDispatch.getAll(es, rl, e));
				for(int k=0;k<e.getCRsize();k++)
					for(int l=k+1;l<e.getCRsize();l++)
						e.sortCR(k, l);
			}
		}
		es.addTime(1); 
	}
	public static int JudgeIfSimul(Requestlist r, int i, int j)
	{
		if(r.getRequest(i).getEF()==1 && r.getRequest(j).getEF()==1){//FR
			if(r.getRequest(i).getm()==r.getRequest(j).getm()){
				if(r.getRequest(i).getT()==r.getRequest(j).getT()){
			        if(r.getRequest(i).getState()==r.getRequest(j).getState()){
						//System.out.println("You pressed twice at the same button on Floor "+r.getRequest(i).getm()+".");
						System.out.println("SAME "+r.getRequest(j).getName());
						return 1;
					}
				}
				else if(r.getRequest(i).getT()!=r.getRequest(j).getT()){
					if(r.getRequest(i).getState()==r.getRequest(j).getState()){
						if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getm()-(i==0?1:r.getRequest(i-1).getm()))*0.5+1){
							//System.out.println("The request from Floor "+r.getRequest(i).getm()+" is invalid.");
							System.out.println("INVALID "+r.getRequest(j).getName());
							return 1;
						}
					}
				}
			}
		}
		else if(r.getRequest(i).getEF()==0 && r.getRequest(j).getEF()==0){//ER
			if(r.getRequest(i).getn()==r.getRequest(j).getn()){
				if(r.getRequest(i).getState()==r.getRequest(j).getState()){
					if(r.getRequest(i).getT()==r.getRequest(j).getT()){
						//System.out.println("You pressed twice at the same button "+r.getRequest(i).getn()+" in the Elevator.");
						System.out.println("SAME "+r.getRequest(j).getName());
						return 1;
					}
					else if(r.getRequest(i).getT()!=r.getRequest(j).getT()){
						if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getn()-(i==0?1:r.getRequest(i-1).getn()))*0.5+1){
							//System.out.println("The request from the Elevator to Floor "+r.getRequest(j).getn()+" is invalid.");
							System.out.println("INVALID "+r.getRequest(j).getName());
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}
}
