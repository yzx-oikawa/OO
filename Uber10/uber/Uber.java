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
	
	public static void main(String args[])
	{
		/*@Requires:map.txt和light.txt
		  @Modifies:this,Taxis, Map, Orderlist, Road, Light, Dispatch, GUI, System.out
		  @Effects:初始化所有对象，并启动作为线程的对象
		*/
		try{
			File file1 = new File("C://Users//Administrator//Desktop//map.txt");
			File file2 = new File("C://Users//Administrator//Desktop//light.txt");
			if(file1.exists()&&file2.exists())
			{
				TaxiGUI gui = new TaxiGUI();
				Map m= new Map();
				m = Map.ParseMap(file1);
				Light l= new Light(gui);
				if(l.SetLight(file2)!=null&&m!=null&&l!=null)
				{	
			//		System.out.println(System.currentTimeMillis());
			//		System.out.println(m.getDistance(81,650)+" "+m.getDistance(650,81));
					Orderlist OL = new Orderlist();
					gui.LoadMap(m.getValue(), length);
					Thread road = new Road(m);
					((Road)road).setTaxi(Taxis);
					((Road)road).initial();
					m.setRoad((Road)road);
					l.start();
					for(int i=0;i<100;i++)
					{
						Taxis[i] = new Taxi(i, m, gui);
						gui.SetTaxiStatus(i, new Point(Taxis[i].getx(), Taxis[i].gety()), Taxis[i].getstate());
						Taxis[i].setRoad((Road)road);
						Taxis[i].setLight(l);
					//	System.out.println(Taxis[i].getx()+","+Taxis[i].gety());
					}
					road.start();
					for(int i=0;i<100;i++)
						Taxis[i].start();
					Thread in = new Input(OL, Taxis, gui, m);
					in.start();
					Thread dis = new Dispatch(m, (Taxi[])Taxis, OL);
					dis.start();
					
					System.out.println("Ready for order!");
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
