package uber;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;   
import java.io.FileWriter;

public class Taxi extends Thread{
	/* Overview:线程类; 出租车根据地图、道路和红绿灯在地图上运动，有服务、接单、等待服务、停止运行这四种运动状态，可做抢单、接单操作
	 * 抽象函数：AF(c)=(No, type, state, credit, x, y, lastx, lasty, order, map, gui, road, light)  
	         where No==c.No, type==c.type, state==c.state, credit==c.credit, x==c.x, y==c.y, lastx==c.lastx, lasty==c.lasty,
	         	   order==c.order, map==c.map, gui==c.gui, road==c.road, light==c.light
	 * 不变式：0<=c.No<=99 && 0<=c.state<=3 && c.credit>=0 && 0<=c.x,c.y,c.lastx,c.lasty<80 && 
	        c.map=!=null && c.road!=null && c.light!=null
     * */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 
	protected int No;
	protected int type; //0普通 1追踪
	protected int state;//1服务状态 3接单状态 2等待服务 0停止运行
	protected int credit;
	protected int x;
	protected int y;
	protected int lastx;
	protected int lasty;
	protected Order order;
	protected Map map;
	protected TaxiGUI gui;
	protected Road road;
	protected Light light;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(No<0 || No>99 || state<0 || state>3 || credit<0)
			return false;
		if(x<0 || x>=80 || y<0 || y>=80 || lastx<0 || lastx>=80 || lasty<0 || lasty>=80)
			return false;
		if(map==null || road==null || light==null)
			return false;
		return true;
	}
	public Taxi(int no, Map map, TaxiGUI gui)
	{
		/*@Requires:出租车序号，地图，GUI
		  @Modifies:this
		  @Effects:根据传入参数构造一个Taxi对象
		*/
		this.No = no;
		this.type = 0;
		this.state = 2;
		this.credit = 0;
		this.order = null;
		this.x = new Random().nextInt(80);
		this.y = new Random().nextInt(80);
		this.lastx = this.x;
		this.lasty = this.y;
		this.map = map;
		this.gui = gui;
		this.road = new Road(map);
		this.light = new Light(gui);
	}
	public int getNo() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的序号
		*/
		return this.No;}
	public int getType() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的种类，如果是普通出租车，返回0；如果是可追踪出租车，返回1
		*/
		return this.type;}
	public int getstate() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车的状态
		*/
		return this.state;}
	public void setstate(int state) {
		/*@Requires:int类型参数(出租车的状态)
		  @Modifies:this.state
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
		  @Modifies:this.x
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
		  @Modifies:this.y
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
		  @Modifies:this.credit
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
		  @Modifies:this.order
		  @Effects:将出租车的订单设为传入参数
		*/
		this.order = order;}
	public void setRoad(Road r){
		/*@Requires:Road
		  @Modifies:this.road
		  @Effects:将出租车的道路设为传入参数
		*/
		this.road = r;}
	public void setLight(Light l){
		/*@Requires:Light
		  @Modifies:this.light
		  @Effects:将出租车的红绿灯设为传入参数
		*/
		this.light = l;
	}
	
	public void runRandom()//乱走一次
	{
		/*@Requires:获得地图上每一条边的开关情况
		  @Modifies:GUI, this
		  @Effects:出租车沿流量最短的一条路运动一次;如果运动过程中遇到红灯，在原地等待直到变为绿灯
		*/
		int flag = 0;
		int choose = 0;
		int direction = new Random().nextInt(4);
		int flow = 10000;
		while(flag<4)
		{
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
				if(this.light.getsignal(this.x, this.y)==1)//有灯
				{
					if(this.lastx-1==this.x&&this.lasty==this.y)//从下方来
					{
						while(this.light.getpass(1, 0)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty+1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(2, 0)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
				}
				this.lastx = this.x;
				this.lasty = this.y;
				this.x--; break;
			case 1:
				if(this.light.getsignal(this.x, this.y)==1)//有灯
				{
					if(this.lastx+1==this.x&&this.lasty==this.y)//从上方来
					{
						while(this.light.getpass(0, 1)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty-1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(3, 1)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
				}
				this.lastx = this.x;
				this.lasty = this.y;
				this.x++; break;
			case 2:
				if(this.light.getsignal(this.x, this.y)==1)//有灯
				{
					if(this.lastx-1==this.x&&this.lasty==this.y)//从下方来
					{
						while(this.light.getpass(1, 2)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty-1==this.y&&this.lastx==this.x)//从右方来
					{
						while(this.light.getpass(3, 2)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
				}
				this.lastx = this.x;
				this.lasty = this.y;
				this.y--; break;
			case 3:
				if(this.light.getsignal(this.x, this.y)==1)//有灯
				{
					if(this.lastx+1==this.x&&this.lasty==this.y)//从上方来
					{
						while(this.light.getpass(0, 3)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty+1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(2, 3)==false){
						//	if(this.No==0) System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
				}
				this.lastx = this.x;
				this.lasty = this.y;
				this.y++; break;
		}
		//if(this.No==0) System.out.println("");
		//System.out.println(this.No+": "+this.x+", "+this.y);
		gui.SetTaxiStatus(this.No, new Point(this.x, this.y), this.state);
		try{
			Thread.sleep(200);
		}catch(InterruptedException e){e.printStackTrace();}
	}
	
	public void runShortest(int x, int y)//x,y are for target position
	{
		/*@Requires:出租车的目的地坐标，参数坐标需要在地图内部，同时需要能够获得地图上每一条边的开关情况
		  @Modifies:文件，GUI, this
		  @Effects:出租车沿最短路径运动，同时将路径输出到文件;如果运动过程中遇到红灯，在原地等待直到变为绿灯
		*/
		ArrayList<Step> steps = map.BFS(this.x, this.y, x, y);
		if(steps!=null)
		{
			while((this.x!=x || this.y!=y)&&steps.size()>0)
			{
			//	System.out.println("("+this.lastx+","+this.lasty+")");
			//	System.out.println("("+this.x+","+this.y+")");
				try{ 
			        FileWriter fw = new FileWriter(this.order.getFile(),true);
			        fw.write("("+this.x+","+this.y+")"+"->");
			        fw.close();
				}catch(Exception e){e.printStackTrace();}
				//System.out.println(steps.size());
				Step now = steps.get(0);
				boolean flag = false;
				switch(now.getdir()){
				case 1://UP
					if(this.lastx-1==this.x&&this.lasty==this.y)//从下方来
					{
						while(this.light.getpass(1, 0)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty+1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(2, 0)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					flag = map.getPoint(this.x, this.y).getUp();break;
				case 2://DOWN
					if(this.lastx+1==this.x&&this.lasty==this.y)//从上方来
					{
						while(this.light.getpass(0, 1)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty-1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(3, 1)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					flag = map.getPoint(this.x, this.y).getDown();break;
				case 3://LEFT
					if(this.lastx-1==this.x&&this.lasty==this.y)//从下方来
					{
						while(this.light.getpass(1, 2)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty-1==this.y&&this.lastx==this.x)//从右方来
					{
						while(this.light.getpass(3, 2)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					flag = map.getPoint(this.x, this.y).getLeft();break;
				case 4://RIGHT
					if(this.lastx+1==this.x&&this.lasty==this.y)//从上方来
					{
						while(this.light.getpass(0, 3)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					if(this.lasty+1==this.y&&this.lastx==this.x)//从左方来
					{
						while(this.light.getpass(2, 3)==false){
						//	System.out.println("wait");
							try{
								Thread.sleep(1);
							}catch(Exception e){e.printStackTrace();}
						}
					}
					flag = map.getPoint(this.x, this.y).getRight();break;
				}
				if(flag)//判断能不能走
				{
					this.lastx = this.x;
					this.lasty = this.y;
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
		  @Effects:出租车往上、下、左、右运动时分别返回0,1,2,3;如果为停止运行状态，则返回-1
		*/
//		if(this.getNo()==0){
//			System.out.println(this.lastx+", "+this.lasty);
//			System.out.println(this.x+", "+this.y);
//		}
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
		  @Modifies:GUI, this
		  @Effects:让出租车根据地图、订单等属性运行并修改位置、状态、信用等信息
		*/
		try{
			int serving = 0;//20000ms
			while(true){
				if (this.state==1)//服务状态
				{
					this.runShortest(order.getDstX(), order.getDstY());
					this.credit+=3;
					this.state = 0;
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){e.printStackTrace();}
					this.state = 2;
					serving = 0;
					
				}
				else if (this.state==3)//接单状态
				{
		//			System.out.println(this.map.getDirection(this.y*length+this.x, order.getSrcY()*length+order.getSrcX()));
					this.runShortest(order.getSrcX(), order.getSrcY());
					this.state = 0;
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){e.printStackTrace();}
					this.state = 1;
					serving = 0;
					
				}
				else if (this.state==2)//等待服务
				{
					if(serving==20000)
					{
						this.state = 0;
						serving = 0;
					}else{
						this.runRandom();//乱走一次
						serving += 200;
					}
				}
				else if(this.state==0)//停止运行
				{
					gui.SetTaxiStatus(this.No, new Point(this.x, this.y), this.state);
					this.lastx = this.x;
					this.lasty = this.y;
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){e.printStackTrace();}
					this.state = 2;
					serving = 0;
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
