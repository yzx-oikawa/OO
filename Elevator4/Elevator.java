package elevator;

import java.util.ArrayList;

public class Elevator extends Thread implements Move{
	private int PresentFloor;
	private int TargetFloor;
	private int state;
	private Requestlist ReqList;
	private Request mainReq;
	private ArrayList<Request> carriedReq;
	private int No;
	private int Exercise;
	private Input in;
	Elevator(Requestlist rl, int no, Input in)
	{
		this.PresentFloor = 1;
		this.state = 0;
		this.mainReq = null;
		this.ReqList = rl;
		ArrayList<Request> c = new ArrayList<Request>();
		this.carriedReq = c;
		this.No = no;
		this.Exercise = 0;
		this.in = in;
	}
	public int getPresentFloor(){return this.PresentFloor;}
	public void setPresentFloor(int x){this.PresentFloor = x;}
	public int getTargetFloor(){return this.TargetFloor;
	}
	@Override
	public int getstate(){
		return this.state;
	}
	public void setState(){
		int Pf = this.PresentFloor;
		int Tf = this.mainReq.getmn();
		if(Tf>Pf)
			this.state = 1;
		else if(Tf<Pf)
			this.state = -1;
		else if(Tf==Pf)
			this.state = 0;
		this.TargetFloor = Tf;
	}
	public Requestlist getReqList(){return this.ReqList;}
	public Request getMainReq(){return this.mainReq;}
	public void setMainReq (Request r){this.mainReq = r;}
	public ArrayList<Request> getCR(){return this.carriedReq;}
	public Request getcarriedReq(int index){return this.carriedReq.get(index);}
	public int getCRsize(){return this.carriedReq.size();}
	public void setcarriedReq(){this.carriedReq.addAll(this.getAll());}
	public void addcarriedReq(ArrayList<Request> list){this.carriedReq.addAll(list);}
	public int getExercise(){return this.Exercise;}
	public void ChangePresentFloor(Request r, int n)
	{
		if (r.getmn()-this.getPresentFloor()>0)//UP
			this.PresentFloor += n;
		else if(r.getmn()-this.getPresentFloor()<0)//DOWN
			this.PresentFloor -= n;
	}
	public void sortCR(int i, int j)
	{
		Request temp = new Request();
		if (this.getcarriedReq(i).getLine()>this.getcarriedReq(j).getLine())
		{
			temp.setRequest(this.getcarriedReq(i));
			this.getcarriedReq(i).setRequest(this.getcarriedReq(j));
			this.getcarriedReq(j).setRequest(temp);  
		}
		
	}
	public String toString()
	{
		String s = new String(this.PresentFloor+" "+this.TargetFloor+" "+this.state);
		return s;
	}
	public ArrayList<Request> getAll()
	{
		ArrayList<Request> list = new ArrayList<Request>();
		Request r = this.getMainReq();
		for(int i=0;i<this.ReqList.getsize();i++)
		{
	//		System.out.println(this.JudgeIfCarry(r, this.ReqList.getRequest(i)));
			if (this.JudgeIfCarry(r, this.ReqList.getRequest(i))>0)
			{
				list.add(this.ReqList.getRequest(i));
				this.ReqList.Remove(i);
				i--;
			}
		}
		return list;
	}
	
