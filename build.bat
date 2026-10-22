   @echo off
   if exist out rmdir /s /q out
   dir /s /b src\*.java > sources.txt
   javac -d out @sources.txt && java -cp out restaurant.Main %*