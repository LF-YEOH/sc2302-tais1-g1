#!/bin/sh
# Compiles all sources into out/ and runs the game. Extra arguments are
# passed to the game, e.g. ./build.sh --replay runs/stage1-victory.txt
rm -rf out
javac --release 17 -d out $(find src -name "*.java") && java -cp out restaurant.Main "$@"
