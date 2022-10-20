> https://github.com/TiPipeline/tiflash/tree/pipeline_model 与 nightly 的测试对比  
# 集群
1 tidb/tikv/tiflash/pd
todo: 配置
# 数据集
```
rows = initialize.agg_key_types * initialize.rows_per_key
     = 100 * 100000
     = 100w
```
# record
## test.concurrency=50 test.round=25
- sql_mode: all
    - nightly: 
    - pipeline: 
- sql_mode: agg
    - nightly: 
    - pipeline: 
- sql_mode: where
    - nightly: 
    - pipeline: 
- sql_mode: join
    - nightly: 
    - pipeline: 
