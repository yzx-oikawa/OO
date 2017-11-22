package elevator;

public class NewDispatch extends Dispatch{ 
	private Elevator E1, E2, E3;
	private Requestlist RL;
	NewDispatch(Requestlist rl, Elevator e1, Elevator e2, Elevator e3)
	{
		this.RL = rl;
		this.E1 = e1;
		this.E2 = e2;
		this.E3 = e3;
	}
	public int JudgeEle(Requestlist rl, Elevator e1, Elevator e2, Elevator e3)
	{
		Request r = new Request();
		r = rl.getRequest(0);
		//ER
		if(r.getNo()==1) return 1;
		else if(r.getNo()==2) return 2;
		else if(r.getNo()==3) return 3;
		//FR
		else if(r.getNo()==0)
		{
			Request m1 = e1.getMainReq();
			Request m2 = e2.getMainReq();
			Request m3 = e3.getMainReq();
			
			int j1 = m1==null? 0 : e1.JudgeIfCarry(e1.getMainReq(),r);
		//	System.out.println(e1.getMainReq().getName()+" "+r.getName());
			int j2 = m2==null? 0 : e2.JudgeIfCarry(e2.getMainReq(),r);
			int j3 = m3==null? 0 : e3.JudgeIfCarry(e3.getMainReq(),r);
			
			int ex1 = e1.getExercise();
			int ex2 = e2.getExercise();
			int ex3 = e3.getExercise();
			
			int em1 = e1.JudgeIfEmpty();//0-empty
			int em2 = e2.JudgeIfEmpty();
			int em3 = e3.JudgeIfEmpty();
			
		//	System.out.println(m1+" "+j1+" "+j2+" "+j3+" "+ex1+" "+ex2+" "+ex3+" "+em1+" "+em2+" "+em3);
			if(j1>0 && j2>0 && j3>0)
			{
				if(ex1<=ex2 && ex1<=ex3) return 1;
				else if(ex2<=ex1 && ex2<=ex3) return 2;
				else if(ex3<=ex1 && ex3<=ex2) return 3;
			}
			else if(j1>0 && j2>0 && j3==0)
			{
				if(ex1<=ex2) return 1;
				else return 2;
			}
			else if(j1>0 && j2==0 && j3>0)
			{
				if(ex3<=ex1) return 3;
				else return 1;
			}
			else if(j1==0 && j2>0 && j3>0)
			{
				if(ex2<=ex3) return 2;
				else return 3;
			}
			else if(j1>0 && j2==0 && j3==0) return 1;
			else if(j1==0 && j2>0 && j3==0) return 2;
			else if(j1==0 && j2==0 && j3>0) return 3;
			else if(j1==0 && j2==0 && j3==0)
			{
				if(em1!=0 && em2!=0 && em3!=0) return 0;
				else if(em1==0 && em2!=0 && em3!=0) return 1;
				else if(em1!=0 && em2==0 && em3!=0) return 2;
				else if(em1!=0 && em2!=0 && em3==0) return 3;
				else if(em1==0 && em2==0 && em3!=0){
					if(ex1<=ex2) return 1;
					else return 2;
				}
				else if(em1==0 && em2!=0 && em3==0){
					if(ex3<=ex1) return 3;
					else return 1;
				}
				else if(em1!=0 && em2==0 && em3==0){
					if(ex2<=ex3) return 2;
					else return 3;
				}
				else if(em1==0 && em2==0 && em3==0){
					if(ex1<=ex2 && ex1<=ex3) return 1;
					else if(ex2<=ex3 && ex2<=ex1) return 2;
					else if(ex3<=ex1 && ex3<=ex2) return 3;
				}
			}

		}
		return 0;
	}
	public void run()
	{
		try{
		while (true){
			while(RL.getsize()!=0){
			//	System.out.println(System.currentTimeMillis());
				int flag = this.JudgeEle(this.RL, this.E1, this.E2, this.E3);
			//	System.out.println(System.currentTimeMillis());
		//		System.out.println(RL.getsize());
		//		System.out.println(flag);
				if (flag==1) 
				{
					this.E1.getReqList().Add(RL.getRequest(0));
					RL.Remove(0);
				 }
				else if (flag==2)
				{
					this.E2.getReqList().Add(RL.getRequest(0));
					RL.Remove(0);
				}
				else if (flag==3)
				{
					this.E3.getReqList().Add(RL.getRequest(0));
					RL.Remove(0);
				}
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){}
			}
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){}
		}
		}catch(Exception e){}
		
	}
}
