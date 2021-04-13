@echo off
title Oblivion PK V1.0 - Loading
color 0b

echo.
echo ------------------------------------------------
echo     Oblivion PK V1.0 (x64), Starting up...
echo ------------------------------------------------
if defined java goto :x121
if exist "%HOMEDRIVE%/Program Files (x86)/Java/jre7/bin/"  goto x764
if exist "%HOMEDRIVE%/Program Files/Java/jre7/bin/" goto x786

:x121
title  Oblivion PK V1.0 (x64) - Loader
echo         Auto-Detected 64-bit (x64) System
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo    . C l i e n t   o u t p u t   b e l o w .
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo.

java -Xmx512m -jar SimplicityPs.jar cmdline
pause
exit


:x764
title  Oblivion PK V1.0 (x64) - Loader
echo         Auto-Detected 64-bit (x64) System
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo    . C l i e n t   o u t p u t   b e l o w .
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo.

"%HOMEDRIVE%/Program Files (x86)/Java/jre7/bin/java.exe" -Xmx512m -jar Oblivion.jar cmdline
pause
exit

:delx764
"%HOMEDRIVE%/Program Files (x86)/Java/jre7/bin/java.exe" -Xmx512m -jar Oblivion.jar cmdline
pause
exit

:x786
title  Oblivion PK V1.0 (x86) - Loader
echo         Auto-Detected 32-bit (x86) System
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo    . C l i e n t   o u t p u t   b e l o w .
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo.

"%HOMEDRIVE%/Program Files/Java/jre7/bin/java.exe" -Xmx512m -jar Oblivion.jar cmdline
pause
exit

:delx786
"%HOMEDRIVE%/Program Files/Java/jre7/bin/java.exe" -Xmx512m -jar Oblivion.jar cmdline
pause
exit