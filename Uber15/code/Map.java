package uber;

import java.io.BufferedReader;
import java.io.File;   
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 

public class Map {
	/* Overview:保存地图中点和点的连接情况及道路; 能够计算两点之间的最短路径并保存路径每一步的方向
	 * 抽象函数：AF(c)=(value, points, road) where value==c.value, points==c.points, road==c.road
	 * 不变式：for(0<i,j<80) 0<=c.value[i][j]<=3 && c.points[i][j]!=null && c.road!=null
	 * */
	private static int length = 80;
	private int value[][] = new int[length][length];
	private Points points[][] = new Points[length][length];
	private Road road;
	public boolean repOK(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				if(value[i][j]<0||value[i][j]>3)
					return false;
				if(points[i][j]==null)
					return false;
				
			}
		}
		if(road==null)
			return false;
		return true;
	}
	Map(){
		/*@Requires:无
		  @Modifies:this
		  @Effects:构造一个Map对象
		*/
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.value[i][j]=0;
				this.points[i][j]=new Points(0);
			}
		}
		this.road = new Road(this);
	}
	public Points getPoint(int i, int j){
		/*@Requires:点坐标
		  @Modifies:无
		  @Effects:返回对应点
		*/
		return this.points[i][j];}
	public int[][] getValue(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回所有点的value数组
		*/
		return this.value;}
	public Road getRoad(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:返回该对象的Road属性
		*/
		return this.road;}
	public void changeValue(int i, int j, int v)
	{	
		/*@Requires:点坐标和修改的差值
		  @Modifies:this.points
		  @Effects:修改对应点的value和相关边的打开与关闭
		*/
		this.points[i][j].addValue(v);
		switch(v){
			case 1:
				this.points[i][j].setRight(true);
				this.points[i][j+1].setLeft(true);
				break;
			case -1:
				this.points[i][j].setRight(false);
				this.points[i][j+1].setLeft(false);
				break;
			case 2:
				this.points[i][j].setDown(true);
				this.points[i+1][j].setUp(true);
				break;
			case -2:
				this.points[i][j].setDown(false);
				this.points[i+1][j].setUp(false);
				break;
		}
	}
 	public static Map ParseMap(File f)
	{
 		/*@Requires:map.txt
 		  @Modifies:System.out
 		  @Effects:返回一个地图对象；如果是无效地图或解析失败，则返回null
 		*/
 		Map map = new Map();
 		int no = 0;
		for(int i = 0;i<length;i++)
			for(int j = 0;j<length;j++)
				map.points[i][j] = new Points(no++,i,j,0);	
		String Line;
		String[] tempString = new String[length];
		int[] tempInt = new int[length];
		BufferedReader reader;
		int line = 0;
		try {
			reader = new BufferedReader(new FileReader(f));
			while ((Line = reader.readLine()) != null && line < length) {
			//	System.out.println("line " + line + ": " + tempString);
				tempString = Line.split("");
				if(tempString.length!=80){
					System.out.println("Invalid map!");
					reader.close();
					return null;
				}
				for(int i=0;i<length; i++){
					tempInt[i] = Integer.parseInt(tempString[i]);
			//		System.out.print(tempInt[i]+ " ");
					map.points[line][i].setValue(tempInt[i]);
					map.value[line][i] = tempInt[i];
					map.points[line][i].Initial(tempInt[i]);
					switch(map.points[line][i].getValue()){
					case 0:
						break;
					case 1:
						map.points[line][i+1].Initial(-1);
						break;
					case 2:
						map.points[line+1][i].Initial(-2);
						break;
					case 3:
						map.points[line][i+1].Initial(-1);
						map.points[line+1][i].Initial(-2);
						break;
					default:
						System.out.println("Invalid map!");
						return null;
			}
				}
		//		System.out.println();    
                line++;
            }
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	//	map.CreateLight();
		return map;
	}
//	public void CreateLight()
//	{
//		/*@Requires:无
//		  @Modifies:light.txt
//		  @Effects:返回一个说明红绿灯分布位置的.txt文件
//		*/
//		int[][] light = new int[length][length];//0-没灯 1-有灯
//		for(int i=0;i<length;i++)
//			for(int j=0;j<length;j++)
//				light[i][j] = 0;
//		int count = 1920;//6400*30%
//		int[] visit = new int[length*length];
//		for(int i=0;i<length*length;i++)
//			visit[i] = 0;
//		while(count>0)
//		{
//			int x = new Random().nextInt(length);
//			int y = new Random().nextInt(length);
//			if(visit[x*length+y]==0){
//				visit[x*length+y] = 1;
//				if((value[x][y]==3&&(points[x][y].getUp()==true||points[x][y].getLeft()==true))||
//				  ((value[x][y]==2||value[x][y]==1)&&points[x][y].getUp()==true&&points[x][y].getLeft()==true))
//				{
//					light[x][y]=1;
//					count--;
//				}
//			}
//		}
//		
////		for(int i=0;i<length;i++){
////			for(int j=0;j<length;j++){
////				System.out.print(light[i][j]+" ");
////			}
////			System.out.println();
////		}
//		try{ 
//	        FileWriter fw = new FileWriter("C://Users//Administrator//Desktop//light.txt");
//	        for(int i=0;i<length;i++){
//				for(int j=0;j<length;j++){
//					fw.write(light[i][j]+"");
//				}
//				 fw.write(LINE_SEPARATOR);
//	        }
//	        fw.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

 	public void setRoad(Road r)
	{
		/*@Requires:Road
		  @Modifies:this.road
		  @Effects:将地图的道路设为传入参数
		*/
		this.road = r;
	}
	public short[] BFSdis(int commandX,int commandY)
	{
		/*@Requires:指令的xy坐标，坐标需要在地图内部，this.points用来标记是否已经到达
		  @Modifies:this.points
		  @Effects:返回一个short数组，数组的内容是地图上所有点到commandX，commandY的距离
		*/
		ArrayList<Points> Queue = new ArrayList<Points>();
		ArrayList<Step> Steps = new ArrayList<Step>();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.points[i][j].setVisit(0);
			}
		}
		Points current = new Points(0);
		Queue.add(this.points[commandX][commandY]);
		int no, tempX, tempY;
		tempX = commandX;
		tempY = commandY;
		
		
		short[] distance = new short[6400];
		for(int i =0;i<6400;i++)
		{
			distance[i] = 0;
		}
		distance[commandX*length+commandY] = 0;
	
		while(!Queue.isEmpty())
		{
			current = Queue.get(0);//取队中第一项
			current.setVisit(1);
			no = current.getNo();
			tempX = current.getx();
			tempY = current.gety();
			
			Queue.remove(0);
				
			if(current.getUp()==true && this.points[tempX-1][tempY].getVisit()==0)//可以往上且上面没被访问过
			{
				this.points[tempX-1][tempY].setVisit(1);
				Queue.add(this.points[tempX-1][tempY]);//可以到达的点放到队里
				distance[no-length] = (short) (distance[no]+1);	
			}			
			if(current.getDown()==true && this.points[tempX+1][tempY].getVisit()==0)//可以往下且面没被访问过
			{
				this.points[tempX+1][tempY].setVisit(1);
				Queue.add(this.points[tempX+1][tempY]);//可以到达的点放到队里
				distance[no+length] = (short) (distance[no]+1);				
			}
			if(current.getLeft()==true && this.points[tempX][tempY-1].getVisit()==0)//可以往左且上面没被访问过
			{
				this.points[tempX][tempY-1].setVisit(1);
				Queue.add(this.points[tempX][tempY-1]);//可以到达的点放到队里
				distance[no-1] = (short) (distance[no]+1);	
			}
			if(current.getRight()==true && this.points[tempX][tempY+1].getVisit()==0)//可以往you且上面没被访问过
			{
				this.points[tempX][tempY+1].setVisit(1);
				Queue.add(this.points[tempX][tempY+1]);//可以到达的点放到队里
				distance[no+1] = (short) (distance[no]+1);
			}	
		}
		return distance;
	}

	public ArrayList<Step> BFS(int taxiX,int taxiY,int x, int y)//from taxiXY to xy
	{
		/*@Requires:汽车当前坐标和目的地坐标，坐标需要在地图内部，this.points用来标记是否已经到达
		  @Modifies:this.points
		  @Effects:生成从taxiX，taxiY到x，y的最短路径的动态数组，数组的元素是路径的每一条边；如果出发点与目标点相同，则返回null
		*/
	//	System.out.println(taxiX+" "+taxiY+" "+x+" "+y);
		ArrayList<Points> Queue = new ArrayList<Points>();
		ArrayList<Step> Steps = new ArrayList<Step>();
//		ArrayList<Points> Waiting = new ArrayList<Points>();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.points[i][j].setVisit(0);
//				Waiting.add(this.points[i][j]);
			}
		}
