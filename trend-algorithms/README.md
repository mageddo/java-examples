# Trend Algorithms

Small Java 21 lab for comparing independent trend/anomaly algorithms over two paired rate series (`double[]`) whose values are between `0` and `1`.

The sample models `current[i]` versus the equivalent period in `reference[i]`. `TrendInput` calculates:

```text
delta[i] = current[i] - reference[i]
```

Each algorithm is a separate `TrendAlgorithm` implementation.

## Algorithms

- Mean delta
- Median delta
- Delta standard deviation
- OLS slope
- Theil-Sen slope
- Pearson correlation of time vs delta
- Spearman correlation of time vs delta
- Kendall correlation of time vs delta
- Paired t-test
- Wilcoxon signed-rank
- EWMA
- CUSUM

## Run

```bash
gradle run
```

## Test

```bash
gradle test
```

The thresholds in the classes are intentionally explicit demo defaults. For production anomaly detection, `EXPECTED_SIGMA` and classification thresholds should ideally be estimated from healthy historical data for each variable.
