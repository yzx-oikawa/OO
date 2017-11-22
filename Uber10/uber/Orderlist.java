package uber;

import java.util.ArrayList;

public class Orderlist {
	/* Overview:是一个Order对象的链表，包括从链表中取项、添加项、删除项、返回链表长度等操作
	 * 抽象函数：AF(c)=(List) where List==c.List
	 * 不变式：c.List!=null
     * */
	private ArrayList<Order> List;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(List==null)
			return false;
		return true;
	}
	Orderlist() 
	{
		/*@Requires:无
		  @Modifies:this
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
		  @Modifies:this.List
		  @Effects:将传入参数加入订单序列
		*/
		this.List.add(r);
	//	System.out.println("add");
	}
	public void Remove(Order o)
	{
		/*@Requires:Order
		  @Modifies:this.List
		  @Effects:将传入参数从订单序列中删除
		*/
	//	System.out.println("remove");
		this.List.remove(o);
	}
	public void RemoveAt(int index)
	{
		/*@Requires:int类型的参数(订单序列中的第几项)
		  @Modifies:this.List
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
