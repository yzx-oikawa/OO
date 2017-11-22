package uber;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Input extends Thread {
	private Orderlist orderlist;
	private TaxiGUI gui;
	private Taxi[] taxis;
	Input (Orderlist OL, Taxi[] taxis, TaxiGUI gui)
	{
		this.orderlist = OL;
		this.taxis = taxis;
		this.gui = gui;
	}
	public void run()
	{
		Uber.setTime(System.currentTimeMillis());
		Scanner s = new Scanner(System.in);
		String input = " ";
		input = s.nextLine();
		long time = Uber.getTime();
		int line = 1;
		Order temp = new Order();
		while (line<=1001)
		{
			if(line==1001)
			{
				System.out.println("There are more than 1000 lines of order.");
			}
			else if(Pattern.matches("\\d{1,2}", input)==true)
			{
				int no = Integer.parseInt(input);
				if(no>99)
					System.out.println("Invalid input: "+no);
				else{
					Taxi t = this.taxis[no];
					System.out.println(Uber.getTime()+": No."+t.getNo()+" at ("+t.getx()+","+t.gety()
					                   +") status:"+t.getstate()+" credit:"+t.getCredit());
				}
			}
			else if(Pattern.matches("Status\\d", input))
			{
				String inputs = input.replace("Status", "");
				int status = Integer.parseInt(inputs);
				if(status==0 || status>4)
					System.out.println("Invalid input: "+input);
				else{
					for(Taxi t:this.taxis)
					{
						if(t.getstate()==status){
							System.out.print(t.getNo()+" ");
						}
					}
					System.out.println();
				}
			}
			else
			{
				String[] str = input.split("\\;");
				int valid = 1;
				for(int u=0;u<str.length;u++){
					input = str[u];
					temp = Order.ParseOrder(input,time, this.gui);
					if(temp!=null)
					{
						if(valid<=10)
						{
							//add to list in dispatch
							this.orderlist.Add(temp);
							valid++;
							//remove same order
							for(int i = 0;i<orderlist.getsize();i++){
								for(int j = i+1;j<orderlist.getsize();j++){
							//		System.out.println(orderlist.getOrder(i).getTime()+" "+orderlist.getOrder(j).getTime());
									if(orderlist.getOrder(i).getTime()==orderlist.getOrder(j).getTime()
									&& orderlist.getOrder(i).getDstX()==orderlist.getOrder(j).getDstX()
									&& orderlist.getOrder(i).getDstY()==orderlist.getOrder(j).getDstY()
									&& orderlist.getOrder(i).getSrcX()==orderlist.getOrder(j).getSrcX()
									&& orderlist.getOrder(i).getSrcY()==orderlist.getOrder(j).getSrcY())
									{
										System.out.println("Same order: "+input);
										orderlist.RemoveAt(j);
										j--;
									}
								}
							}
						}
						else{
							System.out.println("Invalid order(more than 10 orders in a line): "+input);
						}
					}
				}
			}
			input = s.nextLine();
			time = Uber.getTime();
			line++;
		}
	}
}
