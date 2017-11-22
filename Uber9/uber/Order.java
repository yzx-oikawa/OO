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
	public int getSrcX() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单发出点的x坐标
		*/
		return srcX;}
	public int getSrcY() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单发出点的y坐标
		*/
		return srcY;}
	public int getDstX() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单目标点的x坐标
		*/
		return dstX;}
	public int getDstY() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单目标点的y坐标
		*/
		return dstY;}
	public long getTime() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单的生成时间
		*/
		return this.time;}
	public double getWait() {
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单的等待时间
		*/
		return this.wait;}
	public void addWait(int wait) {
		/*@Requires:int类型的参数(毫秒数)
		  @Modifies:无
		  @Effects:让订单的等待时间增加传入参数的值
		*/
		this.wait += wait;}
	public ArrayList<Taxi> getCompete(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单的抢单车辆序列
		*/
		return this.compete;}
	public File getFile(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回订单的输出文件
		*/
		return this.file;}
	
	
	Order(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:构造一个Order对象
		*/
	}
	Order(int x0, int y0, int x, int y, long T, TaxiGUI gui)
	{
		/*@Requires:四个int类型参数，分表表示订单发出点的xy坐标、订单目标点的xy坐标
		  @Requires:一个long类型参数，表示订单发出的系统时间
		  @Requires:GUI
		  @Modifies:无
		  @Effects:根据传入参数构造一个Order对象
		*/
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
		/*@Requires:输入的字符串、当前系统时间、GUI
		  @Modifies:s,gui
		  @Effects:生成一个Order并返回；如果是无效Order或解析失败，则返回null
		*/
		int a,b,c,d;
		String temp = s;
		s = s.replace(" ", "");
	//	System.out.println(s);
	//  [CR,   (1,   3),   (44,   55)]
		if(Pattern.matches("^(\\[CR\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\])$", s)==true)
		{
			s = s.replace("(", "");
			s = s.replace(")", "");
			s = s.replace("]", "");
			s = s.replace("+", "");
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
				gui.RequestTaxi(new Point(a,b), new Point(c,d));
				return o;
			}
		}
		else{
			System.out.println("Invalid input: "+ temp);
			return null;
		}
	}
	
	public int ChooseTaxi(short[] distance)
	{
		/*@Requires:所有出租车到订单发出点的最短路径的距离数组
		  @Modifies:无
		  @Effects:返回选中的车的序号；如果没有符合条件的车，返回-1
		*/
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
						Taxi temp = new Taxi();
						temp = this.compete.get(i);
						this.compete.remove(best);
						best = temp;
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
				//int no = this.srcY*length+this.srcX;
				if(distance[best.gety()*length+best.getx()]
				  >distance[this.compete.get(i).gety()*length+this.compete.get(i).getx()])
				{
					Taxi temp = new Taxi();
					temp = this.compete.get(i);
					this.compete.remove(best);
					best = temp;
					i--;
				}else if(distance[best.gety()*length+best.getx()]
						<distance[this.compete.get(i).gety()*length+this.compete.get(i).getx()]){
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
		
		/*@Requires:每一辆出租车
		  @Modifies:无
		  @Effects:如果出租车满足抢单要求，将其加入订单的抢单序列；否则不加入
		*/
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
		/*@Requires:每一辆出租车
		  @Modifies:无
		  @Effects:用于请求发出时刻输出符合抢单条件的出租车的具体信息(位置、状态、信用)到文件；如果没有符合条件的车，则不输出
		*/
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