//		System.out.println(Waiting.size());
		Points current = new Points(0);
		Queue.add(this.points[taxiX][taxiY]);
//		Waiting.remove(this.points[y][x]);
		int no, tempX, tempY;
		tempX = taxiX;
		tempY = taxiY;
		
		int[][] wholeFlow = new int[length][length];
		wholeFlow[x][y] = 0;
		int[] distance = new int[6400];
		for(int i =0;i<6400;i++)
		{
			distance[i] = 0;
		}
		distance[taxiX*length+taxiY] = 0;
	
		while(!Queue.isEmpty())
		{
			current = Queue.get(0);//取队中第一项
			current.setVisit(1);
			no = current.getNo();
	//	System.out.println(no+" "+current.getUp()+" "+current.getDown()+" "+current.getLeft()+" "+current.getRight());
			tempX = current.getx();
			tempY = current.gety();
			
			Queue.remove(0);
			
			if(tempX==x&&tempY==y)
				break;
			if(current.getUp()==true && this.points[tempX-1][tempY].getVisit()==0)//可以往上且没被访问过
			{
				this.points[tempX-1][tempY].setVisit(1);
				Queue.add(this.points[tempX-1][tempY]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no-length] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX-1,tempY,this.road.getflowv(tempX-1,tempY),1));
					wholeFlow[tempX-1][tempY] = this.road.getflowv(tempX-1,tempY);
				}else{
					wholeFlow[tempX-1][tempY] = this.road.getflowv(tempX-1,tempY)+wholeFlow[tempX][tempY];
					Steps.add(new Step(tempX,tempY,tempX-1,tempY,wholeFlow[tempX-1][tempY],1));
				}
			}
			else if(current.getUp()==true && this.points[tempX-1][tempY].getVisit()==1)//可以往上且被访问过
			{
				if(distance[no-length]==distance[no]+1)
				{
					if(wholeFlow[tempX-1][tempY]>wholeFlow[tempX][tempY]+this.road.getflowv(tempX-1,tempY))
					{
						wholeFlow[tempX-1][tempY] = wholeFlow[tempX][tempY]+this.road.getflowv(tempX-1,tempY);
						Steps.add(new Step(tempX,tempY,tempX-1,tempY,wholeFlow[tempX-1][tempY],1));
					}					
				}
			}
			
			if(current.getDown()==true && this.points[tempX+1][tempY].getVisit()==0)//可以往下且没被访问过
			{
				this.points[tempX+1][tempY].setVisit(1);
				Queue.add(this.points[tempX+1][tempY]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no+length] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX+1,tempY,this.road.getflowv(tempX,tempY),2));
					wholeFlow[tempX+1][tempY] = this.road.getflowv(tempX,tempY);
				}else{
					wholeFlow[tempX+1][tempY] = this.road.getflowv(tempX,tempY)+wholeFlow[tempX][tempY];
					Steps.add(new Step(tempX,tempY,tempX+1,tempY,wholeFlow[tempX+1][tempY],2));
				}
			}
			else if(current.getDown()==true && this.points[tempX+1][tempY].getVisit()==1)//可以往下且被访问过
			{
				if(distance[no+length]==distance[no]+1)
				{
					if(wholeFlow[tempX+1][tempY]>wholeFlow[tempX][tempY]+this.road.getflowv(tempX,tempY))
					{
						wholeFlow[tempX+1][tempY] = wholeFlow[tempX][tempY]+this.road.getflowv(tempX,tempY);
						Steps.add(new Step(tempX,tempY,tempX+1,tempY,wholeFlow[tempX+1][tempY],2));						
					}
				}				
			}
			
			if(current.getLeft()==true && this.points[tempX][tempY-1].getVisit()==0)//可以往左且没被访问过
			{
				this.points[tempX][tempY-1].setVisit(1);
				Queue.add(this.points[tempX][tempY-1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no-1] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX,tempY-1,this.road.getflowh(tempX,tempY-1),3));
					wholeFlow[tempX][tempY-1] = this.road.getflowh(tempX,tempY-1);
				}else{
					wholeFlow[tempX][tempY-1] = this.road.getflowh(tempX,tempY-1)+wholeFlow[tempX][tempY];
					Steps.add(new Step(tempX,tempY,tempX,tempY-1,wholeFlow[tempX][tempY-1],3));
				}
			}
			else if(current.getLeft()==true && this.points[tempX][tempY-1].getVisit()==1)//可以往左且被访问过
			{
				if(distance[no-1]==distance[no]+1)
				{
					if(wholeFlow[tempX][tempY-1]>wholeFlow[tempX][tempY]+this.road.getflowh(tempX,tempY-1))
					{
						wholeFlow[tempX][tempY-1] = wholeFlow[tempX][tempY]+this.road.getflowh(tempX,tempY-1);
						Steps.add(new Step(tempX,tempY,tempX,tempY-1,wholeFlow[tempX][tempY-1],3));					
					}
				}				
			}
			
			if(current.getRight()==true && this.points[tempX][tempY+1].getVisit()==0)//可以往右且上面没被访问过
			{
				this.points[tempX][tempY+1].setVisit(1);
				Queue.add(this.points[tempX][tempY+1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no+1] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX,tempY+1,this.road.getflowh(tempX,tempY),4));
					wholeFlow[tempX][tempY+1] = this.road.getflowh(tempX,tempY);
				}else{
					wholeFlow[tempX][tempY+1] = this.road.getflowh(tempX,tempY)+wholeFlow[tempX][tempY];
					Steps.add(new Step(tempX,tempY,tempX,tempY+1,wholeFlow[tempX][tempY+1],4));
				}
			}
			else if(current.getRight()==true && this.points[tempX][tempY+1].getVisit()==1)//可以往右且被访问过
			{
				if(distance[no+1]==distance[no]+1)
				{
					if(wholeFlow[tempX][tempY+1]>wholeFlow[tempX][tempY]+this.road.getflowh(tempX,tempY))
					{
						wholeFlow[tempX][tempY+1] = wholeFlow[tempX][tempY]+this.road.getflowh(tempX,tempY);
						Steps.add(new Step(tempX,tempY,tempX,tempY+1,wholeFlow[tempX][tempY+1],4));
						
					}
				}			
			}
		}
		if(Steps.size()==0)	
			return null;
		int i = Steps.size()-1;
		Step temp = Steps.get(i);
		
		int targetX = x;
		int targetY = y;
		
		ArrayList<Step> finalstep = new ArrayList<Step>();
		while(i>=0)
		{
			temp = Steps.get(i);
			if(temp.getToX()==targetX&&temp.getToY()==targetY)
			{
				finalstep.add(0, temp);
				if(temp.getFromX()==taxiX&&temp.getFromY()==taxiY)
				{
					break;
				}
				targetX = temp.getFromX();
				targetY = temp.getFromY();
				i--;
				
				continue;
			}
			i--;				
		}
		return finalstep;
		
	}
}
