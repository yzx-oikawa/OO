package poly;

public class Term {
	private int co;
	private int deg;
	Term(int co,int deg)
	{
		this.co = co;
		this.deg = deg;
	}
	public int getDeg()
	{
		return this.deg;
	}
	public int getCo()
	{
		return this.co;
	}
	public void Inverse()
	{
		this.co = -this.co;
	}
}
