package uber;

import java.io.BufferedReader;
import java.io.File;   
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 

public class Map {
	private static int length = 80;
	private int value[][] = new int[length][length];
	private Points points[][] = new Points[length][length];
	private Road road;
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
		  @Modifies:无
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
	Map(){
		/*@Requires:无
		  @Modifies:无
		  @Effects:构造一个Map对象
		*/
	}
 	public static Map ParseMap(File f)
	{
 		/*@Requires:map.txt
 		  @Modifies:无
 		  @Effects:返回一个地图对象；如果是无效地图或解析失败，则返回null
 		*/
 		Map map = new Map();
 		int no = 0;
		for(int i = 0;i<length;i++)
			for(int j = 0;j<length;j++)
				map.points[i][j] = new Points(no++,i,j,0);	
//		for(int i = 0;i<length*length;i++)
//			for(int j = 0;j<length*length;j++)
//			{
//				map.distance[i][j] = 0;
//				map.direction[i][j] = 0;
//			}
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
		return map;
	}
	public void setRoad(Road r)
	{
		/*@Requires:Road
		  @Modifies:无
		  @Effects:将地图的道路设为传入参数
		*/
		this.road = r;
	}
	public short[] BFSdis(int commandX,int commandY)
	{
		/*@Requires:指令的xy坐标，坐标需要在地图内部，this.points用来标记是否已经到达
		  @Modifies:无
		  @Effects:返回一个short数组，数组的内容是地图上所有点到commandX，commandY的距离
		*/
		ArrayList<Points> Queue = new ArrayList<Points>();
		ArrayList<Step> Steps = new ArrayList<Step>();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.points[i][j].setVisit(0);
			}
		}
		Points current = new Points();
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

	public ArrayList<Step> BFS(int taxiX,int taxiY,int x, int y)//start from taxi , heading to x
	{
		/*@Requires:汽车当前坐标和目的地坐标，坐标需要在地图内部，this.points用来标记是否已经到达
		  @Modifies:无
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
		Points current = new Points();
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
			if(current.getUp()==true && this.points[tempX-1][tempY].getVisit()==0)//可以往上且上面没被访问过
			{
				this.points[tempX-1][tempY].setVisit(1);
				Queue.add(this.points[tempX-1][tempY]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no-length] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX-1,tempY,this.road.getflowv(tempX-1,tempY),1));
					wholeFlow[tempX-1][tempY] = this.road.getflowv(tempX-1,tempY);
				}
				else
				{
					
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
			
			if(current.getDown()==true && this.points[tempX+1][tempY].getVisit()==0)//可以往下且面没被访问过
			{
				this.points[tempX+1][tempY].setVisit(1);
				Queue.add(this.points[tempX+1][tempY]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no+length] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX+1,tempY,this.road.getflowv(tempX,tempY),2));
					wholeFlow[tempX+1][tempY] = this.road.getflowv(tempX,tempY);
				}
				else
				{
					
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
			
			if(current.getLeft()==true && this.points[tempX][tempY-1].getVisit()==0)//可以往左且上面没被访问过
			{
				this.points[tempX][tempY-1].setVisit(1);
				Queue.add(this.points[tempX][tempY-1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no-1] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX,tempY-1,this.road.getflowh(tempX,tempY-1),3));
					wholeFlow[tempX][tempY-1] = this.road.getflowh(tempX,tempY-1);
				}
				else
				{
					
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
			
			if(current.getRight()==true && this.points[tempX][tempY+1].getVisit()==0)//可以往you且上面没被访问过
			{
				this.points[tempX][tempY+1].setVisit(1);
				Queue.add(this.points[tempX][tempY+1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				distance[no+1] = distance[no]+1;
				if(tempX==taxiX&&tempY==taxiY)
				{
					Steps.add(new Step(tempX,tempY,tempX,tempY+1,this.road.getflowh(tempX,tempY),4));
					wholeFlow[tempX][tempY+1] = this.road.getflowh(tempX,tempY);
				}
				else
				{
					
					wholeFlow[tempX][tempY+1] = this.road.getflowh(tempX,tempY)+wholeFlow[tempX][tempY];
					Steps.add(new Step(tempX,tempY,tempX,tempY+1,wholeFlow[tempX][tempY+1],4));
				}
			}
			else if(current.getRight()==true && this.points[tempX][tempY+1].getVisit()==1)//可以you且被访问过
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
//		for(Step st : Steps)
//			System.out.println("from:"+st.getFromX()+","+st.getFromY()+" to:"+st.getToX()+","+st.getToY()+" flow"+st.getFlow());
//		System.out.println("6789");
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
//		for(Step st : finalstep)
//			System.out.println("from:"+st.getFromX()+","+st.getFromY()+" to:"+st.getToX()+","+st.getToY()+" flow"+st.getFlow());
	
		return finalstep;
		
	}
}
