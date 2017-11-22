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
//	public void Output()
//	{
//		for(int i=0;i<this.List.size();i++)
//		{
//			System.out.print("ERorFR="+this.List.get(i).getEF()+" ");
//			System.out.print("UPorDOWN="+this.List.get(i).getUD()+" ");
//			System.out.print("m="+this.List.get(i).getm()+" ");
//			System.out.print("n="+this.List.get(i).getn()+" ");
//			System.out.print("T="+this.List.get(i).getT()+" ");
//			System.out.print("\n");
//		}
//	}
}
