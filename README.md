Mach 1 March Results Application v1.0

This app takes in data from both the group stage and final stage, and outputs a runner's win/loss record and their average and best times for a level.

USAGE:

In its current state, you'll need to run the program from the command line. Open Windows command prompt or powershell and navigate to the file location. Enter the command java -jar M1m-Results-App.jar

It should print out "Done reading results", then "Enter runner name: ". At this point you can type in the runner's name (case doesn't matter), then type in the level when asked. Certain levels will also ask if played on NCS.

The program will keep asking for new runner names until you type exit, quit, or close the command line.

The group results data is included. To update the bracket results, go to the bracket results spreadsheet from the form and download the .csv version. Put that in the same place this doc, the app, and the group stage results are and rename it to "FinalResults".

If when running the app, before it says "Done reading results", it prints "Creating new object for <player name>. Check to make sure the submitted name is correct." that means that in the final bracket a name was submitted that wasn't the same as the name used in groups. It should be pretty easy to figure out which name it is and what the difference is.

Change Log:
Version 1.0 - Works on command line for group stage and bracket.