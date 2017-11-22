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
	private short[][] distance = new short[length*length][length*length];
	private short[][] direction = new short[length*length][length*length];
	public void setDistance(int i, int j, short d){this.distance[i][j] = d;}
	public short getDistance(int i, int j){return this.distance[i][j];}
	public short[][] getDistance(){return this.distance;}
	public void setDirection(int i, int j, short d){this.direction[i][j] = d;}
	public short getDirection(int i, int j){return this.direction[i][j];}
	public Points getPoint(int i, int j){return this.points[i][j];}
	public int[][] getValue(){return this.value;}
	Map(){}
 	public static Map ParseMap(File f)
	{
 		Map map = new Map();
 		int no = 0;
		for(int i = 0;i<length;i++)
			for(int j = 0;j<length;j++)
				map.points[i][j] = new Points(no++,j,i,0);	
		for(int i = 0;i<length*length;i++)
			for(int j = 0;j<length*length;j++)
			{
				map.distance[i][j] = 0;
				map.direction[i][j] = 0;
			}
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
	
	public void BFS(int x, int y)
	{
		ArrayList<Points> Queue = new ArrayList<Points>();
//		ArrayList<Points> Waiting = new ArrayList<Points>();
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				this.points[i][j].setVisit(0);
//				Waiting.add(this.points[i][j]);
			}
		}
//		System.out.println(Waiting.size());
		Points current = new Points();
		Queue.add(this.points[y][x]);
//		Waiting.remove(this.points[y][x]);
		int no, tempX, tempY;
		while(Queue.size()!=0)
		{
			current = Queue.get(0);//取队中第一项
			current.setVisit(1);
			no = current.getNo();
//			System.out.println(no+" "+current.getUp()+" "+current.getDown()+" "+current.getLeft()+" "+current.getRight());
			tempX = current.getx();
			tempY = current.gety();
			if(current.getUp()==true && this.points[tempY-1][tempX].getVisit()==0){
				this.points[tempY-1][tempX].setVisit(1);
				Queue.add(this.points[tempY-1][tempX]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY-1][tempX]);
				this.setDistance(y*length+x, no-length, (short)(this.getDistance(y*length+x, no)+1));
				if(current == this.points[y][x])//第一步
					this.setDirection(y*length+x, no-length, (short)1); //UP
				else
					this.setDirection(y*length+x, no-length, this.getDirection(y*length+x, no));
			}
			if(current.getDown()==true && this.points[tempY+1][tempX].getVisit()==0){
				this.points[tempY+1][tempX].setVisit(1);
				Queue.add(this.points[tempY+1][tempX]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY+1][tempX]);
				this.setDistance(y*length+x, no+length, (short)(this.getDistance(y*length+x, no)+1));
				if(current == this.points[y][x])//第一步
					this.setDirection(y*length+x, no+length, (short)2); //DOWN
				else
					this.setDirection(y*length+x, no+length, this.getDirection(y*length+x, no));
			}
			if(current.getLeft()==true && this.points[tempY][tempX-1].getVisit()==0){
				this.points[tempY][tempX-1].setVisit(1);
				Queue.add(this.points[tempY][tempX-1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY][tempX-1]);
				this.setDistance(y*length+x, no-1, (short)(this.getDistance(y*length+x, no)+1));
				if(current == this.points[y][x])//第一步
					this.setDirection(y*length+x, no-1, (short)3); //LEFT
				else
					this.setDirection(y*length+x, no-1, this.getDirection(y*length+x, no));
			}
			if(current.getRight()==true && this.points[tempY][tempX+1].getVisit()==0){
				this.points[tempY][tempX+1].setVisit(1);
				Queue.add(this.points[tempY][tempX+1]);//可以到达的点放到队里
//				Waiting.remove(this.points[tempY][tempX+1]);
				this.setDistance(y*length+x, no+1, (short)(this.getDistance(y*length+x, no)+1));
				if(current == this.points[y][x])//第一步
					this.setDirection(y*length+x, no+1, (short)4); //RIGHT
				else
					this.setDirection(y*length+x, no+1, this.getDirection(y*length+x, no));
			}
			Queue.remove(0);

		}
//		判断是否为连通图！
//		if(Waiting.size()==0)
//			System.out.println("The map is connected!");
//		else
//			System.out.println("The map is disconnected!");
	}

}
