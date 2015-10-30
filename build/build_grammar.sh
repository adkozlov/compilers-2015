#!/bin/bash
cd ..
java -jar lib/antlr4-4.5.1-1.jar -visitor -listener -package ru.spbau.kozlov.llang.grammar src/main/java/ru/spbau/kozlov/llang/grammar/LLang.g4