package uber;

import java.awt.Point;
import java.io.File;

public class Uber {
	/* Overview:主类，包括Main函数。功能为设定系统时间和创建各对象并启动各线程
	 * 抽象函数：AF(c)=(Tstart, Taxis) where Tstart==c.Tstart, Taxis==c.Taxis
	 * 不变式：c.Tstart>=0 && c.Taxis!=null 
     * */
	private static int length = 80;
	public static long Tstart;
	public static Taxi[] Taxis = new Taxi[100];
//	public static NewTaxi[] NewTaxis = new NewTaxi[30];
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(Tstart<0 || Taxis==null)
			return false;
		return true;
	}
	public static long getTime() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回系统时间(时间窗为100ms)
		*/
		long temp = (System.currentTimeMillis()-Tstart)/100;
		return (temp)*100;
	}
	public static void setTime(long t) {
		/*@Requires:long类型的参数(时间)
		  @Modifies:this.Tstart
		  @Effects:将Uber的初始时间Tstart设置为传入参数
		*/
		Tstart = t;}
	public static Taxi[] initTaxi(Map m, Map m2, TaxiGUI gui, Road road, Light l)
	{
		/*@Requires:无
		  @Modifies:gui
		  @Effects:根据测试者的设计返回100个初始化好的出租车对象
		*/
		//for(int i=0;i<81;i++)
		//System.out.println("[("+i+",8),("+i+",9),OPEN]");
		int a[] = new int[100];
		for(int i=0;i<100;i++)
			a[i] = 0;
		//*********************************************
		//在此处设置出租车类别，将编号为i的出租车设置为可追踪出租车：a[i]=1
		for(int i=30;i<60;i++)
			a[i] = 1;
		//*********************************************
		int count =0;
		for(int i=0;i<100;i++)
			if(a[i] == 1) count++;
		if(count!=30)
		{
			System.out.println("The number of traceable taxi is not 30!");
//			return null;
		}
		Taxi[] init = new Taxi[100];
		for(int i=0;i<100;i++)
		{
			if(a[i]==0){
				init[i] = new Taxi(i, m, gui);
				gui.SetTaxiStatus(i, new Point(init[i].getx(), init[i].gety()), init[i].getstate());
				gui.SetTaxiType(i, 0);
				init[i].setRoad(road);
				init[i].setLight(l);
			}
			else if(a[i]==1)
			{
				init[i] = new NewTaxi(i, m2, gui);
				gui.SetTaxiStatus(i, new Point(init[i].getx(), init[i].gety()), init[i].getstate());
				gui.SetTaxiType(i, 1);
				init[i].setRoad(road);
				init[i].setLight(l);
			}
			
		}
		return init;
		
	}
	public static void main(String args[])
	{
		/*@Requires:map.txt和light.txt
		  @Modifies:this, Taxis, Map, Orderlist, Road, Light, Dispatch, GUI, System.out, 文件夹testtaxi
		  @Effects:初始化所有对象，并启动作为线程的对象
		*/
		try{
			File dir = new File("D://testtaxi");
			if(!dir.exists()){
				dir.mkdir();
			}
			File file1 = new File("D://map.txt");
			File file2 = new File("D://light.txt");
			if(file1.exists()&&file2.exists())
			{
				TaxiGUI gui = new TaxiGUI();
				Map m = new Map();
				m = Map.ParseMap(file1);
				Map m2 = new Map();
				m2 = Map.ParseMap(file1);
				Light l= new Light(gui);
				if(l.SetLight(file2)!=null&&m!=null&&l!=null)
				{	
					Orderlist OL = new Orderlist();
					gui.LoadMap(m.getValue(), length);
					Thread road = new Road(m);	
					((Road)road).initial();
					m.setRoad((Road)road);
					m2.setRoad((Road)road);
					l.start();
					Taxis = initTaxi(m, m2, gui, (Road)road, l);
					if(Taxis!=null){
						((Road)road).setTaxi(Taxis);
						road.start();
						for(int i=0;i<100;i++)
							Taxis[i].start();
						
						Thread in = new Input(OL, Taxis, gui, m);
						in.start();
						Thread dis = new Dispatch(m, (Taxi[])Taxis, OL);
						dis.start();
						System.out.println("Ready for order!");
					}
				}
			}else{
				if(!file1.exists())
					System.out.println("map.txt does not exist!");
				if(!file2.exists())
					System.out.println("light.txt does not exist!");
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
