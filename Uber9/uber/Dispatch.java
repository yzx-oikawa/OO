package uber;

import java.io.FileWriter;

public class Dispatch extends Thread{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Map map;
	private Taxi[] alltaxi;
	private Orderlist orderlist;
	public Map getMap() {return map;}
	public void setMap(Map map) {
		/*@Requires:Map
		  @Modifies:无
		  @Effects:设置Dispatch中的Map属性
		*/
		this.map = map;}
	public Taxi[] getAlltaxi() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回出租车序列
		*/
		return alltaxi;}
	public void setAlltaxi(Taxi[] alltaxi) {
		/*@Requires:Map
		  @Modifies:无
		  @Effects:设置Dispatch中的出租车序列属性
		*/
		this.alltaxi = alltaxi;}
	public Orderlist getOrderlist() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单序列
		*/
		return orderlist;}
	public void setOrderlist(Orderlist orderlist) {
		/*@Requires:Map
		  @Modifies:无
		  @Effects:设置Dispatch中的订单序列属性
		*/
		this.orderlist = orderlist;}
	Dispatch(Map map, Taxi[] alltaxi, Orderlist OL)
	{
		/*@Requires:地图，出租车序列，订单序列
		  @Modifies:无
		  @Effects:根据传入参数创建一个Dispatch序列
		*/
		this.map = map;
		this.alltaxi = alltaxi;
		this.orderlist = OL;
	}
	public void run()
	{
		/*@Requires:无
		  @Modifies:文件
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
							if(t.getstate()==3)
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
									alltaxi[chosen].setstate(2);
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