	public void EleRun()
	{
		int flag = 0;
		Request r = this.getMainReq();
		while(this.getPresentFloor()!=this.getTargetFloor())
		{
			flag = 0;
			for(int i=0;i<this.getCRsize();i++)//check if carried request is finished
			{
				if(this.getPresentFloor()==this.getcarriedReq(i).getmn())
				{
					Output.Out(System.currentTimeMillis()+":"+this.getcarriedReq(i).getName()+"/("+"#"+this.No+","+this.getcarriedReq(i).getmn()+","
							  +NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(this.getstate()==0?EleSys.getTime()+6:EleSys.getTime())+")");
					if(this.getcarriedReq(i).getEF()==0)//ER
						this.in.setButton(this.getcarriedReq(i).getmn(), this.getcarriedReq(i).getNo(), false);
					else if (this.getcarriedReq(i).getEF()==1)//FR
						this.in.setButton(this.getcarriedReq(i).getmn(), this.getcarriedReq(i).getState()==1?4:5, false);
					this.getCR().remove(i);
					i--;
					flag = 1;
				}
			}
			this.ChangePresentFloor(r, 1);
			this.Exercise ++;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			if(flag==1)
			{
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
				}
		//		for(int h=0;h<e.getCRsize();h++)
		//		System.out.println("CRbefore: "+e.getcarriedReq(h).getName());
			}
				this.getCR().addAll(this.getAll());
				for(int k=0;k<this.getCRsize();k++)
					for(int l=k+1;l<this.getCRsize();l++)
						this.sortCR(k, l);
		//		for(int h=0;h<e.getCRsize();h++)
		//		System.out.println("CRafter: "+e.getcarriedReq(h).getName());
			
		}
	//	System.out.print(this.getMainReq().getName()+"/#"+this.No);
	//	System.out.println("("+this.getMainReq().getmn()+","+NewDispatch.toString(this.getstate())+","+(this.getstate()==0?EleSys.getTime()+1:EleSys.getTime())+")");
		int stillm = 0;
		String stilloutputm = "123";
		if(this.state==0){
			stillm=1;
			stilloutputm = ":"+this.getMainReq().getName()+"/("+"#"+this.No+","+this.getMainReq().getmn()+","
					+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime()+6)+")";
		}else{
	//		System.out.println(System.currentTimeMillis()+":"+this.getMainReq().getName()+"/("+"#"+this.No+","+this.getMainReq().getmn()+","
	//				+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime())+")");
			Output.Out(System.currentTimeMillis()+":"+this.getMainReq().getName()+"/("+"#"+this.No+","+this.getMainReq().getmn()+","
					+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime())+")");
		}
		if(this.getMainReq().getEF()==0)//ER
			this.in.setButton(this.getMainReq().getmn(), this.getMainReq().getNo(), false);
		else if (this.getMainReq().getEF()==1)//FR
			this.in.setButton(this.getMainReq().getmn(), this.getMainReq().getState()==1?4:5, false);
		this.setMainReq(null);
		int stillc = 0;
		String stilloutputc = ""; 
		if(this.getPresentFloor()==this.getTargetFloor())//the main request has the same effect as a carried request
		{
			for(int i=0;i<this.getCRsize();i++)//check if carried request is finished
			{
				if(this.getPresentFloor()==this.getcarriedReq(i).getmn())
				{
		//			System.out.print(this.getcarriedReq(i).getName()+"/#"+this.No);
		//			System.out.println(/*"CR:*/"("+this.getcarriedReq(i).getmn()+","+NewDispatch.toString(this.getstate())+","+(this.getstate()==0?EleSys.getTime()+1:EleSys.getTime())+")");
		//			System.out.println(rl.getsize());
					if(this.getstate()==0){//STILL
						stillc = 1;
						stilloutputc = ":"+this.getcarriedReq(i).getName()+"/("+"#"+this.No+","+this.getcarriedReq(i).getmn()+","
								+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime()+6)+")";
					}else{
			//			System.out.println(System.currentTimeMillis()+":"+this.getcarriedReq(i).getName()+"/("+"#"+this.No+","+this.getcarriedReq(i).getmn()+","
			//						+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime())+")");
						Output.Out(System.currentTimeMillis()+":"+this.getcarriedReq(i).getName()+"/("+"#"+this.No+","+this.getcarriedReq(i).getmn()+","
									+NewDispatch.toString(this.getstate())+","+this.getExercise()+","+(EleSys.getTime())+")");
						
					}
					if(this.getcarriedReq(i).getEF()==0)//ER
						this.in.setButton(this.getcarriedReq(i).getmn(), this.getcarriedReq(i).getNo(), false);
					else if (this.getcarriedReq(i).getEF()==1)//FR
						this.in.setButton(this.getcarriedReq(i).getmn(), this.getcarriedReq(i).getState()==1?4:5, false);
					this.getCR().remove(i);
					i--;
				}
			}
			this.getCR().addAll(this.getAll());
			for(int k=0;k<this.getCRsize();k++)
				for(int l=k+1;l<this.getCRsize();l++)
					this.sortCR(k, l);
			
		}
		try {
			Thread.sleep(6000);
			if(stillm == 1){
		//		System.out.println(System.currentTimeMillis()+stilloutputm);
				Output.Out(System.currentTimeMillis()+stilloutputm);
			}
			if(stillc == 1){
		//		System.out.println(System.currentTimeMillis()+stilloutputc);
				Output.Out(System.currentTimeMillis()+stilloutputc);
			}
		} catch (InterruptedException e) {
		}
	}
	public int JudgeIfEmpty()
	{
		if(this.mainReq==null && this.getCRsize()==0 && this.ReqList.getsize()==0)
			return 0;
		return 1;
	}
	public int JudgeIfCarry(Request a, Request b)//b is carried by a
	{
		double Trun = Math.abs(b.getmn()-this.getPresentFloor())*3;
		if(EleSys.getTime() + Trun > b.getT())
		{
			if(b.getEF()==1){//FR
				if(b.getState()==this.getstate())
				{
					if(b.getState()==1){//UP
						if(this.getPresentFloor()<b.getmn()&&b.getmn()<=a.getmn())
							return 1;
					}
					else if(b.getState()==-1){//DOWN
						if(this.getPresentFloor()>b.getmn()&&b.getmn()>=a.getmn())
							return 2;
					}
				}	
			}
			else if	(b.getEF()==0){//ER
				if(this.getstate()==1){//UP
					if(this.getPresentFloor()<b.getmn())
						return 3;
				}
				else if(this.getstate()==-1){//DOWN
					if(this.getPresentFloor()>b.getmn())
						return 4;
				}
			}
		}
		return 0;	
	}
	public void run(){
		try
		{
			while (true){
				while(this.ReqList.getsize()!=0 || this.getCRsize()!=0){
				if(this.getCRsize()!=0)//get the first carried request as MainReq
				{
				//	System.out.println("size of carried request = "+e.getCRsize());
					this.setMainReq(this.getCR().get(0));
				//	System.out.println("MainfromCR: "+e.getMainReq().getName());
					this.getCR().remove(0);
				}
				else //get the first request as MainReq
				{
					this.setMainReq(this.ReqList.getRequest(0));
				//	System.out.println("Main: "+e.getMainReq().getName());
					this.ReqList.Remove(0);
				}
				this.setState();
				//get all carried request
					this.setcarriedReq();
					for(int k=0;k<this.getCRsize();k++)
						for(int l=k+1;l<this.getCRsize();l++)
							this.sortCR(k, l);
				this.EleRun();
				}
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){}
			}
			
		}
		catch(Exception e){}
	}
}

