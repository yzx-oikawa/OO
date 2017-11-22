package uber;

import java.util.ArrayList;

public class Orderlist {
	private ArrayList<Order> List;
	Orderlist() 
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:构造一个Orderlist对象
		*/
		this.List = new ArrayList<Order>();
	}
	public Order getOrder(int index)
	{
		/*@Requires:int类型的参数(订单序列中的第几项)
		  @Modifies:无
		  @Effects:参数是几就返回订单序列中的第几项
		*/
		return this.List.get(index);
	}
	public int getsize()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单序列的长度
		*/
		return this.List.size();
	}
	public void Add(Order r)
	{
		/*@Requires:Order
		  @Modifies:无
		  @Effects:将传入参数加入订单序列
		*/
		this.List.add(r);
	//	System.out.println("add");
	}
	public void Remove(Order o)
	{
		/*@Requires:Order
		  @Modifies:无
		  @Effects:将传入参数从订单序列中删除
		*/
	//	System.out.println("remove");
		this.List.remove(o);
	}
	public void RemoveAt(int index)
	{
		/*@Requires:int类型的参数(订单序列中的第几项)
		  @Modifies:无
		  @Effects:参数是几就将订单序列中的第几项删除
		*/
	//	System.out.println("remove");
		this.List.remove(index);
	}
	public ArrayList<Order> getList() 
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单序列
		*/
		return this.List;
	}
}
