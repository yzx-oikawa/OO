package elevator;

public class EleSys{
	public static int first;
	public static long time;
	public static long Tstart;
	public static double getTime() {
		long temp = (System.currentTimeMillis()-Tstart)/100;
		return (double)(temp)/10;
	}
	
	public static void setTime(long t) {
		Tstart = t;
	}
	public static int getFirst(){return first;}
	public static void setFirst(){first = 1;}
	
	public static void main(String args[])
	{	
		try{
			Output.Out(System.currentTimeMillis()+":Start!");
			first = 0;
		
			Requestlist RL = new Requestlist("RL");
			Requestlist RL1 = new Requestlist("RL1");
			Requestlist RL2 = new Requestlist("RL2");
			Requestlist RL3 = new Requestlist("RL3");
			Thread in = new Input(RL);
			Thread e1 = new Elevator(RL1,1,(Input)in);
			Thread e2 = new Elevator(RL2,2,(Input)in);
			Thread e3 = new Elevator(RL3,3,(Input)in);
			Thread d = new NewDispatch(RL, (Elevator)e1, (Elevator)e2, (Elevator)e3);
			in.start();
			e1.start();
			e2.start();
			e3.start();
			d.start();
		}catch(Exception e){}
		
	}
}

