@echo off
setlocal enabledelayedexpansion
echo Generating SSH key for GitHub...
ssh-keygen -t rsa -b 4096 -f "%USERPROFILE%\.ssh\id_rsa" -C "orderservice@github"
echo.
echo SSH key generated successfully!
echo.
echo Public key location: %USERPROFILE%\.ssh\id_rsa.pub
echo Private key location: %USERPROFILE%\.ssh\id_rsa
echo.
echo Next steps:
echo 1. Copy the public key: type %USERPROFILE%\.ssh\id_rsa.pub
echo 2. Go to GitHub Settings ^> SSH and GPG keys
echo 3. Click "New SSH key"
echo 4. Paste the public key content
echo 5. Save the key
pause

