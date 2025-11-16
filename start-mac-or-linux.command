#!/bin/bash
#This is a shebang (it must be on the first line). It tells the operating system that this file should run using the bash shell.
#Without it, the operating system might not know how to interpret the commands.

# This detects the operating system type and stores it in OS
OS="$(uname -s)"

#$0 is the path of the currently running script and dirname "$0" takes a full pth and returns the directory part,
#stripping off the file name
#cd "$(dirname "$0")" changes the current working directory of the terminal to the directory where the script lives.
#Essentially, it ensures that when you run mvn spring-boot:run, it runs from the project folder, no matter where the user double-clicked
#the script from.
cd "$(dirname "$0")"

# Print OS-specific message
if [[ "$OS" == "Darwin" ]]; then
    echo "Starting Date Night Planner on macOS..."
    mvn spring-boot:run
elif [[ "$OS" == "Linux" ]]; then
    echo "Starting Date Night Planner on Linux..."
    mvn spring-boot:run
else
    echo "Unsupported OS: $OS"
    exit
#fi ends the if block. Every "if" needs a corresponding fi in command
fi

# Keep terminal open
read -p "Press [Enter] to close..."
