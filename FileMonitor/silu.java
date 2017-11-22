文件名、父文件名、路径、最后修改时间、大小
Renamed 
trigger = 1, file a, file b
	路径相同，最后修改时间相同，文件大小相同
	文件名有新增&&有缺失
	{
		新增list.size==0 || 缺失list.size==0
			不触发
		if 新增list.size <= 缺失list.size
			for(i=0;i<新增list.size;i++)
				触发(新增list[i],缺失list[i])
		else
			for(i=0;i<缺失list.size;i++)
				触发(新增list[i],缺失list[i])
	}
	
Modified 
trigger = 2, file f
	路径相同、文件名相同
	最后修改时间不同
	触发该文件即可
	
Path-changed
trigger = 3, file pre, file new_
	文件名相同，大小相同，最后修改时间相同
	原文件消失&&路径不同
	
Size-changed
trigger = 4, file f
	路径相同
	如果是目录：目录内文件的新增、删除、修改内容产生大小变化
	如果是文件：该文件的新增、删除、修改内容产生大小变化
	if(上一次文件.size>0 && (当前文件.size==0||当前文件不存在))
		将文件加入缺失list
	else if((上一次文件.size==0||上一次文件不存在)&&当前文件.size>0)
		将文件加入新增list
	
silu:
trigger类包含四个函数分别对应四个触发器
sizechange
{
	如果监视文件
		文件被删除、重命名、移动路径都相当于size变为0（即在当前目录下找不到这个文件
		如果能找到，则只需要对比文件的大小，并记录细节
	如果监视文件夹
		同modified 如果这个路径能够找到这个文件，则对比，否则相当于size变为0，文件目录任然同第一次相比	
}
modified
{
	如果监视文件
		更简单，只需要修改filetreenode的细节部分即可
	如果监视文件夹
		树的结构都是跟第一次扫描的比较，树的细节是要根据之后的扫描的结果修改的
}
pathchange
{
	仅对文件
		如果原路径找到了，则没有变化，如果原路径找不到，在父目录下递归查找，并修改细节
}
rename
{
	仅对文件
		如果原路径找到了，则没有变化，如果原路径找不到，在父目录下非递归查找
}
分别有三种操作
recover
summary
detail

			recover	summary	detail fileonly
sizechange		0		
modified		0
pathchange								1
rename									1
所以有四个对应四种trigger的detail的字符串


Search(Filetreenode ftn, file f)返回一个路径
{
	如果ftn是文件
		判断文件的三个属性
	如果ftn是目录
		遍历子节点
			递归
}
两棵树
{
	如果是文件 return 0
	如果now文件存在于该目录下, return 0
	如果不存在，在父目录下非递归查找 
	最后修改时间相同、大小相同的文件
}
Summary是累计
Detail 是实时
IF D://test Renamed THEN recover
IF D://test//a//123.txt Size-changed THEN record-summary
IF D://test//a//123.txt Size-changed THEN record-detail
IF D://test//a Size-changed THEN record-detail
IF D://test//a//123.txt Modified THEN record-summary
IF D://test//a//123.txt Modified THEN record-detail
IF D://test Modified THEN record-detail
IF D://test//a//123.txt Path-changed THEN record-summary
IF D://test//a//123.txt Path-changed THEN record-detail
IF D://test//a//123.txt Path-changed THEN recover
IF D://test//a//123.txt Renamed THEN record-detail
IF D://test//a//123.txt Renamed THEN recover

IF D://test//a//123.txt Renamed THEN record-summary
IF D://test//a//4.txt Renamed THEN record-summary

IF D://test//a//aa Size-changed THEN record-detail
end


(ER,#1,5)
(ER,#1,2)
(ER,#1,1)