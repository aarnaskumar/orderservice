@echo off
echo Checking for Git installation...

REM Try to find Git in common locations
if exist "C:\Program Files\Git\bin\git.exe" (
    set GIT_PATH="C:\Program Files\Git\bin\git.exe"
    goto :git_found
)

if exist "C:\Program Files (x86)\Git\bin\git.exe" (
    set GIT_PATH="C:\Program Files (x86)\Git\bin\git.exe"
    goto :git_found
)

REM Check if git is in PATH
git --version >nul 2>&1
if %errorlevel% equ 0 (
    set GIT_PATH=git
    goto :git_found
)

echo Git not found. Please install Git from https://git-scm.com/downloads
echo Then run this script again.
pause
exit /b 1

:git_found
echo Git found at %GIT_PATH%

cd /d C:\Users\pc\IdeaProjects\orderservice

echo Initializing git repository...
%GIT_PATH% init

echo Adding remote origin...
%GIT_PATH% remote add origin https://github.com/aarnaskumar/orderservice.git

echo Adding all files...
%GIT_PATH% add .

echo Committing changes...
%GIT_PATH% commit -m "Initial commit: Add Order Service Spring Boot application with AGENTS.md"

echo Pushing to GitHub...
%GIT_PATH% push -u origin main

echo Done!
pause
