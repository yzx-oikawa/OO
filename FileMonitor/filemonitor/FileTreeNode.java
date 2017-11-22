package filemonitor;

import java.util.*;
import java.io.*;

public class FileTreeNode {
	private File FileNode;
	private String Path;
	private String ParentPath;
	private String FileName;
	private long Size;
	private long LastModifiedTime;
	private boolean isDir;
	private ArrayList<FileTreeNode> ChildNode;
	
	public boolean isDir() {
		return isDir;
	}
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	public File getFile(){return this.FileNode;}
	public void setFile(File f){this.FileNode = f;}
	public String getPath(){return this.Path;}
	public void setPath(String path){this.Path = path;}
	public String getParent(){return this.ParentPath;}
	public String getFileName(){return this.FileName;}
	public void setFileName(String name){this.FileName = name;}
	public long getSize(){return this.Size;}
	public void setSize(long size){this.Size = size;}
	public long getTime(){return this.LastModifiedTime;}
	public void setTime(long time){this.LastModifiedTime = time;}
	public ArrayList<FileTreeNode> getCN(){return this.ChildNode;}
	public void setCN(ArrayList<FileTreeNode> cn){this.ChildNode = cn;}
	
	FileTreeNode(){
		this.ChildNode = new ArrayList<FileTreeNode>();
	}
	FileTreeNode(File f)
	{
		this.FileNode = f;
		this.Path = f.getAbsolutePath();
		this.FileName = f.getName();
		this.Size = f.length();
		this.LastModifiedTime = f.lastModified();
		this.ChildNode = new ArrayList<FileTreeNode>();
		this.ParentPath = f.getParent();
		if(f.isDirectory()){
			this.isDir = true;
		}
		else{
			this.isDir = false;	
		}
	}
	public static void Snapshot(File f, FileTreeNode main)
	{
	//	System.out.println(f);
	//	FileTreeNode main = new FileTreeNode(f);
		if (f != null)
		{
			if(f.isDirectory()){
				File[] files = f.listFiles();
				if(files != null)
				{
					for(File file: files)
					{		
						main.Size += file.length();
						FileTreeNode cn = new FileTreeNode(file);
						cn.ParentPath = f.getAbsolutePath();
						main.ChildNode.add(cn);
						Snapshot(file, cn);
					}
				}
			}
		}
	}
	public static void print(FileTreeNode ftn)
	{
		System.out.println(ftn.Path+" "+ftn.Size);

		for (int i=0;i<ftn.ChildNode.size();i++)
		{
			//System.out.println(ftn.ChildNode.get(i).Path);
			print(ftn.ChildNode.get(i));
		}
	}
//	public static void main(String[] args)
//	{
//		File f = new File("D://test//a//123.txt");
////		System.out.println(f.renameTo(new File("D://test//a//aa//attt.txt")));
////		System.out.println(f.exists());
//		if(f.exists())
//		{
//			FileTreeNode ftn = new FileTreeNode(f);
//			if(f.isFile())
//			{
//				File Father = new File(f.getParent());
//				FileTreeNode ftn1 = new FileTreeNode(Father);
//				Snapshot(Father,ftn1);
//				ftn.ChildNode = ftn1.ChildNode;
//			}
//			else if(f.isDirectory())
//			{
//				Snapshot(f,ftn);
//			}	
//			print(ftn);
//		}
//		else
//		{
//			System.out.println("File does not exist!");
//		}
//	}

	
}
