package uber;

public class Points {
	private int No;
	private int x;
	private int y;
	private int value;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private int visit;
	Points(){}
	Points(int v)
	{
		this.value = v;
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.visit = 0;
	}
	Points(int n,int x, int y, int v)
	{
		this.No = n;
		this.x = x;
		this.y = y;
		this.value = v;
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.visit = 0;
	}
	public int getNo() {return this.No;}
	public void setNo(int No) {this.No = No;}
	public int getx() {return this.x;}
	public void setx(int x) {this.x = x;}
	public int gety() {return this.y;}
	public void sety(int y) {this.y = y;}
	public int getValue() {return value;}
	public void setValue(int value) {this.value = value;}
	public boolean getUp() {return this.up;}
	public void setUp() {this.up = true;}
	public boolean getDown() {return this.down;}
	public void setDown() {this.down = true;}
	public boolean getLeft() {return this.left;}
	public void setLeft() {this.left = true;}
	public boolean getRight() {return this.right;}
	public void setRight() {this.right = true;}
	public int getVisit() {return this.visit;}
	public void setVisit(int v) {this.visit = v;}
	public void Initial(int v)
	{
		switch(v)
		{
			case 0:
				break;
			case 1:
				this.right = true;
				break;
			case 2:
				this.down = true;
				break;
			case 3:
				this.right = true;
				this.down = true;
				break;
			case -1:
				this.left = true;
				break;
			case -2:
				this.up = true;
				break;
		}
	}
}
