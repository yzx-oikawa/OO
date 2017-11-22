package uber;

import java.io.File;   
import java.io.FileWriter;
import java.io.IOException; 
import java.util.Random;

public class CreateMap {
	private static int length = 10;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 

	public static void main(String args[])
	{
		Points points[][] = new Points[length][length];
		for(int i = 0;i<length;i++)
			for(int j = 0;j<length;j++)
				points[i][j] = new Points(0);
		File file = new File("C://Users//Administrator//Desktop//Map.txt");
	    FileWriter fw;
	    int ran;
		try {
			fw = new FileWriter(file,false);
			for(int i = 0;i<length;i++){
				for(int j = 0;j<length;j++)
				{
					if(i==length-1 && j==length-1)//points[79][79]
						points[i][j].setValue(0);
					else if(i==length-1 && j==length-2)//points[79][78]
						points[i][j].setValue(1);
					else if(j==length-1)//points[i][79] 最后一列
					{
						if(points[i][j].getLeft()==false && points[i][j].getUp()==false)
							points[i][j].setValue(2);
						else
							points[i][j].setValue(0);
					}
					else if(i==length-1)//points[79][j] 最后一行
					{
						if(points[i][j].getLeft()==false && points[i][j].getUp()==false)
							points[i][j].setValue(1);
						else
							points[i][j].setValue(0);
					}
					else	
					{
						if(points[i][j].getLeft()==false && points[i][j].getUp()==false)
						{
							ran = new Random().nextInt(3);
							points[i][j].setValue(ran+1);
						}
						else
						{
							ran = new Random().nextInt(4);
							points[i][j].setValue(ran);
						}
					}
					points[i][j].Initial(points[i][j].getValue());
					switch(points[i][j].getValue()){
							case 0:
								break;
							case 1:
								points[i][j+1].Initial(-1);
								break;
							case 2:
								points[i+1][j].Initial(-2);
								break;
							case 3:
								points[i][j+1].Initial(-1);
								points[i+1][j].Initial(-2);
								break;
					}
					System.out.print(points[i][j].getValue()+" ");
					fw.write(points[i][j].getValue()+" ");	
				}
				System.out.print("\n");
				fw.write(LINE_SEPARATOR);	
		    }
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
