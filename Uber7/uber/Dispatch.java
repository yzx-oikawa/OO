package uber;

import java.io.FileWriter;

public class Dispatch extends Thread{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Map map;
	private Taxi[] alltaxi;
	private Orderlist orderlist;
	public Map getMap() {return map;}
	public void setMap(Map map) {this.map = map;}
	public Taxi[] getAlltaxi() {return alltaxi;}
	public void setAlltaxi(Taxi[] alltaxi) {this.alltaxi = alltaxi;}
	public Orderlist getOrderlist() {return orderlist;}
	public void setOrderlist(Orderlist orderlist) {this.orderlist = orderlist;}
	Dispatch(Map map, Taxi[] alltaxi, Orderlist OL)
	{
		this.map = map;
		this.alltaxi = alltaxi;
		this.orderlist = OL;
	}
	public void run()
	{
		try
		{
			while(true){
			//	System.out.println(orderlist.getsize());
				if(orderlist.getsize()!=0){						
					for(int i = 0;i<orderlist.getsize();i++){//对列表的每个指令遍历
						Order temp = orderlist.getOrder(i);
						if(temp.getWait()==0){
							for(Taxi t:alltaxi)
								temp.startOrder(t);
						}
						for(Taxi t:alltaxi)
						{
							if(t.getstate()==3)
								temp.JudgeTaxi(t);
						}
						orderlist.getOrder(i).addWait(200);
						if(orderlist.getOrder(i).getWait()==3000){
							if(orderlist.getOrder(i).getCompete().size()!=0)
							{
								int chosen = orderlist.getOrder(i).ChooseTaxi(this.map.getDistance());
								if(chosen!=-1){
									alltaxi[chosen].setOrder(orderlist.getOrder(i));
									alltaxi[chosen].setstate(2);
								}
								else
								{
									try{ 
								        FileWriter fw = new FileWriter(orderlist.getOrder(i).getFile(),true);
								        fw.write("No taxi answer the order:("+LINE_SEPARATOR);
								        fw.close();
									}catch(Exception e){}
								}
							}
							else//没有车接单
							{
								try{ 
							        FileWriter fw = new FileWriter(orderlist.getOrder(i).getFile(),true);
							        fw.write("No taxi answer the order:("+LINE_SEPARATOR);
							        fw.close();
								}catch(Exception e){}
							}
							orderlist.RemoveAt(i);
							i--;
						}
					}
				}
				
				try{
					Thread.sleep(200);
				}catch(InterruptedException e){}
			}
		}catch(Exception e){}
	}
}
