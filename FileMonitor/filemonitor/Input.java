package filemonitor;

import java.util.ArrayList;
import java.util.Scanner;

public class Input {
	//IF /root modified THEN record-summary
	private static ArrayList<Command> CommandList;
	public static void main(String args[])
	{
		try{
			Scanner s = new Scanner(System.in);
			String input = " ";
			input = s.nextLine();
			Command temp = new Command();
			CommandList = new ArrayList<Command>();
			Summary sum = new Summary();
			Detail det = new Detail();
			Monitor[] m = new Monitor[8]; 
			Test test = new Test();
			int num_of_thread = 0;
			while (!input.equals("end"))
			{
				temp = Command.ParseCommand(input);
				if(temp!=null)
				{
					CommandList.add(temp);
				}
				input = s.nextLine();
			}
			for(int i =0;i<CommandList.size();i++)
			{
				int flag = 0;
				//	System.out.println(CommandList.get(i).getPath()+" "+CommandList.get(i).getTrigger()+" "+CommandList.get(i).getTask());
				if(num_of_thread<8)
				{
					for(int j=0;j<num_of_thread;j++)
					{
						if(CommandList.get(i).getPath().equals(m[j].getPath())){
							flag = 1;
							m[j].ChangeMonitor(CommandList.get(i).getTrigger(), CommandList.get(i).getTask());
							break;
						}
					}
					if(flag==0){
						m[num_of_thread] = new Monitor(CommandList.get(i).getPath(),sum,det);
						m[num_of_thread].ChangeMonitor(CommandList.get(i).getTrigger(), CommandList.get(i).getTask());
						num_of_thread++;
					}
				}
				else{
					System.out.println("The number of files to monitor is beyond range.");
					break;
				}
			}
			for(int k=0;k<num_of_thread;k++)
			{
				m[k].start();
			}
			sum.start();
			det.start();
			test.start();
		}catch(Exception e){}
	}
}
