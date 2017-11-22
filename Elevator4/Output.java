package elevator;

import java.io.File;   
import java.io.FileWriter;   


public class Output {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 
	public static void Out(String s){   
		try{
        File file = new File("C://Users//Administrator//Desktop//result.txt");
        FileWriter fw = new FileWriter(file,true);
        fw.write(s+LINE_SEPARATOR);
        fw.close();
		}catch(Exception e){}
    }  
}
