package uber;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;   
import java.io.FileWriter;

public class Taxi extends Thread{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 
	private static int length = 80;
	private int No;
	private int state;//1服务状态 2接单状态 3等待服务 4停止运行
	private int credit;
	private int x;
	private int y;
	private int lastx;
	private int lasty;
	private Order order;
	private Map map;
	private TaxiGUI gui;
	private Road road;
	public int getNo() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的序号
		*/
		return this.No;}
	public int getstate() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的状态
		*/
		return this.state;}
	public void setstate(int state) {
		/*@Requires:int类型参数(出租车的状态)
		  @Modifies:无
		  @Effects:将出租车的状态设置为传入参数的值
		*/
		this.state = state;}
	public int getx() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车当前位置的x坐标
		*/
		return this.x;}
	public void setx(int x) {
		/*@Requires:int类型参数(x坐标)
		  @Modifies:无
		  @Effects:将出租车当前位置的x坐标设置为传入参数的值
		*/
		this.x = x;}
	public int gety() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车当前位置的y坐标
		*/
		return this.y;}
	public void sety(int y) {
		/*@Requires:int类型参数(y坐标)
		  @Modifies:无
		  @Effects:将出租车当前位置的y坐标设置为传入参数的值
		*/
		this.y = y;}
	public int getCredit() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的信用
		*/
		return this.credit;}
	public void addCredit(int credit) {
		/*@Requires:int类型参数(信用)
		  @Modifies:无
		  @Effects:将出租车当前位置的信用加上传入参数的值
		*/
		this.credit += credit;}
	public Order getOrder() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车接到的订单；如果出租车没有接到单，返回null
		*/
		return this.order;}
	public void setOrder(Order order) {
		/*@Requires:Order
		  @Modifies:无
		  @Effects:将出租车的订单设为传入参数
		*/
		this.order = order;}
	public void setRoad(Road r){
		/*@Requires:Road
		  @Modifies:无
		  @Effects:将出租车的道路设为传入参数
		*/
		this.road = r;}
	
	Taxi(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:构造一个Taxi对象
		*/
	}
	Taxi(int no, Map map, TaxiGUI gui)
	{
		/*@Requires:出租车序号，地图，GUI
		  @Modifies:无
		  @Effects:根据传入参数构造一个Taxi对象
		*/
		this.No = no;
		this.state = 3;
		this.credit = 0;
		this.order = null;
		this.x = new Random().nextInt(80);
		this.y = new Random().nextInt(80);
		this.lastx = this.x;
		this.lasty = this.y;
		this.map = map;
		this.gui = gui;
	}
	
	public void runRandom()//乱走一次
	{
		/*@Requires:获得地图上每一条边的开关情况
		  @Modifies:GUI
		  @Effects:出租车沿流量最短的一条路运动一次
		*/
		int flag = 0;
		int choose = 0;
		int direction = new Random().nextInt(4);
		int flow = 10000;
		while(flag<4)
		{
		//	if(this.No==0)
		//		System.out.print(direction);
			switch(direction){
				case 0://UP
					if(map.getPoint(this.x, this.y).getUp()==true
					 &&this.road.getflowv(this.x-1, this.y)<flow)
					{
						flow = this.road.getflowv(this.x-1, this.y);
						choose = 0;
					}
					flag++;
					break;
				case 1://DOWN
					if(map.getPoint(this.x, this.y).getDown()==true
					 &&this.road.getflowv(this.x, this.y)<flow)
					{
						flow = this.road.getflowv(this.x, this.y);
						choose = 1;
					}
					flag++;
					break;
				case 2://LEFT
					if(map.getPoint(this.x, this.y).getLeft()==true
					 &&this.road.getflowh(this.x, this.y-1)<flow)
					{
						flow = this.road.getflowh(this.x, this.y-1);
						choose = 2;
					}
					flag++;
					break;
				case 3://RIGHT
					if(map.getPoint(this.x, this.y).getRight()==true
					 &&this.road.getflowh(this.x, this.y)<flow)
					{
						flow = this.road.getflowh(this.x, this.y);
						choose = 3;
					}
					flag++;
					break;
			}
			direction = (direction+1)%4;
		}
		switch(choose)
		{
			case 0:
				this.lastx = this.x;
				this.x--; 
				break;
			case 1:
				this.lastx = this.x;
				this.x++;
				break;
			case 2:
				this.lasty = this.y;
				this.y--; 
				break;
			case 3:
				this.lasty = this.y;
				this.y++; 
				break;
		}
		//if(this.No==0) System.out.println("");
		//System.out.println(this.No+": "+this.x+", "+this.y);
		gui.SetTaxiStatus(this.No, new Point(this.x, this.y), this.state);
		try{
			Thread.sleep(200);
		}catch(InterruptedException e){}
	}
	
	public void runShortest(int x, int y)//x,y are for target position
	{
		/*@Requires:出租车的目的地坐标，参数坐标需要在地图内部，同时需要能够获得地图上每一条边的开关情况
		  @Modifies:文件，GUI
		  @Effects:出租车沿最短路径运动，同时将路径输出到文件
		*/
		ArrayList<Step> steps = map.BFS(this.x, this.y, x, y);
		if(steps!=null)
		{
		while(this.x!=x || this.y!=y)
		{
		//	System.out.println("("+this.x+","+this.y+")");
			try{ 
		        FileWriter fw = new FileWriter(this.order.getFile(),true);
		        fw.write("("+this.x+","+this.y+")"+"->");
		        fw.close();
			}catch(Exception e){}
			
			Step now = steps.get(0);
			boolean flag = false;
			switch(now.getdir()){
			case 1://UP
				flag = map.getPoint(this.x, this.y).getUp();break;
			case 2://DOWN
				flag = map.getPoint(this.x, this.y).getDown();break;
			case 3://LEFT
				flag = map.getPoint(this.x, this.y).getLeft();break;
			case 4://RIGHT
				flag = map.getPoint(this.x, this.y).getRight();break;
			}
			if(flag)//要判断上下左右能不能走，能走才能
			{
				this.lastx = x;
				this.lasty = y;
				this.x = now.getToX();
				this.y = now.getToY();
			}
			else
			{
				steps = null;
				steps = map.BFS(this.x, this.y, x, y);
			}
			steps.remove(0);
			
			gui.SetTaxiStatus(this.No, new Point(this.x, this.y), this.state);
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
		}
		}	
	//	System.out.println("("+this.x+","+this.y+")");
		try{ 
	        FileWriter fw = new FileWriter(this.order.getFile(),true);
	        fw.write("("+this.x+","+this.y+")"+LINE_SEPARATOR);
	        fw.close();
			}catch(Exception e){}
		
	}
	public int calculateFlow()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:出租车往上、下、左、右运动时分别返回0,1,2,3;如果为停止运行状态，则返回0
		*/
		if(this.x==this.lastx && this.y==this.lasty-1)//LEFT
			return 2;
		else if(this.x==this.lastx && this.y==this.lasty+1)//RIGHT
			return 3;
		else if(this.x==this.lastx-1 && this.y==this.lasty)//UP
			return 0;
		else if(this.x==this.lastx+1 && this.y==this.lasty)//DOWN
			return 1;
		return -1;//STILL
	}
	public void run()
	{
		/*@Requires:GUI
		  @Modifies:GUI
		  @Effects:让出租车根据地图、订单等属性运行并修改位置、状态、信用等信息
		*/
		try{
			int serving = 0;//20000ms
			while(true){
				if (this.state==1)//服务状态
				{
		//			this.map.BFS(order.getSrcX(),order.getSrcY());
					this.runShortest(order.getDstX(), order.getDstY());
					this.credit+=3;
					this.state = 3;
					serving = 0;
				}
				else if (this.state==2)//接单状态
				{
		//			this.map.BFS(this.x,this.y);
		//			this.map.BFS(order.getSrcX(), order.getSrcY());
		//			System.out.println(this.map.getDirection(this.y*length+this.x, order.getSrcY()*length+order.getSrcX()));
					this.runShortest(order.getSrcX(), order.getSrcY());
					this.state = 1;
					serving = 0;
				}
				else if (this.state==3)//等待服务
				{
					if(serving==20000)
					{
						this.state = 4;
						serving = 0;
					}else{
						this.runRandom();//乱走一次
						serving += 200;
					}
				}
				else if(this.state==4)//停止运行
				{
					gui.SetTaxiStatus(this.No, new Point(this.x, this.y), this.state);
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){}
					this.state = 3;
					serving = 0;
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
