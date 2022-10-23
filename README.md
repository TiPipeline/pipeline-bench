## 配置文件
`/bin/test.properties`

## 使用步骤
```
cd bin/
# build binary
./build.sh
# init db data
./initialize.sh 
# do test
## 可以不指定 sql mode, 默认 all
### simple_agg
### simple_where
### simple_join
### all
./test.sh
./test.sh ${sql_mode}
```

## prepare
```
测试前记得 compact 一下
ALTER table ${table} COMPACT TIFLASH REPLICA;

ALTER table big_one    COMPACT TIFLASH REPLICA;
ALTER table small_one  COMPACT TIFLASH REPLICA;

ALTER table customer  COMPACT TIFLASH REPLICA;
ALTER table lineitem  COMPACT TIFLASH REPLICA;
ALTER table nation    COMPACT TIFLASH REPLICA;
ALTER table orders    COMPACT TIFLASH REPLICA;
ALTER table part      COMPACT TIFLASH REPLICA;
ALTER table partsupp  COMPACT TIFLASH REPLICA;
ALTER table region    COMPACT TIFLASH REPLICA;
ALTER table supplier  COMPACT TIFLASH REPLICA;
```

## bench for TiPipeline
```
首先用 tiup cluster nightly 起一个集群用脚本进行测试
然后用 tiup patch 换 TiPipeline 的包，进行一样的测试
最后对比测试结果
```

## use tiup tpch bench
```
tiup bench tpch --sf=${sf} --threads=${threads} --host=${host} --port=${port} --password=${password} --db=${db} --check=true --queries='q1,q2,q3,q4,q6,q7,q8,q9,q10,q11,q12,q13,q14,q16,q17,q18,q19,q20,q21,q22' run
```
- q5 报错已知问题，所以排除
- q15 会创建 view，没法并发执行，所以排除
- sf 尽量别设太大, 容易 oom, 所以 5 or 10 吧
- threads 50 or 100 吧.
