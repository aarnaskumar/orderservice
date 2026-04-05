@echo off
setlocal enabledelayedexpansion

set GIT_PATH=C:\Program Files\Git\bin\git.exe
set PROJECT_DIR=C:\Users\pc\IdeaProjects\orderservice
set GITHUB_REPO=https://github.com/aarnaskumar/orderservice.git

echo ========================================
echo Order Service - GitHub Push Script
echo ========================================
echo.

REM Check if Git is available
if not exist "%GIT_PATH%" (
    echo Error: Git not found at %GIT_PATH%
    echo Please install Git first.
    pause
    exit /b 1
)

echo Git found: %GIT_PATH%
echo.

REM Navigate to project directory
cd /d "%PROJECT_DIR%"
if errorlevel 1 (
    echo Error: Could not navigate to %PROJECT_DIR%
    pause
    exit /b 1
)

echo Project directory: %cd%
echo.

REM Check if git is already initialized
if not exist ".git" (
    echo Initializing git repository...
    "%GIT_PATH%" init
    if errorlevel 1 (
        echo Error: Failed to initialize git repository
        pause
        exit /b 1
    )
    echo Git repository initialized successfully.
    echo.
)

REM Configure git user
echo Configuring git user...
"%GIT_PATH%" config user.name "GitHub User"
"%GIT_PATH%" config user.email "user@github.com"
echo.

REM Check if remote already exists
"%GIT_PATH%" remote get-url origin >nul 2>&1
if errorlevel 1 (
    echo Adding remote origin...
    "%GIT_PATH%" remote add origin %GITHUB_REPO%
) else (
    echo Remote origin already exists. Updating URL...
    "%GIT_PATH%" remote set-url origin %GITHUB_REPO%
)
echo Remote URL: %GITHUB_REPO%
echo.

REM Add all files
echo Adding files to staging area...
"%GIT_PATH%" add .
if errorlevel 1 (
    echo Error: Failed to add files
    pause
    exit /b 1
)
echo Files added successfully.
echo.

REM Check if there are changes to commit
"%GIT_PATH%" diff-index --quiet HEAD --
if errorlevel 1 (
    REM There are changes, commit them
    echo Committing changes...
    "%GIT_PATH%" commit -m "Initial commit: Add Order Service Spring Boot application with AGENTS.md and SSH setup"
    if errorlevel 1 (
        echo Error: Failed to commit changes
        pause
        exit /b 1
    )
    echo Changes committed successfully.
) else (
    echo No changes to commit.
)
echo.

REM Push to GitHub
echo Pushing to GitHub...
"%GIT_PATH%" push -u origin main 2>&1
if errorlevel 1 (
    echo.
    echo Error pushing to GitHub. Trying with 'master' branch instead...
    "%GIT_PATH%" push -u origin master 2>&1
    if errorlevel 1 (
        echo Error: Failed to push to GitHub
        echo Please check your GitHub credentials and SSH setup.
        pause
        exit /b 1
    )
)
echo.
echo Push completed successfully!
echo.
echo Your Order Service has been pushed to:
echo %GITHUB_REPO%
echo.
pause

