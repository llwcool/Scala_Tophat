#!/bin/bash
ls
while :
do
echo "-----                       Welcom                      -----"
echo "-----                     1.序列比对                    -----"
echo "-----                       2.排序                      -----"
echo "-----                    3.数据可视化(samtools)         -----"
echo "-----                    4.查找(范围)                   -----"
echo "-----                    5.查找(序号)                   -----"
echo "-----                    6.结果匹配                     -----"
echo "-----                    7.数据可视化(pysam)           -----"
echo "-----                      8.初始化                     -----"
echo "-----                       0.退出                      -----"
read number
case $number in
0)
clear
echo "你已安全退出"
exit 0 
;;
1)
clear
sudo ./bowtie/bowtie2-build data/ref.fa data/ref   
sudo ./bowtie/bowtie2 -x data/ref -U data/reads1.fq -S data/result.sam
echo "比对完成"
echo "比对结果在data下的result.sam"
;;
2)
clear
tail -n+4 data/result.sam > data/result2.sam
sort -n -k 4 -t $'\t' data/result2.sam > data/result3.sam
cat data/result3.sam | grep -v '^r[0-9]*[[:blank:]][0-9][[:blank:]]\*[[:blank:]][0]' > data/result_sort.sam
rm -r data/result2.sam
rm -r data/result3.sam
echo "排序完成"
echo "排序结果在data下的result_sort.sam"
;;
3)
clear
samtools view -bS data/result.sam > data/result.bam
samtools sort data/result.bam data/result.sort
samtools index data/result.sort.bam data/result.sort.bam.bai
samtools tview data/result.sort.bam data/ref.fa
;;
4)
clear
echo "请输入左边界"
read l1
echo "请输入右边界"
read r1
if [ -f data/sam.find ];then
rm -r data/sam.find
fi
<<!
	for((i=l1;i<r1;++i))
	do
	{
	cat data/result_sort.sam | grep '^r[0-9]*[[:blank:]][0-9]*[[:blank:]][a-z]*|[0-9]*|[a-z]*|[A-Z]*\_[0-9]*\.[0-9]*|[[:blank:]]'$i'[[:blank:]]' >> data/sam.find
}
done
!
if (( $l1 < $r1 ))
then
	awk '$4>='"$l1"'&&$4<='"$r1"'' data/result_sort.sam > data/sam.find
	echo "查找完成，在data下的sam.find"
	echo "是否查看？--1、查看  --2、不查看"
	read num
	case $num in
	1)
	vi data/sam.find
	;;
	2)
	esac
else 
	echo "请确认右边界大于左边界！！！"
fi
;;
5)
clear
echo "请输入要查找的序号"
read index
awk '$1 == "'$index'"' data/result_sort.sam
;;
6)
clear
sudo python read.py
echo "匹配结果已生成，可以开始可视化操作"
;;
7)
clear
sudo python show.py
;;
8)
clear
sudo chmod 777 bowtie/bowtie2*
if [ ! -f /usr/bin/samtools ];then
sudo apt-get install samtools
fi
sudo apt-get install python-pip
sudo apt-get install python-dev
sudo pip install evdev
echo "初始化成功"
;;
esac
done
