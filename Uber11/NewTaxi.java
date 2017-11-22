package uber;

import java.awt.Point;
import java.io.FileWriter;
import java.util.Random;
import java.util.ArrayList;
import java.util.ListIterator;

public class NewTaxi extends Taxi{
	/* Overview:线程类; 可追踪出租车，继承出租车类;可以在关闭的道路上行驶；与出租车类相比增加了可追踪乘客服务情况的功能
	 * 抽象函数：AF(c)=(No, type, state, credit, x, y, lastx, lasty, order, map, gui, road, light, current, ServeInfo, iterator)  
	         where No==c.No, type==c.type, state==c.state, credit==c.credit, x==c.x, y==c.y, lastx==c.lastx, lasty==c.lasty,
	         	   order==c.order, map==c.map, gui==c.gui, road==c.road, light==c.light, 
	         	   current==c.current, ServeInfo==c.ServeInfo, iterator==c.interator
	 * 不变式：0<=c.No<=99 && 0<=c.state<=3 && c.credit>=0 && 0<=c.x,c.y,c.lastx,c.lasty<80 && 
	        c.map=!=null && c.road!=null && c.light!=null && c.current>0 && c.ServeInfo!=null && c.literator !=null
     * */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 	
//	private int count;
	private int current;
	private ArrayList<String> ServeInfo;
	private ListIterator<String> iterator;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(No<0 || No>99 || state<0 || state>3 || credit<0)
			return false;
		if(x<0 || x>=80 || y<0 || y>=80 || lastx<0 || lastx>=80 || lasty<0 || lasty>=80 || current<0)
			return false;
		if(map==null || road==null || light==null || ServeInfo==null || iterator==null)
			return false;
		return true;
	}
	public NewTaxi(int no, Map map, TaxiGUI gui) {
		/*@Requires:出租车序号，地图，GUI
		  @Modifies:this
		  @Effects:根据传入参数构造一个NewTaxi对象
		*/
		super(no, map, gui);
		this.type = 1;
		this.state = 2;
		this.credit = 0;
		this.order = null;
		this.x = new Random().nextInt(80);
		this.y = new Random().nextInt(80);
		this.lastx = this.x;
		this.lasty = this.y;
		this.road = new Road(map);
		this.light = new Light(gui);
		this.ServeInfo = new ArrayList<String>();
		this.ServeInfo.add("");
		this.iterator = this.ServeInfo.listIterator();
		this.current = 0;
	//	this.count =0;
	}
	
	public ArrayList<String> getServeInfo()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回可追踪出租车的乘客服务信息序列
		*/
		return this.ServeInfo;
	}
	public void addString(int index, String s)
	{
		/*@Requires:乘客服务情况信息的序号，添加的字符串
		  @Modifies:this.ServeInfo
		  @Effects:更新可追踪出租车的乘客服务信息序列的相应项（将传入的字符串加在原来的字符串后面）
		*/
		String temp;
		temp = this.ServeInfo.get(index);
		this.ServeInfo.set(index, temp+s);
	}
	public ListIterator<String> getIterator()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回可追踪出租车的双向迭代器
		*/
		return this.iterator;
	}
	public void setIterator()
	{
		/*@Requires:无
		  @Modifies:this.iterator
		  @Effects:更新可追踪出租车的双向迭代器
		*/
		this.iterator = this.ServeInfo.listIterator();
	}
	
	public void runShortest(int x, int y)//x,y are for target position
	{
		/*@Requires:可追踪出租车的目的地坐标，参数坐标需要在地图内部，同时需要能够获得地图上每一条边的开关情况
		  @Modifies:文件，GUI, this
		  @Effects:可追踪出租车沿最短路径运动，同时将路径输出到文件并更新乘客服务信息序列；如果运动过程中遇到红灯，在原地等待直到变为绿灯
		*/
		ArrayList<Step> steps = map.BFS(this.x, this.y, x, y);
		if(steps!=null)
		{
			while((this.x!=x || this.y!=y)&&steps.size()>0)
			{
			//	System.out.println("("+this.lastx+","+this.lasty+")");
			//	System.out.println("("+this.x+","+this.y+")"+"->");
				this.addString(this.current, "("+this.x+","+this.y+")"+"->");
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
		this.addString(this.current, "("+this.x+","+this.y+")\n");
	//	System.out.println("("+this.x+","+this.y+")");
		try{ 
	        FileWriter fw = new FileWriter(this.order.getFile(),true);
	        fw.write("("+this.x+","+this.y+")"+LINE_SEPARATOR);
	        fw.close();
		}catch(Exception e){}
		
	}
	public void run()
	{
		/*@Requires:GUI
		  @Modifies:GUI, this
		  @Effects:让可追踪出租车根据地图、订单等属性运行并修改位置、状态、信用等信息，并更新乘客服务信息序列
		*/
		try{
			int serving = 0;//20000ms
			while(true){
				if (this.state==1)//服务状态
				{
					//count++;
					//if(count==3)System.out.println(this.No);
					this.runShortest(order.getDstX(), order.getDstY());
					//System.out.println(this.No+" "+this.ServeInfo.get(this.current));
					this.current++;
					this.ServeInfo.add("");
				//	System.out.println(this.ServeInfo.get(this.current));
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
					this.addString(this.current, "Order: "+order.getTime()+" ("+order.getSrcX()+","
							+order.getSrcY()+")->("+order.getDstX()+","+order.getDstY()+")\n");
					this.addString(this.current, "Taxi at ("+this.x+","+this.y+")\n");
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
