package elevator;

import java.util.Scanner;

public class Input extends Thread{
	private Requestlist RL;
	private boolean[] Button1;
	private boolean[] Button2;
	private boolean[] Button3;
	private boolean[][] Buttonf;
	Input(Requestlist rl)
	{
		this.RL = rl;
		this.Button1 = new boolean[21];
		this.Button2 = new boolean[21];
		this.Button3 = new boolean[21];
		this.Buttonf = new boolean[21][2];
		this.Buttonf = new boolean[21][2];
		for(int i = 1; i<=20; i++)
		{
			this.Button1[i] = false;
			this.Button2[i] = false;
			this.Button3[i] = false;
			this.Buttonf[i][0] = false;//UP
			this.Buttonf[i][1] = false;//DOWN
		}
	}
	public void setButton(int floor, int sel, boolean b)
	{
		if(sel==1)
			this.Button1[floor] = b;
		else if (sel==2)
			this.Button2[floor] = b;
		else if (sel==3)
			this.Button3[floor] = b;
		else if (sel==4)
			this.Buttonf[floor][0] = b;
		else if (sel==5)
			this.Buttonf[floor][1] = b;
	}
	public boolean getButton(int floor, int sel)
	{
		if(sel==1)
			return this.Button1[floor];
		else if (sel==2)
			return this.Button2[floor];
		else if (sel==3)
			return this.Button3[floor];
		else if (sel==4)
			return this.Buttonf[floor][0];
		else if (sel==5)
			return this.Buttonf[floor][1];
		return false;
	}
	public void run(){
		try{
			Scanner s = new Scanner(System.in);
			String input = " ";
			input = s.nextLine();
			EleSys.setTime(System.currentTimeMillis());
			int line = 1;
			Request temp = new Request();
			while (!input.equals("run") && line<=1000)
			{
				if(line==1001)
				{
					System.out.println("There are more than 1000 lines of request.");
				}
				else
				{	
					String[] Req = input.split("\\;");
					int valid = 1;
					for(int u=0;u<Req.length;u++){
						input = Req[u];
						temp = Request.ParseRequest(input,line,this);
						input = "["+input+","+EleSys.getTime()+"]";
						if(temp!=null)
						{	
							if(temp.getLine()==0)
								Output.Out(System.currentTimeMillis()+":SAME "+input);
							else
							{
								if(valid<=10)
								{	
						//			System.out.println("1 "+EleSys.getTime());
									RL.Add(temp);
						//			System.out.println("2 "+EleSys.getTime());
									valid++;
								}else{
									Output.Out(System.currentTimeMillis()+":INVALID "+input);
								}
							}
						//	System.out.println("size: "+RL.getsize()+" ");
						}
						else{
							Output.Out(System.currentTimeMillis()+":INVALID "+input);
						}
					}
					input = s.nextLine();
				}	
				line++;
			}
		}catch(Exception e){}
	}
}
