# Gradle Test Reports

A small web app to browse the test reports of your Gradle projects, starting
from a directory.

## What it does

Walk through your test results in 4 steps — each step has its own shareable URL:

1. **Directory** — enter an absolute path.
2. **Projects** — pick a Gradle project found under that directory.
3. **Results** — pick a test suite (e.g. `test`, `componentTest`), with total
   time, number of tests, passed, failed and skipped.
4. **Report** — see the detailed test table with search, filters, sorting,
   duration bars and CSV/JSON export.

The directory you type is remembered in the browser, so you don't have to type
it again.

## How to run

```bash
./gradlew quarkusDev
```

Then open http://localhost:8080

> Use `quarkusDev` for hot reload — changes to code and pages are picked up
> without restarting.
