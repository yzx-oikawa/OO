package filemonitor;

public class Command {
	private String path;
	private int trigger;//1 rename 2 modified 3 path 4 size
	private int task; //1 summary 2 detail 3 recover
	public String getPath(){return this.path;}
	public int getTrigger(){return this.trigger;}
	public int getTask(){return this.task;}
	
	Command(){}
	Command(String s, int trigger, int task)
	{
		this.path = s;
		this.trigger = trigger;
		this.task = task;
	}
	public static Command ParseCommand(String input){
		//IF /root modified THEN record-summary
		String[] inputs = input.split(" ");
		if(inputs.length!=5 || input.equals("") || !inputs[0].equals("IF") || !inputs[3].equals("THEN"))
		{
			System.out.println("Invalid Input!");
			return null;
		}
		else 
		{
			String path = inputs[1].replace(" ", "");
			int tri = 0;
			int ta = 0;
			if(inputs[2].equals("Renamed")) tri = 1;
			else if(inputs[2].equals("Modified")) tri = 2;
			else if(inputs[2].equals("Path-changed")) tri = 3;
			else if(inputs[2].equals("Size-changed")) tri = 4;
			else{
				System.out.println("Invalid Input!(Invalid Trigger)");
				return null;
			}
			if(inputs[4].equals("record-summary")) ta = 1;
			else if(inputs[4].equals("record-detail")) ta = 2;
			else if(inputs[4].equals("recover")) ta = 3;
			else{
				System.out.println("Invalid Input!(Invalid Task)");
				return null;
			}
			if((tri==2||tri==4)&&ta==3)
			{
				System.out.println("Invalid Input!(Task does not serve Trigger)");
				return null;
			}
			else
			{
				Command c = new Command(path, tri ,ta);
				return c;
			}
		}
	}
}
