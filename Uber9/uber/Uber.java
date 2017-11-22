package uber;

import java.awt.Point;
import java.io.File;

public class Uber {
	private static int length = 80;
	public static long Tstart;
	public static Taxi[] Taxis = new Taxi[100];
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
		  @Modifies:无
		  @Effects:将Uber的初始时间Tstart设置为传入参数
		*/
		Tstart = t;}
	
	public static void main(String args[])
	{
		try{
			File file = new File("C://Users//Administrator//Desktop//map.txt");
			if(file.exists())
			{
				Map m= new Map();
				m = Map.ParseMap(file);
				if(m!=null)
		//		for(int i=0;i<length;i++){
		//			for(int j=0;j<length;j++){
		//				System.out.print(m.getPoint(i,j).getValue()+" ");
		//			}
		//			System.out.println();
		//		}
				{	
			//		System.out.println(System.currentTimeMillis());
			//		System.out.println(m.getDistance(81,650)+" "+m.getDistance(650,81));
					TaxiGUI gui = new TaxiGUI();
					Orderlist OL = new Orderlist();
					gui.LoadMap(m.getValue(), length);
					Thread road = new Road(m);
					((Road)road).setTaxi(Taxis);
					((Road)road).initial();
					m.setRoad((Road)road);
						
					for(int i=0;i<100;i++)
					{
						Taxis[i] = new Taxi(i, m, gui);
						gui.SetTaxiStatus(i, new Point(Taxis[i].getx(), Taxis[i].gety()), Taxis[i].getstate());
						Taxis[i].setRoad((Road)road);
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
			}
			else
				System.out.println("map.txt does not exist!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
