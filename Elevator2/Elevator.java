package elevator;

public class Elevator {
	private int PresentFloor;
	private boolean[] floorbutton; 
	Elevator(int x, int n)
	{
		this.PresentFloor = x;
		this.floorbutton = new boolean[n+1] ;
		for (int i=1;i<=n;i++)
			this.floorbutton[i]=false;
	}
	public int getPresentFloor()
	{
		return this.PresentFloor;
	}
	public void setPresentFloor(int x)
	{
		this.PresentFloor = x;
	}
	public boolean getEleFloor(int b)
	{
		return this.floorbutton[b];
	}
	public void setFLoorButton(int b)
	{
		this.floorbutton[b] = true;
	}
	public void cancelFLoorButton(int b)
	{
		this.floorbutton[b] = false;
	}
	/*public static void main(String args[])
	{
		Elevator e = new Elevator(1,10);
		System.out.println(e.PresentFloor);
		for(int i=1;i<=10;i++)
			System.out.println(e.floorbutton[i]);
	}*/
}

