package uber;

public class Points {
	/* Overview:地图上的点，保存了点的序号、坐标、以及上下左右的道路是否连通等属性
	 * 抽象函数：AF(c)=(No, x, y, value, up ,down, left, right, visit) 
	          where No==c.No, x==c.x, y==c.y, value==c.value, up==c.up, down==c.down,
	          left==c.left, right==c.right, visit==c.visit
	 * 不变式：0<=c.No<6400 && 0<=c.x,c.y<80 && 0<=c.value<=3 && (c.visit=0||c.visit=1)
	 * */
	private int No;
	private int x;
	private int y;
	private int value;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private int visit;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(No<0 || No>=6400 || x<0 || x>=80 || y<0 || y>=80 || value<0 || value>3 || (visit!=0 && visit!=1))
			return false;
		return true;
	}
	Points(int v)
	{
		/*@Requires:无
		  @Modifies:this
		  @Effects:构造一个Points对象并初始化
		*/
		this.value = v;
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.visit = 0;
	}
	Points(int n,int x, int y, int v)
	{
		/*@Requires:四个int类型参数，分表表示点的序号、x坐标、y坐标、value
		  @Modifies:this
		  @Effects:根据传入参数构造一个Points对象
		*/
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
	public int getNo() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的序号
		*/
		return this.No;}
	public void setNo(int No) {
		/*@Requires:int类型参数(序号)
		  @Modifies:this.No
		  @Effects:将点的序号设置为传入的参数
		*/
		this.No = No;}
	public int getx() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的x坐标
		*/
		return this.x;}
	public void setx(int x) {
		/*@Requires:int类型参数(x坐标)
		  @Modifies:this.x
		  @Effects:将点的x坐标设置为传入的参数
		*/
		this.x = x;}
	public int gety() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的有y坐标
		*/
		return this.y;}
	public void sety(int y) {
		/*@Requires:int类型参数(y坐标)
		  @Modifies:this.y
		  @Effects:将点的y坐标设置为传入的参数
		*/
		this.y = y;}
	public int getValue() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的value
		*/
		return value;}
	public void setValue(int v) {
		/*@Requires:int类型参数(value)
		  @Modifies:this.value
		  @Effects:将点的value设置为传入的参数
		*/
		this.value = v;}
	public void addValue(int v) {
		/*@Requires:int类型参数(value)
		  @Modifies:this.value
		  @Effects:将点的value加上传入的参数
		*/
		this.value += v;}
	public boolean getUp() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的上边
		*/
		return this.up;}
	public void setUp(boolean b) {
		/*@Requires:boolean类型参数(上边是否打开)
		  @Modifies:this.up
		  @Effects:将点的上边设置为传入的参数
		*/
		this.up = b;}
	public boolean getDown() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的下边
		*/
		return this.down;}
	public void setDown(boolean b) {
		/*@Requires:boolean类型参数(下边是否打开)
		  @Modifies:this.down
		  @Effects:将点的下边设置为传入的参数
		*/
		this.down = b;}
	public boolean getLeft() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的左边
		*/
		return this.left;}
	public void setLeft(boolean b) {
		/*@Requires:boolean类型参数(左边是否打开)
		  @Modifies:this.left
		  @Effects:将点的左边设置为传入的参数
		*/
		this.left = b;}
	public boolean getRight() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的右边
		*/
		return this.right;}
	public void setRight(boolean b) {
		/*@Requires:boolean类型参数(右边是否打开)
		  @Modifies:this.right
		  @Effects:将点的右边设置为传入的参数
		*/
		this.right = b;}
	public int getVisit() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回点的visit
		*/
		return this.visit;}
	public void setVisit(int v) {
		/*@Requires:int类型参数(点是否被访问)
		  @Modifies:this.visit
		  @Effects:将点的visit设为传入参数，1为被访问，0为未被访问
		*/
		this.visit = v;}
	public void Initial(int v)
	{
		/*@Requires:int类型的参数，表示对点进行何种类型的初始化
		  @Modifies:this
		  @Effects:根据传入参数对点进行相应的初始化
		*/
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
