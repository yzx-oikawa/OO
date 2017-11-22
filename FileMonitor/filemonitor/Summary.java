package filemonitor;

import java.io.File;   
import java.io.FileWriter;
import java.io.IOException; 

public class Summary extends Thread{
	private int renamed = 0;
	private int pathchanged = 0;
	private int sizechanged = 0;
	private int modified = 0;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 
	public synchronized int getRenamed() {
		return renamed;
	}
	public synchronized void setRenamed(int renamed) {
		this.renamed += renamed;
	}
	public synchronized int getPathchanged() {
		return pathchanged;
	}
	public synchronized void setPathchanged(int pathchanged) {
		this.pathchanged += pathchanged;
	}
	public synchronized int getSizechanged() {
		return sizechanged;
	}
	public synchronized void setSizechanged(int sizechanged) {
		this.sizechanged += sizechanged;
	}
	public synchronized int getModified() {
		return modified;
	}
	public synchronized void setModified(int modified) {
		this.modified += modified;
	}
	public synchronized void print(){
		String s="Renamed:"+this.renamed+" Modified:"+this.modified+
		   " PathChanged:"+this.pathchanged+" SizeChanged:"+this.sizechanged;
		System.out.println(s);
		File file = new File("D://Summary.txt");
	    FileWriter fw;
		try {
			fw = new FileWriter(file,true);
			fw.write(s+LINE_SEPARATOR);
		    fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run()
	{
		
		try{
			while(true){
				this.print();
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e){}
			}
		}catch(Exception e){}
	}
}
