package uber;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Input extends Thread {
	/* Overview:线程类; 负责从控制台读入字符串并判断、解析成指令，指令包括订单、开关路指令和查询信息的指令
	 * 抽象函数：AF(c)=(orderlist, gui, taxis, map) 
	          where orderlist==c.orderlist, gui==c.gui, taxis==c.taxis, map==c.map
	 * 不变式：c.orderlist!=null && c.gui!=null && c.taxis!=null && c.map!=null && c.taxis.length<=100
     * */
	private Orderlist orderlist;
	private TaxiGUI gui;
	private Taxi[] taxis;
	private Map map;
	public boolean repOK()
	{
		/*@Requires:无
		  @Modifies:无
		  @Effects:如果为无效对象，返回false；否则返回true
		*/
		if(orderlist==null || gui==null || taxis==null || map==null || taxis.length>100)
			return false;
		return true;
	}
	Input (Orderlist OL, Taxi[] taxis, TaxiGUI gui, Map map)
	{
		/*@Requires:订单序列、出租车序列、GUI、地图
		  @Modifies:this
		  @Effects:根据传出参数构造一个Input对象
		*/
		this.orderlist = OL;
		this.taxis = taxis;
		this.gui = gui;
		this.map = map;
	}
	public void run()
	{
		/*@Requires:System.in
		  @Modifies:System.in, System.out
		  @Effects:等待控制台输入字符串，
		  @Effects:如果是订单，就解析生成订单；
		  @Effects:如果是开关路指令，则执行指令；
		  @Effects:如果是查询出租车位置、状态信息、乘客服务情况的字符串，则输出相应信息；
		  @Effects:否则，输出报错信息
		*/
		try{
		Uber.setTime(System.currentTimeMillis());
		Scanner s = new Scanner(System.in);
		String input = " ";
		input = s.nextLine();
		long time = Uber.getTime();
		int line = 1;
		while (line<=1001)
		{
			
			if(line==1001)
			{
				System.out.println("There are more than 1000 lines of order.");
			}
			else if(Pattern.matches("Info\\d{1,2}", input)==true)
			{
				String inputs = input.replace("Info", "");
				int no = Integer.parseInt(inputs);
				if(this.taxis[no].getType()==0)
					System.out.println("Taxi No."+no+" is a normal taxi!");
				else{
					NewTaxi t = (NewTaxi)this.taxis[no];
				//	System.out.println("123");
					t.setIterator();
					while(t.getIterator().hasNext())
					{
				//		System.out.println(t.getServeInfo().size());
						String output = t.getIterator().next();
						System.out.print(output);
					}
				}
			}
			else if(Pattern.matches("Reverse\\d{1,2}", input)==true)
			{
				String inputs = input.replace("Reverse", "");
				int no = Integer.parseInt(inputs);
				if(this.taxis[no].getType()==0)
					System.out.println("Taxi No."+no+" is not a traceable taxi!");
				else{
					NewTaxi t = (NewTaxi)this.taxis[no];
				//	System.out.println("123");
					t.setIterator();
					while(t.getIterator().hasNext()){
						String output = t.getIterator().next();
					}
					while(t.getIterator().hasPrevious())
					{
				//		System.out.println(t.getServeInfo().size());
						String output = t.getIterator().previous();
						System.out.print(output);
					}
				}	
			}
			else if(Pattern.matches("\\d{1,2}", input)==true)
			{
				int no = Integer.parseInt(input);
				if(no>99)
					System.out.println("Invalid input: "+no);
				else{
					Taxi t = this.taxis[no];
					System.out.println(Uber.getTime()+": No."+t.getNo()+" Type:"+t.getType()+" at ("+t.getx()+","+t.gety()
					                   +") status:"+t.getstate()+" credit:"+t.getCredit());
				}
				
			}
			else if(Pattern.matches("Status\\d", input))
			{
				String inputs = input.replace("Status", "");
				int status = Integer.parseInt(inputs);
				if(status>3)
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
					Order temp = Order.ParseOrder(input,time, this.gui);
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
		}catch(Exception e){e.printStackTrace();}
	}
}
