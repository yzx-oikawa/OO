package uber;

import java.io.FileWriter;

public class Dispatch extends Thread{
	/* Overview:线程类;负责将订单序列里的订单根据信用度和路径长度分配给出租车
	 * 抽象函数：AF(c)=(map, alltaxi, orderlist) where map==c.map, alltaxi==c.alltaxi, orderlist==c.orderlist
	 * 不变式：c.map!=null && c.alltaxi!=null && c.orderlist!=null && c.alltaxi.length<=100
	 * */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Map map;
	private Taxi[] alltaxi;
	private Orderlist orderlist;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(map==null||alltaxi==null||orderlist==null)
			return false;
		if(alltaxi.length>100)
			return false;
		return true;
	}
	public Map getMap() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回地图
		*/
		return map;}
	public Taxi[] getAlltaxi() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车序列
		*/
		return alltaxi;}
	public Orderlist getOrderlist() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单序列
		*/
		return orderlist;}
	Dispatch(Map map, Taxi[] alltaxi, Orderlist OL)
	{
		/*@Requires:地图，出租车序列，订单序列
		  @Modifies:this
		  @Effects:根据传入参数创建一个Dispatch对象
		*/
		this.map = map;
		this.alltaxi = alltaxi;
		this.orderlist = OL;
	}
	public void run()
	{
		/*@Requires:无
		  @Modifies:文件,this
		  @Effects:对于订单序列中的每个订单，判断符合抢单条件的出租车，在3s结束后选择接单的出租车并将订单分配给它，然后将订单移出序列；
		  @Effects:如果没有能分配的出租车，则在文件输出相应信息之后直接将订单移出序列
		*/
		try
		{
			while(true){
//				System.out.println(orderlist.getsize());
				if(orderlist.getsize()!=0){						
					for(int i = 0;i<orderlist.getsize();i++){//对列表的每个指令遍历
						Order temp = orderlist.getOrder(i);
						if(temp.getWait()==0){
							for(Taxi t:alltaxi)
								temp.startOrder(t);
						}
						for(Taxi t:alltaxi)
						{
							if(t.getstate()==2)
								temp.JudgeTaxi(t);
						}
						orderlist.getOrder(i).addWait(200);
					//	System.out.println("debug");
						if(orderlist.getOrder(i).getWait()==3000){
							if(orderlist.getOrder(i).getCompete().size()!=0)
							{
								
								int chosen = orderlist.getOrder(i).ChooseTaxi(this.map.BFSdis(orderlist.getOrder(i).getSrcX(), orderlist.getOrder(i).getSrcY()));
								if(chosen!=-1){
									alltaxi[chosen].setOrder(orderlist.getOrder(i));
									alltaxi[chosen].setstate(3);
								}
								else
								{
									try{ 
								        FileWriter fw = new FileWriter(orderlist.getOrder(i).getFile(),true);
								        fw.write("No taxi answer the order:("+LINE_SEPARATOR);
								        fw.close();
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}
							else//没有车接单
							{
								try{ 
							        FileWriter fw = new FileWriter(orderlist.getOrder(i).getFile(),true);
							        fw.write("No taxi answer the order:("+LINE_SEPARATOR);
							        fw.close();
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							orderlist.RemoveAt(i);
							i--;
						}
					}
				}
				try{
					Thread.sleep(200);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	//	System.out.println("end");
	}
}
