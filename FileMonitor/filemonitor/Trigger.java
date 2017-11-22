package filemonitor;

import java.util.*;
import java.io.*;

public class Trigger {
	private String Modified_Detail;
	private String SizeChanged_Detail;
	private String PathChanged_Detail;
	private String Renamed_Detail;
	private String Rec_path;
	private String Rec_name;
	public String getModifiedDetail(){return this.Modified_Detail;}
	public String getSizeChangedDetail(){return this.SizeChanged_Detail;}
	public String getPathChangedDetail(){return this.PathChanged_Detail;}
	public String getRenamedDetail(){return this.Renamed_Detail;}
	public void setRecPath(String rec){this.Rec_path = rec;}
	public void setRecName(String rec){this.Rec_name = rec;}
	public String getRecPath(){return this.Rec_path;}
	public String getRecName(){return this.Rec_name;}
	Trigger()
	{
		Modified_Detail = "";
		SizeChanged_Detail = "";
		PathChanged_Detail = "";
		Renamed_Detail = "";
	}
	public int Modified(FileTreeNode a)//a:the first snapshot
	{
		int ans = 0;
		if(a.isDir())//directory
		{
			ArrayList<FileTreeNode> TreeNodeChildren = a.getCN();
			for(FileTreeNode ChildFileNode : TreeNodeChildren)
			{
				ans+=Modified(ChildFileNode);
			}
		}
		else//file
		{
			String filename = a.getPath();
			File now = new File(filename);
			if(now.exists()&&now.isFile()&&(a.getTime()!=now.lastModified()))
			{
				this.Modified_Detail += a.getPath()+": from "+a.getTime()+" to "+now.lastModified()+"\n";
				a.setTime(now.lastModified());
				return 1;
			}
			else
			{
				return 0;
			}
		}
		return ans;
	}
	
	public int SizeChanged(FileTreeNode a)
	{
		int ans = 0;
		if(a.isDir())
		{
			ArrayList<FileTreeNode> TreeNodeChildren = a.getCN();
			for(FileTreeNode ChildFileNode : TreeNodeChildren)
			{
				ans+=SizeChanged(ChildFileNode);
			}
		}
		else //file
		{
			String filename = a.getPath();
		//	System.out.println(filename);
			File now = new File(filename);
			if(!now.exists()&&a.getSize()!=0)//the file is deleted
			{
			//	System.out.println("1");
				this.SizeChanged_Detail += a.getPath()+": from "+a.getSize()+" to 0\n";
				a.setSize(0);
				return 1;
			}
			else if(now.isFile()&&(a.getSize()!=now.length()))
			{
			//	System.out.println("2");
				this.SizeChanged_Detail += a.getPath()+": from "+a.getSize()+" to "+now.length()+"\n";
			//	System.out.println(this.SizeChanged_Detail);
				
				a.setSize(now.length());
				return 1;
			}
		}
		return ans;
	}

	public int PathChanged(FileTreeNode a)//file only
	{
		int ans = 0;
		if(a.isDir())
		{
			return 0;
		}
		else
		{
			String filename = a.getPath();
			File now = new File(filename);
			if(now.exists()) return 0;
			else// search under parent path (with recursion)
			{
				File nowparent = new File(now.getParent());
				FileTreeNode ftn = new FileTreeNode(nowparent);
				FileTreeNode.Snapshot(nowparent,ftn);
				if(SearchwithRec(ftn, now, a.getSize())!=null)
				{
					String newPath = SearchwithRec(ftn, now, a.getSize());
					this.PathChanged_Detail += "from "+filename+" to "+newPath+"\n";
					setRecPath(filename);
					a.setPath(newPath);
					a.setFile(new File(newPath));
					ans += 1;
				}
			}
		}
		return ans;
	}
	
	public int Renamed(FileTreeNode a)
	{
		int ans = 0;
		if(a.isDir())
		{
			return 0;
		}
		else
		{
			String filename = a.getPath();
			File now = new File(filename);
		//	System.out.println(now.exists());
			if(now.exists()) return 0;
			else// search under parent path (without recursion)
			{
				File nowparent = new File(now.getParent());
				FileTreeNode ftn = new FileTreeNode(nowparent);
				FileTreeNode.Snapshot(nowparent,ftn);
				if(SearchwithoutRec(ftn, now, a.getSize())!=null)
				{
					String newName = SearchwithoutRec(ftn, now, a.getSize());
					this.Renamed_Detail += a.getPath()+": from "+a.getFileName()+" to "+newName+"\n";
					setRecName(filename);
					a.setFileName(newName);
					a.setPath(a.getParent()+"//"+newName);
					a.setFile(new File(a.getParent()+"//"+newName));
					ans += 1;
				}
			}
		}
		return ans;
	}
	
	public String SearchwithRec(FileTreeNode ftn, File f, long length)
	{
		String path = null;
		if(!ftn.isDir())//file
		{	
		//	System.out.println(ftn.getFileName().equals(f.getName())&&ftn.getSize()==f.length());
			if(ftn.getFileName().equals(f.getName())&&ftn.getSize()==length)
			{
		//		System.out.println(ftn.getPath());
				return ftn.getPath();
			}
		}
		else
		{
			ArrayList<FileTreeNode> TreeNodeChildren = ftn.getCN();
			for(FileTreeNode ChildTreeNode:TreeNodeChildren)
			{
				if(SearchwithRec(ChildTreeNode, f, length)!=null){
					path = SearchwithRec(ChildTreeNode, f, length);
					break;
				}
			}
		}
	//	System.out.println(path);
		return path;//the file is removed from workspace
	}
	public String SearchwithoutRec(FileTreeNode ftn, File f, long length)
	{
		String name = null;
		ArrayList<FileTreeNode> TreeNodeChildren = ftn.getCN();
		for(FileTreeNode ChildFileNode : TreeNodeChildren)
		{ 
		//	System.out.println(f.length());
			if(ChildFileNode.getSize()==length)
			{
				name = ChildFileNode.getFileName();
				break;
			}
		}
	//	System.out.println(name);
		return name;//the file is removed from workspace
	}
}
