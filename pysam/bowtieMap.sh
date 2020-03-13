./bowtie/bowtie2-build data/ref.fa data/ref   
./bowtie/bowtie2 -x data/ref -U data/reads1.fq -S data/result1.sam
tail -n+4 data/result1.sam > data/result2.sam
sort -n -k 4 -t $'\t' data/result2.sam > data/result3.sam
cat data/result3.sam | grep -v '^r[0-9]*[[:blank:]][0-9][[:blank:]]\*[[:blank:]][0]' > data/result4.sam

