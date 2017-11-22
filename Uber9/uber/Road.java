package uber;

import java.awt.Point;
import java.util.regex.Pattern;

public class Road extends Thread{
	private static int length = 80;
	private int[][] horz = new int[length][length-1]; //横边
	private int[][] vert = new int[length-1][length]; //竖边
	private int[][] flowh = new int[length][length-1]; //横边流量
	private int[][] flowv = new int[length-1][length]; //竖边流量
	private Map map;
	private Taxi[] taxis;
	public int getflowh(int i, int j){
		/*@Requires:横边坐标
		  @Modifies:无
		  @Effects:返回相应横边的流量
		*/
		return this.flowh[i][j];}
	public int getflowv(int i, int j){
		/*@Requires:竖边坐标
		  @Modifies:无
		  @Effects:返回相应竖边的流量
		*/
		return this.flowv[i][j];}
	public void sethorz(int i, int j, int v){
		/*@Requires:横边坐标，int类型参数(是否打开)
		  @Modifies:无
		  @Effects:将相应横边设置为传入参数
		*/
		this.horz[i][j]=v;}
	public void setvert(int i, int j, int v){
		/*@Requires:竖边坐标，int类型参数(是否打开)
		  @Modifies:无
		  @Effects:将相应竖边设置为传入参数
		*/
		this.vert[i][j]=v;}
	public int gethorz(int i, int j){
		/*@Requires:横边坐标
		  @Modifies:无
		  @Effects:返回相应横边的开关情况
		*/
		return this.horz[i][j];}
	public int getvert(int i, int j){
		/*@Requires:竖边坐标
		  @Modifies:无
		  @Effects:返回相应竖边的开关情况
		*/
		return this.vert[i][j];}
	public void setTaxi(Taxi[] t){
		/*@Requires:Taxi[]
		  @Modifies:无
		  @Effects:将Road的出租车序列设置为传入参数
		*/
		this.taxis = t;}
	Road(Map map)
	{
		/*@Requires:Map
		  @Modifies:无
		  @Effects:根据传入参数构造一个Road对象
		*/
		this.map = map;
	//	this.taxis = t;
		for(int i=0;i<length;i++){
			for(int j=0;j>length-1;j++)
			{
				this.horz[i][j] = 0;
				this.vert[j][i] = 0;
				this.flowh[i][j] = 0;
				this.flowv[j][i] = 0;
			}
		}
	}
	
