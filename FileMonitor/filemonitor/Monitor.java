package filemonitor;

import java.io.File;
import java.util.ArrayList;

public class Monitor extends Thread{
	//1 rename  2 modified 3 path 4 size
	//1 summary 2 detail   3 recover
	private String path;
	private boolean Renamed;
	private boolean Modified;
	private boolean PathChanged;
	private boolean SizeChanged;
	private boolean Summary;
	private boolean Detail;
	private boolean Recover;
	private Summary sum;
	private Detail det;

	Monitor(String path, Summary sum ,Detail det)
	{
		this.path = path;
		this.Renamed = false;
		this.Modified = false;
		this.PathChanged = false;
		this.SizeChanged = false;
		this.Summary = false;
		this.Detail = false;
		this.Recover = false;
		this.sum = sum;
		this.det =det;
	}
	public String getPath(){return this.path;}
	
	public void ChangeMonitor(int tr, int ta)
	{
		if(tr==1) this.Renamed = true;
		if(tr==2) this.Modified = true;
		if(tr==3) this.PathChanged = true;
		if(tr==4) this.SizeChanged =true;
		if(ta==1) this.Summary = true;
		if(ta==2) this.Detail = true;
		if(ta==3) this.Recover = true;
	}
	public void run()
	{
		try{
			File f = new File(this.path);
			FileTreeNode ftn = new FileTreeNode(f);
			if(f.exists())
			{
				if(f.isFile())
				{
					File Father = new File(f.getParent());
					FileTreeNode ftn1 = new FileTreeNode(Father);
					FileTreeNode.Snapshot(Father,ftn1);
					ftn.setCN(ftn1.getCN());
				}
				else if(f.isDirectory())
				{
					FileTreeNode.Snapshot(f,ftn);
				}
				while(true)
				{
					Trigger t = new Trigger();
					
					if(Renamed)
					{
						String newname = "";
						int i = t.Renamed(ftn);
						if(i!=0)
						{
							if(Summary) sum.setRenamed(i);
							if(Detail) det.setRenamed(t.getRenamedDetail());
							if(Recover) 
							{
								ftn.getFile().renameTo(new File(t.getRecName()));
								ftn.setPath(t.getRecName());
							}
						}
					}
					if(Modified)
					{
						int i = t.Modified(ftn);
						if(i!=0)
						{
							if(Summary) sum.setModified(i);
							if(Detail) det.setModified(t.getModifiedDetail());
						}
					}
					if(PathChanged)
					{
						int i = t.PathChanged(ftn);
						
						if(i!=0 )
						{
				//			System.out.println(this.Recover);
							if(Summary) sum.setPathchanged(i);
							if(Detail) det.setPathchanged(t.getPathChangedDetail());
							if(Recover)
							{
								ftn.getFile().renameTo(new File(t.getRecPath()));
								ftn.setPath(t.getRecPath());
							}
						}
					}
					if(SizeChanged)
					{	
						int i = t.SizeChanged(ftn);
						if(i!=0)
						{
							if(Summary) sum.setSizechanged(i);
							if(Detail) det.setSizechanged(t.getSizeChangedDetail());
						}
					}
					try{
						Thread.sleep(500);
					}catch(InterruptedException e){}
				}
			}
			else{
				System.out.println("File "+this.path+" does not exist!");
			}
		}catch(Exception e){}
		
	}
}
