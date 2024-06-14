@echo off
REM Set the Git hooks path to the .githooks directory
git config core.hooksPath .githooks

REM Check if the command was successful
if %errorlevel% neq 0 (
    echo Failed to set Git hooks path. Please ensure Git is installed and you are in the correct directory.
) else (
    echo Git hooks path set to .githooks
)
