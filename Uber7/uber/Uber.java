package uber;

import java.awt.Point;
import java.io.File;

public class Uber {
	private static int length = 80;
	public static long Tstart;
	public static Taxi[] Taxis = new Taxi[100];
	public static long getTime() {
		long temp = (System.currentTimeMillis()-Tstart)/100;
		return (temp)*100;
	}
	public static void setTime(long t) {Tstart = t;}
	
	public static void main(String args[])
	{
		File file = new File("C://Users//Administrator//Desktop//map.txt");
		Map m= new Map();
		m = Map.ParseMap(file);
		if(m!=null)
//		for(int i=0;i<length;i++){
//			for(int j=0;j<length;j++){
//				System.out.print(m.getPoint(i,j).getValue()+" ");
//			}
//			System.out.println();
//		}
//		System.out.println(System.currentTimeMillis());
		{	
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++){
					m.BFS(j,i);
				}
			}
	//		System.out.println(System.currentTimeMillis());
	//		System.out.println(m.getDistance(81,650)+" "+m.getDistance(650,81));
			TaxiGUI gui = new TaxiGUI();
			Orderlist OL = new Orderlist();
			gui.LoadMap(m.getValue(), length);
			for(int i=0;i<100;i++)
			{
				Taxis[i] = new Taxi(i, m, gui);
				gui.SetTaxiStatus(i, new Point(Taxis[i].getx(), Taxis[i].gety()), Taxis[i].getstate());
				Taxis[i].start();
			//	System.out.println(Taxis[i].getx()+","+Taxis[i].gety());
			}
			Thread in = new Input(OL, Taxis, gui);
			in.start();
			Thread dis = new Dispatch(m, (Taxi[])Taxis, OL);
			dis.start();
			System.out.println("Ready for order!");
		}
	}
}
