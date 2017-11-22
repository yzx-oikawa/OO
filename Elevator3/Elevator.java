package elevator;

import java.util.ArrayList;

public class Elevator implements Move{
	private int PresentFloor;
	private int TargetFloor;
	private int state;
	private Request mainReq;
	private ArrayList<Request> carriedReq;
	Elevator(int x)
	{
		this.PresentFloor = x;
		this.state = 0;
		this.mainReq = null;
		ArrayList<Request> r = new ArrayList<Request>();
		this.carriedReq = r;
	}
	public int getPresentFloor(){
		return this.PresentFloor;}
	public void setPresentFloor(int x){
		this.PresentFloor = x;}
	public int getTargetFloor(){
		return this.TargetFloor;
	}
	@Override
	public int getState(){
		return this.state;
	}
	public void setState(){
		int Pf = this.PresentFloor;
		int Tf = this.mainReq.getmn();
		if(Tf>Pf)
			this.state = 1;
		else if(Tf<Pf)
			this.state = -1;
		else if(Tf==Pf)
			this.state = 0;
		this.TargetFloor = Tf;
	}
	public Request getMainReq(){
		return this.mainReq;
	}
	public void setMainReq (Request r){
		this.mainReq = r;
	}
	public ArrayList<Request> getCR(){
		return this.carriedReq;
	}
	public Request getcarriedReq(int index){
		return this.carriedReq.get(index);
	}
	public int getCRsize(){
		return this.carriedReq.size();
	}
	public void setcarriedReq(EleSys es, Requestlist rl, Elevator e){
		this.carriedReq.addAll(NewDispatch.getAll(es, rl, e));
	}
	public void addcarriedReq(ArrayList<Request> list){
		this.carriedReq.addAll(list);
	}
	public void ChangePresentFloor(Elevator e, Request r, int n)
	{
		if (r.getmn()-e.getPresentFloor()>0)//UP
			this.PresentFloor += n;
		else if(r.getmn()-e.getPresentFloor()<0)//DOWN
			this.PresentFloor -= n;
	}
	public void sortCR(int i, int j)
	{
		Request temp = new Request();
		if (this.getcarriedReq(i).getLine()>this.getcarriedReq(j).getLine())
		{
			temp.setRequest(this.getcarriedReq(i));
			this.getcarriedReq(i).setRequest(this.getcarriedReq(j));
		
			this.getcarriedReq(j).setRequest(temp);  
		}
		
	}
	public String toString()
	{
		String s = new String(this.PresentFloor+" "+this.TargetFloor+" "+this.state);
		return s;
	}
}

