package filemonitor;

import java.io.File;   
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Detail extends Thread{
	private ArrayList<String> renamed;
	private ArrayList<String> modified;
	private ArrayList<String> pathchanged;
	private ArrayList<String> sizechanged;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); 
	Detail(){
		this.renamed = new ArrayList<String>();
		this.modified = new ArrayList<String>();
		this.pathchanged = new ArrayList<String>();
		this.sizechanged = new ArrayList<String>();
	}
	public synchronized ArrayList<String> getRenamed() {
		return renamed;
	}
	public synchronized void setRenamed(String renamed) {
		this.renamed.add(renamed);
	}
	public synchronized ArrayList<String> getModified() {
		return modified;
	}
	public synchronized void setModified(String modified) {
		this.modified.add(modified);
	}
	public synchronized ArrayList<String> getPathchanged() {
		return pathchanged;
	}
	public synchronized void setPathchanged(String pathchanged) {
		this.pathchanged.add(pathchanged);
	}
	public synchronized ArrayList<String> getSizechanged() {
		return sizechanged;
	}
	public synchronized void setSizechanged(String sizechanged) {
		this.sizechanged.add(sizechanged);
	}
	public synchronized void print(){
		try{
			File file = new File("D://Detail.txt");
		    FileWriter fw;
		    fw = new FileWriter(file,true);
		    fw.write("Renamed: "+LINE_SEPARATOR);
			System.out.println("Renamed: ");
			for(int i=0;i<this.renamed.size();i++){
				System.out.println(this.renamed.get(i));
				fw.write(this.renamed.get(i)+LINE_SEPARATOR);}
			fw.write("Modified: "+LINE_SEPARATOR);
			System.out.println("Modified: ");
			for(int i=0;i<this.modified.size();i++){
				//System.out.println(this.modified.get(i));
				fw.write(this.modified.get(i)+LINE_SEPARATOR);}
			fw.write("Pathchanged: "+LINE_SEPARATOR);
			System.out.println("Pathchanged: ");
			for(int i=0;i<this.pathchanged.size();i++){
				//System.out.println(this.pathchanged.get(i));
				fw.write(this.pathchanged.get(i)+LINE_SEPARATOR);}
			fw.write("Sizechanged: "+LINE_SEPARATOR);
			System.out.println("Sizechanged: ");
			for(int i=0;i<this.sizechanged.size();i++){
				System.out.println(this.sizechanged.get(i));
				fw.write(this.sizechanged.get(i)+LINE_SEPARATOR);}
			fw.close();
			this.modified = new ArrayList<String>();
			this.pathchanged = new ArrayList<String>();
			this.renamed = new ArrayList<String>();
			this.sizechanged = new ArrayList<String>();
		}catch (IOException e) {
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
