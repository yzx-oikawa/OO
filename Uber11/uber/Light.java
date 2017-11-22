package uber;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Light extends Thread{
	/* Overview:线程类; 保存了地图上每个点的红绿灯是否存在、绿灯的方向、以及车是否能通过路口等信息;
	      			      根据随机生成的间隔时间(50-100ms中的一个值)改变红绿灯的方向
	 * 抽象函数：AF(c)=(interval, signal, direction, pass, gui)  
	         where interval==c.interval, signal==c.signal, direction==c.direction,
	         	   pass==c.pass, gui==c.gui
	 * 不变式：50<=c.interval<=100 && for(0<=i,j<80) c.signal[i][j]=0||c.signal[i][j]=1) &&
			(c.direction=0||c.direction=1)
	 * */
	private static int length = 80;
	private long interval;//随机生成的间隔时间
	private int[][] signal = new int[length][length];//是否有灯
	private int direction;//0-南北绿灯，1-东西绿灯
	private boolean[][] pass = new boolean[4][4];//0上1下2左3右，第一个表示从哪儿来，第二个表示到哪儿去
	private TaxiGUI gui;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(interval<50 || interval>100 || (direction!=0 && direction!=1))
			return false;
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				if(signal[i][j]!=0 && signal[i][j]!=1)
					return false;
			}
		}
		return true;
	}
	Light(TaxiGUI gui)
	{
		/*@Requires:GUI
		  @Modifies:this
		  @Effects:创建一个Light对象并初始化;取一个200-500之间的随机数，设定为红绿灯改变颜色的间隔时间;再随机设定红绿灯开始时刻的方向
		*/
		this.interval = new Random().nextInt(301);
		this.interval += 200;
		this.direction = new Random().nextInt(2);
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.signal[i][j] = 0;
			}
		}
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				this.pass[i][j] = true;
			}
		}
		this.gui = gui;
	}
	public int getsignal(int x, int y)
	{
		/*@Requires:路口的xy坐标(int类型参数)
		  @Modifies:无
		  @Effects:返回路口是否有灯的int，如果有，返回1；如果没有，返回0
		*/
		return this.signal[x][y];
	}
	public boolean getpass(int from, int to)
	{
		/*@Requires:表示来的方向和去的方向的int类型参数
		  @Modifies:无
		  @Effects:返回路口是否能通过的boolean
		*/
		return this.pass[from][to];
	}
	public Light SetLight(File f)
	{
 		/*@Requires:light.txt
 		  @Modifies:System.out
 		  @Effects:返回一个light对象；如果是无效文件或解析失败，则返回null
 		*/	
		String Line;
		String[] tempString = new String[length];
		int[] tempInt = new int[length];
		BufferedReader reader;
		int line = 0;
		try {
			reader = new BufferedReader(new FileReader(f));
			while ((Line = reader.readLine()) != null && line < length) {
				tempString = Line.split("");
				if(tempString.length!=80){
					System.out.println("Invalid Traffic Lights!");
					reader.close();
					return null;
				}
				for(int i=0;i<length; i++){
					tempInt[i] = Integer.parseInt(tempString[i]);
					if(tempInt[i]>1)
					{
						System.out.println("Invalid Traffic Lights!");
						reader.close();
						return null;
					}else{
						this.signal[line][i] = tempInt[i];
					}
				}
				line++;
			}
				reader.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			return this;
		}
	public void ChangeDirection()
	{
		/*@Requires:无
		  @Modifies:this.pass, this.direction
		  @Effects:改变两组红绿灯的颜色
		*/
		if(direction==0)//南北绿灯
		{
			pass[0][1] = true;
			pass[1][0] = true;
			pass[2][0] = true;
			pass[3][1] = true;
			pass[0][3] = false;
			pass[1][2] = false;
			pass[2][3] = false;
			pass[3][2] = false;
			direction = 1;
		}
		else if(direction==1)
		{
			pass[0][3] = true;
			pass[1][2] = true;
			pass[2][3] = true;
			pass[3][2] = true;
			pass[0][1] = false;
			pass[1][0] = false;
			pass[2][0] = false;
			pass[3][1] = false;
			direction = 0;
		}
	}
	//public void main(String args[])
	public void run()
	{
		/*@Requires:无
		  @Modifies:this, GUI
		  @Effects:根据该间隔时间改变两组红绿灯的颜色
		*/
//		for(int i=0;i<length;i++){
//			for(int j=0;j<length;j++){
//				System.out.print(signal[i][j]+"");
//			}
//			System.out.println();
//		}
		this.ChangeDirection();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				if(this.signal[i][j]==0)
					gui.SetLightStatus(new Point(i,j), 0);
				else{
					if(this.direction==0)//0-南北 1-东西
						gui.SetLightStatus(new Point(i,j), 1);
					else
						gui.SetLightStatus(new Point(i,j), 2);
				}
			}
		}
	//	gui.SetLightStatus(Point p,int status);//设置红绿灯 status 0 没有红绿灯 1 东西方向为绿灯 2 东西方向为红灯
	//	System.out.println(direction);
		//车get的时候只get那个boolean的数组，不get direction的属性
		while(true)
		{
			this.ChangeDirection();
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++){
					if(this.signal[i][j]==1){
						if(this.direction==0)//0-南北 1-东西
							gui.SetLightStatus(new Point(i,j), 1);
						else
							gui.SetLightStatus(new Point(i,j), 2);
					}
				}
			}
		//	System.out.println(direction);
		//	System.out.println(System.currentTimeMillis());
			try{
				Thread.sleep(interval);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
