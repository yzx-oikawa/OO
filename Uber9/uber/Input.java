package uber;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Input extends Thread {
	private Orderlist orderlist;
	private TaxiGUI gui;
	private Taxi[] taxis;
	private Map map;
	Input (Orderlist OL, Taxi[] taxis, TaxiGUI gui, Map map)
	{
		/*@Requires:订单序列、出租车序列、GUI、地图
		  @Modifies:无
		  @Effects:根据传出参数构造一个Input对象
		*/
		this.orderlist = OL;
		this.taxis = taxis;
		this.gui = gui;
		this.map = map;
	}
	public void run()
	{
		/*@Requires:system.in
		  @Modifies:system.in, system.out
		  @Effects:等待控制台输入字符串，
		  @Effects:如果是订单，就解析生成订单；
		  @Effects:如果是开关路指令，则执行指令；
		  @Effects:如果是查询出租车位置、状态信息的字符串，则输出相应信息；
		  @Effects:否则，输出报错信息
		*/
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
			else if(Pattern.matches("^(\\[OPEN\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\])$", input))
			{
				Road.ParseOpenRequest(input, this.gui, this.map);
			}
			else if(Pattern.matches("^(\\[CLOSE\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\,\\(\\+?\\d{1,2}\\,\\+?\\d{1,2}\\)\\])$", input))
			{
				Road.ParseCloseRequest(input, this.gui, this.map);
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
