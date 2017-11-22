package elevator; 

public class Floor {
	private boolean[][] floor;
	Floor (int n)
	{
		this.floor = new boolean[n+1][2];
		for(int i=1;i<=n;i++)
		{
			this.floor[i][0]=false;
			this.floor[i][1]=false;
		}
	}
	public boolean getFloor(int x, int state)
	{
		return this.floor[x][state];
	}
	public void setFloor(int x, int state) //1-UP -1-DOWN 0-STILL
	{
		this.floor[x][state] = true;
	}
	public void cancelFloor(int x, int state)
	{
		this.floor[x][state] = false;
	}
	/*public static void main(String args[])
	{
		Floor f = new Floor(10);
		for(int i=1;i<=10;i++)
			System.out.println(f.floor[i][0]+" "+f.floor[i][1]);
	}*/
}