	public void initial()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:根据地图属性中每个点的value对每一条边进行初始化
		*/
		int[][] value = new int[length][length];
		value = this.map.getValue();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				switch(value[i][j])
				{
					case 0:
						break;
					case 1:
						this.horz[i][j] = 1;
						break;
					case 2:
						this.vert[i][j] = 1;
						break;
					case 3:
						this.horz[i][j] = 1;
						this.vert[i][j] = 1;
						break;
				}
			}
		}
	}
	public void clear()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:将所有边的流量清零
		*/
		for(int i=0;i<length;i++){
			for(int j=0;j>length-1;j++)
			{
				this.flowh[i][j] = 0;
				this.flowv[j][i] = 0;
			}
		}
	}
	public static void ParseOpenRequest(String s, TaxiGUI gui, Map map)
	{
		/*@Requires:输入的字符串、GUI、地图
		  @Modifies:s,gui,map，system.out
		  @Effects:根据字符串打开地图上的相应道路，并在GUI上显示；如果是无效Request或解析失败，则输出相应报错信息
		*/
		int a,b,c,d;
		String temp = s;
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
			System.out.println("Invalid request: "+temp);
		}
		else if(a==c && b==d)
		{
			System.out.println("Invalid request(not a road): "+temp);
		}
		else
		{
			if(a==c&&b==d-1)//(a,b)--(c,d)
			{
				if(map.getRoad().gethorz(a, b)==1)
					System.out.println("The road is already opened： "+temp);
				else{
					map.changeValue(a, b, 1);
					map.getRoad().sethorz(a, b, 1);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 1);
				}
			}
			else if(a==c&&b==d+1)//(c,d)--(a,b)
			{
				if(map.getRoad().gethorz(c, d)==1)
					System.out.println("The road is already opened： "+temp);
				else{
					map.changeValue(c, d, 1);
					map.getRoad().sethorz(c, d, 1);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 1);
				}
			}
			else if(b==d&&a==c-1)//(a,b)/(c,d)
			{
				if(map.getRoad().getvert(a, b)==1)
					System.out.println("The road is already opened： "+temp);
				else{
					map.changeValue(a, b, 2);
					map.getRoad().setvert(a, b, 1);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 1);
				}
			}
			else if(b==d&&a==c+1)//(c,d)/(a,b)
			{
				if(map.getRoad().getvert(c, d)==1)
					System.out.println("The road is already opened： "+temp);
				else{
					map.changeValue(c, d, 2);
					map.getRoad().setvert(c, d, 1);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 1);
				}
			}
			else
				System.out.println("Invalid request(not a road): "+temp);
		}
	}
	public static void ParseCloseRequest(String s, TaxiGUI gui, Map map)
	{
		/*@Requires:输入的字符串、GUI、地图
		  @Modifies:s,gui,map，system.out
		  @Effects:根据字符串关闭地图上的相应道路，并在GUI上显示；如果是无效Request或解析失败，则输出相应报错信息
		*/
		int a,b,c,d;
		String temp = s;
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
			System.out.println("Invalid request: "+temp);
		}
		else if(a==c && b==d)
		{
			System.out.println("Invalid request(not a road): "+temp);
		}
		else
		{
			if(a==c&&b==d-1)//(a,b)--(c,d)
			{
				if(map.getRoad().gethorz(a, b)==0)
					System.out.println("The road is already closed： "+temp);
				else{
					map.changeValue(a, b, -1);
					map.getRoad().sethorz(a, b, 0);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 0);
				}
			}
			else if(a==c&&b==d+1)//(c,d)--(a,b)
			{
				if(map.getRoad().gethorz(c, d)==0)
					System.out.println("The road is already closed： "+temp);
				else{
					map.changeValue(c, d, -1);
					map.getRoad().sethorz(c, d, 0);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 0);
				}
			}
			else if(b==d&&a==c-1)//(a,b)/(c,d)
			{
				if(map.getRoad().getvert(a, b)==0)
					System.out.println("The road is already closed： "+temp);
				else{
					map.changeValue(a, b, -2);
					map.getRoad().setvert(a, b, 0);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 0);
				}
			}
			else if(b==d&&a==c+1)//(c,d)/(a,b)
			{
				if(map.getRoad().getvert(c, d)==0)
					System.out.println("The road is already closed： "+temp);
				else{
					map.changeValue(c, d, -2);
					map.getRoad().setvert(c, d, 0);
					gui.SetRoadStatus(new Point (a,b), new Point (c,d), 0);
				}
			}
			else
				System.out.println("Invalid request(not a road): "+temp);
		}
	}
	public void run()
	{
		/*@Requires:出租车200ms前后的位置信息
		  @Modifies:无
		  @Effects:计算每条路的流量，每200ms更新一次
		*/
	//	this.initial();
		while(true)
		{
			this.clear();
			for(Taxi taxi: taxis)
			{
				
				switch(taxi.calculateFlow()){
					case -1://STILL
						break;
					case 0://UP
						this.flowv[taxi.getx()][taxi.gety()]++;
						break;
					case 1://DOWN
						this.flowv[taxi.getx()-1][taxi.gety()]++;
						break;
					case 2://LEFT
						this.flowh[taxi.getx()][taxi.gety()]++;
						break;
					case 3://RIGHT
						this.flowh[taxi.getx()][taxi.gety()-1]++;
						break;
				}
			}
			
			try{
				Thread.sleep(200);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

