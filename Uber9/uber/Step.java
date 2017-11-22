package uber;

public class Step {
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private int flow;
	private int dir;
	Step(int a,int b,int c,int d,int e,int f)
	{
		/*@Requires:六个int类型参数，分表表示起始点的xy坐标、目标点的xy坐标、起始点到目标点的边的流量和方向
		  @Modifies:无
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
