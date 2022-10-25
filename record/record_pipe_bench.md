> https://github.com/TiPipeline/tiflash/tree/pipeline_model 与 nightly 的测试对比  
# 集群
1 tidb/tikv/tiflash/pd
Amazon Linux 48vCPU amzn2-ami-kernel-5.10-hvm-2.0.20221004.0-x86_64-gp2
# 数据集 
```
rows = initialize.agg_key_types * initialize.rows_per_key
     = 100 * 100000
     = 100w
```
除了 join，其他全部回退！！！！！！！！！！
# record
## test.concurrency=20 test.round=25
- sql_mode: all
    - nightly: 
        - sum = 56807 milliseconds
        - sum = 55559 milliseconds
        - sum = 54969 milliseconds
    - pipeline:
        - 
        - 
        - 
- sql_mode: agg
    - nightly: 
        - sum = 64194 milliseconds
        - sum = 66811 milliseconds
        - sum = 65691 milliseconds
    - pipeline:
        - 
        - 
        - 
- sql_mode: where
    - nightly: 
        - sum = 45708 milliseconds
        - sum = 45237 milliseconds
        - sum = 45791 milliseconds
    - pipeline:
        - 
        - 
        - 
- sql_mode: join
    - nightly: 
        - sum = 43173 milliseconds
        - sum = 48243 milliseconds
        - sum = 43834 milliseconds
    - pipeline:
        - sum = 41538 milliseconds
        - sum = 40895 milliseconds
        - sum = 42149 milliseconds
## test.concurrency=50 test.round=25
- sql_mode: all
    - nightly: 
        - sum = 285354 milliseconds
        - sum = 273822 milliseconds
        - sum = 288048 milliseconds
    - pipeline: 
        - 
        - 
        - 
- sql_mode: agg
    - nightly: 
        - sum = 359062 milliseconds
        - sum = 375884 milliseconds
        - sum = 361296 milliseconds
    - pipeline: 
        - 
        - 
        - 
- sql_mode: where
    - nightly: 
        - sum = 134739 milliseconds
        - sum = 143058 milliseconds
        - sum = 136706 milliseconds
    - pipeline: 
        - 
        - 
        - 
- sql_mode: join
    - nightly: 
        - sum = 201318 milliseconds
        - sum = 199613 milliseconds
        - sum = 201909 milliseconds
    - pipeline: 
        - sum = 164672 milliseconds
        - sum = 154462 milliseconds
        - sum = 152556 milliseconds
## test.concurrency=100 test.round=25
- sql_mode: all
    - nightly: 
        - sum = 1087699 milliseconds
        - sum = 1085513 milliseconds
        - sum = 1096580 milliseconds
    - pipeline: 
        - 
        - 
        - 
- sql_mode: agg
    - nightly: 
        - sum = 1487090 milliseconds
        - sum = 1430741 milliseconds
        - sum = 1428144 milliseconds
    - pipeline: 
        - 
        - 
        - 
- sql_mode: where
    - nightly: 
        - sum = 563479 milliseconds
        - sum = 587625 milliseconds
        - sum = 548215 milliseconds
    - pipeline: 
        - sum = 606175 milliseconds
        - sum = 604841 milliseconds
        - 
- sql_mode: join
    - nightly: 
        - sum = 812805 milliseconds
        - sum = 826183 milliseconds
        - sum = 796363 milliseconds
    - pipeline: 
        - sum = 652126 milliseconds
        - sum = 663651 milliseconds
        - sum = 679477 milliseconds


# new record
## test.concurrency=20 test.round=25
- sql_mode: join
    - nightly: 
        - sum = 57966 milliseconds
        - sum = 61746 milliseconds
        - sum = 58826 milliseconds
    - pipeline:
        - sum = 53969 milliseconds
        - sum = 54474 milliseconds
        - sum = 52739 milliseconds
## test.concurrency=50 test.round=25
- sql_mode: join
    - nightly: 
        - sum = 341606 milliseconds
        - sum = 339916 milliseconds
        - sum = 338723 milliseconds
    - pipeline: 
        - sum = 325580 milliseconds
        - sum = 329393 milliseconds
        - sum = 321896 milliseconds
## test.concurrency=100 test.round=25
- sql_mode: join
    - nightly: 
        - sum = 1353480 milliseconds
        - sum = 1317898 milliseconds
        - sum = 1313597 milliseconds
    - pipeline:
        - sum = 1272500 milliseconds
        - sum = 1270401 milliseconds
        - sum = 1249211 milliseconds
## test.concurrency=200 test.round=25
- sql_mode: join
    - nightly: 
        - sum = 5623459 milliseconds
        - sum = 5464287 milliseconds
        - sum = 5557414 milliseconds
    - pipeline:
        - sum = 4875731 milliseconds
        - sum = 4859550 milliseconds
        - sum = 4864620 milliseconds
