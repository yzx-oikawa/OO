package elevator;

import java.util.ArrayList;

public class Requestlist {
	private String name;
	private ArrayList<Request> List;
	public static boolean[] button;
	Requestlist(String name) 
	{
		this.List = new ArrayList<Request>();
		this.name = name;
	}
	public Request getRequest(int index)
	{
		return this.List.get(index);
	}
	public int getsize()
	{
		return this.List.size();
	}
	public synchronized void Add(Request r)
	{
//		while (full = false){
	//		try{
				this.List.add(r);
		//		wait();  } catch (InterruptedException e) { }  
//			System.out.println("add "+this.name);
//		full = false;
//		notifyAll();
		
	}
	public synchronized void Remove(int index)
	{
//		while (full = true){
//			try{
				this.List.remove(index);
//				wait();  } catch (InterruptedException e) { }  
//			}
//		full = true;
//				System.out.println("remove "+this.name);
//		notifyAll();

	}

}
	

