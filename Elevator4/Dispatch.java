package elevator;

public class Dispatch extends Thread{
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

}
