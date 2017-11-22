package elevator;

import java.util.ArrayList;

public class Requestlist {
	private ArrayList<Request> List;
	Requestlist() 
	{
		this.List = new ArrayList<Request>();
	}
	public Request getRequest(int index)
	{
		return this.List.get(index);
	}
	public int getsize()
	{
		return this.List.size();
	}
	public void Add(Request r)
	{
		this.List.add(r);
	}
	public void Remove(int index)
	{
		this.List.remove(index);
	}
}
