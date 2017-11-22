package uber;

import java.awt.Point;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.File;   
import java.io.FileWriter;

public class Order{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static int length = 80;
	private int srcX;
	private int srcY;
	private int dstX;
	private int dstY;
	private long time;
	private int wait;
	private ArrayList<Taxi> compete;
	private TaxiGUI gui;
	private File file;
	public int getSrcX() {return srcX;}
	public void setSrcX(int srcX) {this.srcX = srcX;}
	public int getSrcY() {return srcY;}
	public void setSrcY(int srcY) {this.srcY = srcY;}
	public int getDstX() {return dstX;}
	public void setDstX(int dstX) {this.dstX = dstX;}
	public int getDstY() {return dstY;}
	public void setDstY(int dstY) {this.dstY = dstY;}
	public long getTime() {return this.time;}
	public void setTime(long time) {this.time = time;}
	public double getWait() {return this.wait;}
	public void addWait(int wait) {this.wait += wait;}
	public ArrayList<Taxi> getCompete(){return this.compete;}
	public File getFile(){return this.file;}
	
	
	Order(){}
	Order(int x0, int y0, int x, int y, long T, TaxiGUI gui)
	{
		this.srcX = x0;
		this.srcY = y0;
		this.dstX = x;
		this.dstY = y;
		this.time = T;
		this.wait = 0;
		this.compete = new ArrayList<Taxi>();
		this.gui = gui;
		try{ 
			this.file = new File("D://testtaxi//"+T+"("+x0+","+y0+")-("+x+","+y+").txt");
	        FileWriter fw = new FileWriter(this.file,true);
	        fw.close();
			}catch(Exception e){}
	}
	
	public static Order ParseOrder(String s, long T, TaxiGUI gui)
	{
		int a,b,c,d;
		String temp = s;
		s = s.replace(" ", "");
	//	System.out.println(s);
	//  [CR,   (1,   3),   (44,   55)]
		if(Pattern.matches("^(\\[CR\\,\\(\\d{1,2}\\,\\d{1,2}\\)\\,\\(\\d{1,2}\\,\\d{1,2}\\)\\])$", s)==true)
		{
			s = s.replace("(", "");
			s = s.replace(")", "");
			s = s.replace("]", "");
			String[] string = s.split(",");
			a = Integer.parseInt(string[1]);
			b = Integer.parseInt(string[2]);
			c = Integer.parseInt(string[3]);
			d = Integer.parseInt(string[4]);
			if(a>79 || b>79 || c>79 || d>79)
			{
				System.out.println("Invalid order: "+temp);
				return null;
			}
			else if(a==c && b==d)
			{
				System.out.println("Invalid order(src=dst): "+temp);
				return null;
			}
			else
			{
				Order o = new Order(a,b,c,d,T,gui);
				gui.RequestTaxi(new Point(o.srcY, o.srcX), new Point(o.dstY, o.dstX));
				return o;
			}
		}
		else{
			System.out.println("Invalid input: "+ temp);
			return null;
		}
	}
	
	public int ChooseTaxi(short[][] distance)
	{
		Taxi best = new Taxi();
		best = this.compete.get(0);
	//	System.out.println(best.getNo());
	//	System.out.println(this.compete.size());
		if(this.compete.size()>1)
		{
			for(int i=0;i<this.compete.size();i++)
			{
				if(this.compete.get(i).getstate()!=3)
				{
					this.compete.remove(i);
					i--;
				}else{
					if(best.getCredit()<this.compete.get(i).getCredit())
					{
						this.compete.remove(best);
						best = this.compete.get(i);
						i--;
					}else if(best.getCredit()>this.compete.get(i).getCredit()){
						this.compete.remove(i);
						i--;
					}
				}
			}
		}
		if(this.compete.size()>1)
		{
			for(int i=0;i<this.compete.size();i++)
			{
				int no = this.srcY*length+this.srcX;
				if(distance[best.gety()*length+best.getx()][no]
				  >distance[this.compete.get(i).gety()*length+this.compete.get(i).getx()][no])
				{
					this.compete.remove(best);
					best = this.compete.get(i);
					i--;
				}else if(distance[best.gety()*length+best.getx()][no]
						<distance[this.compete.get(i).gety()*length+this.compete.get(i).getx()][no]){
					this.compete.remove(this.compete.get(i));
					i--;
				}
			}
		}
		if(best.getstate()!=3)
		{
			return -1;
		}
		try{ 
	        FileWriter fw = new FileWriter(this.file,true);
	        fw.write("Choose: No."+best.getNo()+LINE_SEPARATOR);
	        fw.close();
			}catch(Exception e){}
	//	System.out.println("Choose:"+best.getNo());
		return best.getNo();
	}
	public void JudgeTaxi(Taxi t)//during 3s, judge every 200ms
	{
		int x = t.getx();
		int y = t.gety();
		int flag = 0;
		
		if(this.compete.size()!=0)
		{
			for(Taxi taxi: compete)
			{
				if (t.getNo() == taxi.getNo())
				{
					flag = 1;
					break;
				}
			}
		}
		if(x<=srcX+2&&x>=srcX-2&&y<=srcY+2&&y>=srcY-2&& flag==0)
		{
	//		System.out.println("order add taxi:"+t.getNo()+" at "+t.getx()+","+t.gety());
	//		System.out.println(t.getNo());
			try{ 
		        FileWriter fw = new FileWriter(this.file,true);
		        fw.write("Taxi in compete: No."+t.getNo()+" at ("+t.getx()+","+t.gety()+")"+LINE_SEPARATOR);
		        fw.close();
				}catch(Exception e){}
			this.compete.add(t);
			t.addCredit(1);
		}
	//	System.out.println("judge");
	}
	public void startOrder(Taxi t)
	{
		int x = t.getx();
		int y = t.gety();
		if(x<=srcX+2&&x>=srcX-2&&y<=srcY+2&&y>=srcY-2)
		{
			try{ 
		        FileWriter fw = new FileWriter(this.file,true);
		        fw.write("Taxi No."+t.getNo()+": at ("+t.getx()+","+t.gety()+") status:"+t.getstate()+" credit:"+t.getCredit()+LINE_SEPARATOR);
		        fw.close();
				}catch(Exception e){}
		}
	}

	
}
