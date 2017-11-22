package uber;

import java.util.ArrayList;

public class Orderlist {
	private ArrayList<Order> List;
	Orderlist() 
	{
		this.List = new ArrayList<Order>();
	}
	public Order getOrder(int index)
	{
		return this.List.get(index);
	}
	public int getsize()
	{
		return this.List.size();
	}
	public synchronized void Add(Order r)
	{
		this.List.add(r);
	//	System.out.println("add");
	}
	public void Remove(Order o)
	{
	//	System.out.println("remove");
		this.List.remove(o);
	}
	public void RemoveAt(int index)
	{
	//	System.out.println("remove");
		this.List.remove(index);
	}
	public ArrayList<Order> getList() 
	{
		return this.List;
	}
}
