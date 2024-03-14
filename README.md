# JDA Template Bot

## Compilation Instructions

1. Run `./gradlew clean build` or `gradlew.bat clean build` depending on your OS.
2. Find the distribution files in `build/distributions/JDA-Template-Bot-version.tar.gz`

## How to run?

1. Extract the `tar.gz` file to a folder or server,
2. Create a script to run the bot. The environment variable `DISCORD_BOT_TOKEN` must be set to your bot token.

Example Script (Bash):
```bash
#!/usr/bin/env bash

echo "Loading bot..."
export DISCORD_BOT_TOKEN="<token>"

java -Xmx1G -jar "target/JDA-Template-Bot-1.0.0.jar"
exit
```
3. Execute the script.