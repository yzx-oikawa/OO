IF [D:\my_test\a1.txt] renamed THEN record_detail

IF [D:\my_test\a1.txt] renamed THEN record_detail
IF [D:\my_test\a1.txt] renamed THEN record_detail

IF [D:\my_test\a1.txt] modified THEN record_detail
删除和拷贝回来都不能触发，Detail.txt没有输出
IF [D:\my_test\my_test] modified THEN record_detail

IF [D:\my_test] path_changed THEN record_detail

IF [D:\my_test\my_test] size_changed THEN record_detail

IF [D:\my_test\my_test\a2.txt] size_changed THEN record_detail

IF [D:\my_test] size_changed THEN record_detail

IF [D:\my_test] size_changed THEN record_detail

IF [D:\my_test\a1.txt] renamed THEN recover

IF [D:\my_test\my_test\a2.txt] path_changed THEN recover

IF [D:\my_test\a1.txt] renamed THEN record_detail
IF [D:\my_test\a1.txt] renamed THEN recover

IF [D:\my_test\a1.txt] modified THEN record_detail
IF [D:\my_test\a1.txt] size_changed THEN record_detail

IF [D:\my_test\a1.txt] modified THEN record_detail
IF [D:\my_test\a1.txt] size_changed THEN record_detail
IF [D:\my_test] size_changed THEN record_detail

IF [D:\my_test\my_test\a2.txt] modified THEN record_detail
IF [D:\my_test\my_test] size_changed THEN record_detail
IF [D:\my_test] size_changed THEN record_detail

IF [D:\my_test\mytest\a2.txt] path_changed THEN record_detail
IF [D:\my_test\my_test] size_changed THEN record_detail
IF [D:\my_test\my_test\my_test] size_changed THEN record_detail



IF [D:\my_test\a1.txt] modified THEN record_detail
IF [D:\my_test\a2.txt] modified THEN record_detail
IF [D:\my_test\a3.txt] modified THEN record_detail
IF [D:\my_test\a4.txt] modified THEN record_detail
IF [D:\my_test\a5.txt] modified THEN record_detail
IF [D:\my_test\a6.txt] modified THEN record_detail
IF [D:\my_test\a7.txt] modified THEN record_detail
IF [D:\my_test\a8.txt] modified THEN record_detail
IF [D:\my_test\a9.txt] modified THEN record_detail
创建相应的监视线程。要求支持监控的目录/文件不多于8个

IF [D:\my_test\a1.txt] size_changed THEN record_summary
IF [D:\my_test\a1.txt] size_changed THEN record_detail
IF [D:\my_test\a2.txt] size_changed THEN record_summary
IF [D:\my_test\a3.txt] size_changed THEN record_summary

只检测a1但是把所有的Detail信息都输出到文件了

summary.txt 或者没有输出，或者打开的时候显示“存储空间不足，无法执行此命令”，因此并不能判断你的summary功能是否实现

IF [D:\my_test\a1.txt] Path_changed THEN record_summary
IF [D:\my_test\a1.txt] Path_changed THEN record_detail
IF [D:\my_test\a1.txt] Path_changed THEN recover
recover的时候会改变文件名

IF [D:\my_test] size_changed THEN record_detail