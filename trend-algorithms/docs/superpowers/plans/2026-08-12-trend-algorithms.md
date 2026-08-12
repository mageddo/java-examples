# Trend Algorithms Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extract each trend/anomaly formula into one focused Java class behind a common `TrendAlgorithm` strategy.

**Architecture:** `TrendInput` validates and prepares the paired series once. Each algorithm consumes the same input and returns an independent `TrendResult`; `Main` only registers and executes algorithms.

**Tech Stack:** Java 21, Apache Commons Math 3.6.1, JUnit 5, Gradle.

## Global Constraints

- One class per algorithm.
- Keep algorithm-specific thresholds inside that algorithm unless classification is truly shared.
- `Main` must contain no statistical logic.
- Compare results independently; do not synthesize a final score.

---

### Task 1: Shared contracts and input

**Files:** `TrendAlgorithm.java`, `TrendInput.java`, `TrendResult.java`, `TrendStatus.java`, `DirectionalStatusClassifier.java`

- [ ] Write tests proving paired input validation and delta calculation.
- [ ] Verify tests fail before production types exist.
- [ ] Implement minimal shared types.
- [ ] Run tests and keep them green.

### Task 2: Level and trend algorithms

**Files:** `MeanDeltaAlgorithm.java`, `MedianDeltaAlgorithm.java`, `DeltaStdDevAlgorithm.java`, `OlsSlopeAlgorithm.java`, `TheilSenSlopeAlgorithm.java`, `PearsonTrendAlgorithm.java`, `SpearmanTrendAlgorithm.java`, `KendallTrendAlgorithm.java`

- [ ] Add behavioral tests for level, slope, monotonic direction and stable data.
- [ ] Implement each algorithm as a separate strategy.
- [ ] Verify all tests.

### Task 3: Statistical significance and process-control algorithms

**Files:** `PairedTTestAlgorithm.java`, `WilcoxonAlgorithm.java`, `EwmaAlgorithm.java`, `CusumAlgorithm.java`

- [ ] Add tests for significant degradation, no-change, EWMA and CUSUM degradation.
- [ ] Implement each algorithm independently.
- [ ] Verify all tests.

### Task 4: Runner and packaging

**Files:** `Main.java`, `README.md`, `build.gradle`, `settings.gradle`

- [ ] Add the demo runner with all algorithms.
- [ ] Compile and execute the sample.
- [ ] Run the complete test suite.
- [ ] Package the project as a zip.
