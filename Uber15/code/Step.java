package uber;

public class Step {
	/* Overview:由六个int类型的数构成的对象，保存了从起始点到目标点的最短路径中的一步的坐标、方向和道路流量
	 * 抽象函数：AF(c)=(fromX, fromY, toX, toY, flow, dir) where fromX==c.fromX, fromY==c.fromY,
	   		  toX==c.toX, toY=c.toY, up==c.up, flow==c.flow, dir==c.dir
	 * 不变式：0<=c.fromX,c.fromY,c.toX,c.toY<80 && c.flow=>0 && 1<=c.dir<=4
	 * */
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private int flow;
	private int dir;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(fromX<0 || fromX>=80 || fromY<0 || fromY>=80 || toX<0 || toX>=80 || toY<0 || toY>=80)
			return false;
		if(flow<0 || dir<1 || dir>4)
			return false;
		return true;
	}
	Step(int a, int b, int c, int d, int e, int f)
	{
		/*@Requires:六个int类型参数，分表表示起始点的xy坐标、目标点的xy坐标、起始点到目标点的边的流量和方向
		  @Modifies:this
		  @Effects:根据传入参数构造一个Step对象
		*/
		this.fromX=a;
		this.fromY=b;
		this.toX=c;
		this.toY=d;
		this.flow = e;
		this.dir = f;
	}
	public int getFromX() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回起始点的x坐标
		*/
		return fromX;
	}
	
	public int getFromY() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回起始点的y坐标
		*/
		return fromY;
	}
	
	public int getToX() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回目标点的x坐标
		*/
		return toX;
	}
	
	public int getToY() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回目标点的y坐标
		*/
		return toY;
	}
	public int getFlow() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回起始点到目标点的边的流量
		*/
		return flow;
	}
	public int getdir() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回起始点到目标点的边的方向
		*/
		return dir;
	}


	
}
