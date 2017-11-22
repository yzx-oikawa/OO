package uber;

import java.awt.Point;
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
	private Order order;
	private Map map;
	private TaxiGUI gui;
	public int getNo() {return this.No;}
	public void setNo(int no) {this.No = no;}
	public int getstate() {return this.state;}
	public void setstate(int state) {this.state = state;}
	public int getx() {return this.x;}
	public void setx(int x) {this.x = x;}
	public int gety() {return this.y;}
	public void sety(int y) {this.y = y;}
	public int getCredit() {return this.credit;}
	public void addCredit(int credit) {this.credit += credit;}
	public Order getOrder() {return this.order;}
	public void setOrder(Order order) {this.order = order;}
	
	Taxi(){}
	Taxi(int no, Map map, TaxiGUI gui)
	{
		this.No = no;
		this.state = 3;
		this.credit = 0;
		this.order = null;
		this.x = new Random().nextInt(80);
		this.y = new Random().nextInt(80);
		this.map = map;
		this.gui = gui;
	}
	
	public void runRandom()//乱走一次
	{
		int flag = 0;
		int direction;
		while(flag!=1)
		{
			direction = new Random().nextInt(4);
		//	System.out.println(direction);
			switch(direction){
				case 0://UP
					if(map.getPoint(this.y, this.x).getUp()==true)
					{
						this.y--; 
						flag=1;
					}
					break;
				case 1://DOWN
					if(map.getPoint(this.y, this.x).getDown()==true)
					{
						this.y++;
						flag=1;
					}
					break;
				case 2://LEFT
					if(map.getPoint(this.y, this.x).getLeft()==true)
					{
						this.x--; 
						flag=1;
					}
					break;
				case 3://RIGHT
					if(map.getPoint(this.y, this.x).getRight()==true)
					{
						this.x++; 
						flag=1;
					}
					break;
			}
		}
		//System.out.println(this.No+": "+this.x+", "+this.y);
		gui.SetTaxiStatus(this.No, new Point(this.y, this.x), this.state);
		try{
			Thread.sleep(200);
		}catch(InterruptedException e){}
	}
	
	public void runShortest(int x, int y)//x,y are for target position
	{
		int direction = 0;
		direction = map.getDirection(this.y*length+this.x, y*length+x);
		while(this.x!=x || this.y!=y)
		{
		//	System.out.println("("+this.x+","+this.y+")");
			try{ 
		        FileWriter fw = new FileWriter(this.order.getFile(),true);
		        fw.write("("+this.x+","+this.y+")"+"->");
		        fw.close();
				}catch(Exception e){}
			switch(direction){
			case 1://UP
				this.y--; break;
			case 2://DOWN
				this.y++; break;
			case 3://LEFT
				this.x--; break;
			case 4://RIGHT
				this.x++; break;
			}
			direction = map.getDirection(this.y*length+this.x, y*length+x);
			gui.SetTaxiStatus(this.No, new Point(this.y, this.x), this.state);
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
		}
	//	System.out.println("("+this.x+","+this.y+")");
		try{ 
	        FileWriter fw = new FileWriter(this.order.getFile(),true);
	        fw.write("("+this.x+","+this.y+")"+LINE_SEPARATOR);
	        fw.close();
			}catch(Exception e){}
	}
	public void run()
	{
		try{
			int serving = 0;//20000ms
			while(true){
				if (this.state==1)//服务状态
				{
					this.runShortest(order.getDstX(), order.getDstY());
					this.credit+=3;
					this.state = 3;
					serving = 0;
				}
				else if (this.state==2)//接单状态
				{
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
		}catch(Exception e){}
	}
	


}
