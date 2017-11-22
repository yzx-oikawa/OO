package elevator;

import java.util.Scanner;
import java.lang.Math;

public class EleSys {
	public static int JudgeIfSimul(Requestlist r, int i, int j)
	{
		if(r.getRequest(i).getEF()==1 && r.getRequest(j).getEF()==1){//FR
			if(r.getRequest(i).getm()==r.getRequest(j).getm()){
				if(r.getRequest(i).getT()==r.getRequest(j).getT()){
			        if(r.getRequest(i).getUD()==r.getRequest(j).getUD()){
						System.out.println("You pressed twice at the same button on Floor "+r.getRequest(i).getm()+".");
						return 1;
					}
				}
				else if(r.getRequest(i).getT()!=r.getRequest(j).getT()){
					if(r.getRequest(i).getUD()==r.getRequest(j).getUD()){
						if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getm()-(i==0?1:r.getRequest(i-1).getm()))*0.5+1){
							System.out.println("The request from Floor "+r.getRequest(i).getm()+" is invalid.");
							return 1;
						}
					}
				}
			}
		}
		else if(r.getRequest(i).getEF()==0 && r.getRequest(j).getEF()==0){//ER
			if(r.getRequest(i).getn()==r.getRequest(j).getn()){
				if(r.getRequest(i).getUD()==r.getRequest(j).getUD()){
					if(r.getRequest(i).getT()==r.getRequest(j).getT()){
						System.out.println("You pressed twice at the same button "+r.getRequest(i).getn()+" in the Elevator.");
						return 1;
					}
					else if(r.getRequest(i).getT()!=r.getRequest(j).getT()){
						if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getn()-(i==0?1:r.getRequest(i-1).getn()))*0.5+1){
							System.out.println("The request from the Elevator to Floor "+r.getRequest(j).getn()+" is invalid.");
							return 1;
						}
					}
				}
			}
		}
//		else if(r.getRequest(i).getEF()==1 && r.getRequest(j).getEF()==0){//i is FR and j is ER
//			if(r.getRequest(i).getm()==r.getRequest(j).getn()){
//				if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getm()-(i==0?1:r.getRequest(i-1).getm()))*0.5+1){
//					System.out.println("The request from the Elevator to Floor "+r.getRequest(j).getn()+" is invalid.");
//					return 1;
//				}
//			}
//		}
//		else if(r.getRequest(i).getEF()==0 && r.getRequest(j).getEF()==1){//i is ER and j is FR
//			if(r.getRequest(i).getn()==r.getRequest(j).getm()){
//				if(r.getRequest(j).getT()-r.getRequest(i).getT()<=Math.abs(r.getRequest(i).getn()-(i==0?1:r.getRequest(i-1).getn()))*0.5+1){
//					System.out.println("The request from Floor "+r.getRequest(j).getm()+" is invalid.");
//					return 1;
//				}
//			}
//		}
		return 0;
	}
	public static void main(String args[])
	{
		int num_of_floor = 10;
		Floor f = new Floor(num_of_floor);
		Elevator e = new Elevator(1, num_of_floor);
		Requestlist RequestList = new Requestlist();
		Dispatch[] dispatches = new Dispatch[100000];

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
				temp = Request.ParseRequest(input);
				if(temp!=(null))
				{	
					RequestList.Add(temp);
				}
				else
					System.out.println("Line "+line+ " of input is wrong.");
				input = s.nextLine();
			}	
			line++;
		}
		//RequestList.Output();
		if(RequestList.getsize()==0)
			System.out.println("There is no valid move.");
		else{
		if(RequestList.getRequest(0).getT()!=0){
			System.out.println("The first valid request doesn't begin at 0.");
			System.out.println("The move is cancelled.");
		}
		else{
		//Remove the lines which contain time error.
		int TimeError = 0;
		for(int i=0;i<RequestList.getsize()-1;i++)
		{
			if(RequestList.getRequest(i).getT()>RequestList.getRequest(i+1).getT())
			{
				RequestList.Remove(i+1);
				TimeError = 1;
				i--;
			}
		}
		if(TimeError==1)
			System.out.println("The lines with time error have been removed.");
//		else
//			System.out.println("The lines have no time error:D");
		//System.out.print("\n");
		//RequestList.Output();
		
		//Remove the lines which happen simultaneously or have the same effect.
		for(int i=0;i<RequestList.getsize();i++)
		{
			for(int j=i+1;j<RequestList.getsize();j++)
			{
				if (JudgeIfSimul(RequestList, i, j)==1)
				{
					RequestList.Remove(j);
					j--;
				}
			}
		}
		//System.out.print("\n");
		//RequestList.Output();
		
		double tsend=0, trun=0, tend=-1, d;
		int state, floor=1, j=0;
		String State;

		for(int i=0;i<RequestList.getsize();i++)
		{
			if(RequestList.getRequest(i).getEF()==1)//FR
			{
				//if(f.getFloor(RequestList.getRequest(i).getm(), RequestList.getRequest(i).getUD())==true)
				//	continue;
				//else
				//{
					//f.setFloor(RequestList.getRequest(i).getm(), RequestList.getRequest(i).getUD());//push the button
					tsend = (RequestList.getRequest(i).getT() > tend+1)?RequestList.getRequest(i).getT():tend+1;
					d = RequestList.getRequest(i).getm()-(i==0?1:dispatches[j-1].getFloor());
					trun = Math.abs(d)*0.5;
					tend = tsend + trun;
					state = (d<0?-1:(d==0?0:1));//1-UP -1-DOWN 0-STILL
					floor = RequestList.getRequest(i).getm();
					dispatches[j] =new Dispatch(tsend, trun, tend, floor, state);
					j++;
					
				//}
			}
			else if(RequestList.getRequest(i).getEF()==0)//ER
			{
				//if(e.getEleFloor(RequestList.getRequest(i).getn())==true)
				//	continue;
				//else
				//{
					//e.setFLoorButton(RequestList.getRequest(i).getn());//push the button
					tsend = (RequestList.getRequest(i).getT() > tend+1)?RequestList.getRequest(i).getT():tend+1;
					d = RequestList.getRequest(i).getn()-(i==0?1:dispatches[j-1].getFloor());
					trun = Math.abs(d)*0.5;
					tend = tsend + trun;
					state = (d<0?-1:(d==0?0:1));//1-UP -1-DOWN 0-STILL
					floor = RequestList.getRequest(i).getn();
					dispatches[j] = new Dispatch(tsend, trun, tend, floor, state);
					j++;
				//}
			}
		}

		if(dispatches[0]!=null)
		{
			System.out.println("The moves of the Elevator are:");
			for(int k=0;k<j;k++)
			{
				State = (dispatches[k].getState()==1?"UP":(dispatches[k].getState()==-1?"DOWN":"STILL"));
				System.out.print("(");
				System.out.print(dispatches[k].getFloor()+",");
				System.out.print(State+",");
				System.out.print(dispatches[k].getState()==0?dispatches[k].getTend()+1:dispatches[k].getTend());
				//System.out.printf("%.2f",dispatches[k].getTend());
				System.out.print(")\n");
			}
		}
		else
			System.out.println("There is no valid move.");
	}
	}
	}
}

