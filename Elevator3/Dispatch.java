package elevator;

public class Dispatch implements Move{
	private double Tsend;
	private double Trun;
	private double Tend;
	private int floor;
	private int state; //1-UP -1-DOWN 0-STILL
	Dispatch(){};
	Dispatch(double Tsend, double Trun, double Tend, int floor, int state)
	{
		this.Tsend = Tsend;
		this.Trun = Trun;
		this.Tend = Tend;
		this.floor = floor;
		this.state = state;
	}
	Dispatch (double Tend, int floor, int state)
	{
		this.Tend = Tend;
		this.floor = floor;
		this.state = state;
	}
	public double getTsend()
	{
		return this.Tsend;
	}
	public double getTrun()
	{
		return this.Trun;
	}
	public double getTend()
	{
		return this.Tend;
	}
	public int getFloor()
	{
		return this.floor;
	}
	@Override
	public int getState()
	{
		return this.state;
	}
}
