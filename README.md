# Cron Parser

This is a simple cron parser that allows you to interpret cron expressions. The
project includes an initialization script (`init.sh`) to set up the environment (OS X / Linux).

## Getting Started

Follow the steps below to set up the cron parser:

### Step 1: Initialization

Run the `init.sh` script to initialize the environment. Script has the following steps:

- Install SDKMAN
- Install Maven and Java 17 using SDKMAN
- Set Java 17 as default
- Display installed versions
- Cleanup
- Generate Target

```bash
sh init.sh
```

### Step 2: Run

Run the `cron-parser.sh` script to along with the expression to see the results.

```bash
sh cron-parser.sh "*/15 0 1,15 * 1-5 /usr/bin/find"
```

### Test

While the init step runs tests, you can run the tests again using

```bash
mvn test
```

