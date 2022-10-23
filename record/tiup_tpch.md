## data
tpch10/tpch50
## query
```
tiup bench tpch --sf=${sf} --threads=${threads} --host=${host} --port=${port} --password=${password} --db=${db} --check=true --queries='q1,q2,q3,q4,q6,q8,q9,q10,q11,q12,q13,q14,q16,q17,q18,q19,q20,q21,q22' run
```
- q5 报错已知问题，所以排除
- q15 会创建 view，没法并发执行，所以排除
- q7 有点问题，没调通
## record
### tpch5
- tpch all query threads = 1
    - nightly vs pipeline
    ```
    Q1: 0.34s       Q1: 0.34s
    Q10: 0.37s      Q10: 0.38s
    Q11: 0.17s      Q11: 0.17s
    Q12: 0.17s      Q12: 0.17s
    Q13: 0.30s      Q13: 0.30s
    Q14: 0.10s      Q14: 0.23s
    Q16: 0.37s      Q16: 0.37s
    Q17: 0.50s      Q17: 0.44s
    Q18: 0.64s      Q18: 0.64s
    Q19: 0.30s      Q19: 0.30s
    Q2: 0.47s       Q2: 0.46s
    Q20: 0.17s      Q20: 0.17s
    Q21: 0.60s      Q21: 0.57s
    Q22: 0.10s      Q22: 0.10s
    Q3: 0.29s       Q3: 0.30s
    Q4: 0.23s       Q4: 0.23s
    Q6: 0.10s       Q6: 0.10s
    Q7: 0.44s       Q7: 0.44s
    Q8: 0.30s       Q8: 0.31s
    Q9: 0.97s       Q9: 0.91s
    ```
- q11
    - nightly vs pipeline
    ```
    1 thread:    0.17s    0.17s
    5 thread:    0.21s    0.19s
    10 thread:   0.24s    0.24s
    20 thread:   0.45s    0.39s
    50 thread:   1.00s    0.90s
    ```
### tpch10
- tpch all query threads = 1
    - nightly vs pipeline
    ```
    Q1: 0.64s       Q1: 0.64s
    Q10: 0.57s      Q10: 0.58s
    Q11: 0.22s      Q11: 0.22s
    Q12: 0.30s      Q12: 0.30s
    Q13: 0.64s      Q13: 0.64s
    Q14: 0.36s      Q14: 0.36s
    Q16: 0.31s      Q16: 0.30s
    Q17: 0.92s      Q17: 0.87s
    Q18: 1.11s      Q18: 1.11s
    Q19: 0.50s      Q19: 0.49s
    Q2: 0.30s       Q2: 0.30s
    Q20: 0.30s      Q20: 0.30s
    Q21: 1.45s      Q21: 1.42s
    Q22: 0.16s      Q22: 0.17s
    Q3: 0.49s       Q3: 0.45s
    Q4: 0.62s       Q4: 0.57s
    Q6: 0.17s       Q6: 0.17s
    Q7: 0.64s       Q7: 0.64s
    Q8: 0.44s       Q8: 0.44s
    Q9: 2.17s       Q9: 2.10s
    ```
- q11
    - nightly vs pipeline
    ```
    1 thread:    0.23s       0.23s
    5 thread:    0.29s       0.29s
    10 thread:   0.42s       0.43s
    20 thread:   0.81s       0.77s
    50 thread:   1.97s       1.91s
    ```
- q1
    - nightly vs pipeline
    ```
    1 thread:    0.61s       0.64s
    5 thread:    2.18s       2.21s
    10 thread:   4.28s       4.28
    20 thread:   8.60s       8.55s
    50 thread:   21.12s      21.76s
    100 thread:  42.68s      43.43s
    ```
- q16
    - nightly vs pipeline
    ```
    1 thread:    0.33s     0.33s
    5 thread:    0.73s     0.68s
    10 thread:   1.36s     1.32s
    20 thread:   2.62s     2.73s
    50 thread:   6.82s     6.97s
    ```