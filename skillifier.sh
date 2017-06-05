#!/bin/bash
#Script to run, stop and check skillifier application.

<<HOWTOUSE
    Uses following parameters:
        -r  Runs the application
        -s  Kills the application process
        -st Tells the status of the server
        -rs Stops and starts the server
        -help View help text 
HOWTOUSE

#Location to skillifier jar file:
JAR_LOCATION=$(pwd)/skillifier/target/skillifier-cli-0.1.jar

RETVAL=-1

function startSkillifier {
    checkStatus
    if [ $RETVAL == -1 ]
    then
        echo "Starting skillifier and writing output to applog.txt..."
        java -jar $JAR_LOCATION > $(pwd)/applog.txt &
    else
        echo "Skillifier already running! Pid:" $RETVAL
    fi
}

function stopSkillifier {
    checkStatus
    if [ $RETVAL == -1 ]
    then
        echo "Application not running and cannot be stopped"
    else
        echo "Stopping skillifier with pid: " $RETVAL
        kill $RETVAL
    fi
}

function printHelp {
    echo "Script to run skillifier applicaiton."
    echo "Uses following parameters:"
    echo "   -r --run:    Runs the application"
    echo "   -s --stop:   Kills the application process"
    echo "   -st --status: Tells the status of the server by showing grep"
    echo "   -rs --restart: Stops and starts the server"
    echo "   -h --help: View help (this text)"
}

function checkStatus {
    PIDNUMBER=`ps aux | grep -v grep | grep skillifier-cli- | head -n1 | awk '{print $2}'`
    if [ -z "$PIDNUMBER" ]
    then
        RETVAL=-1
    else
        RETVAL=$PIDNUMBER
    fi
}

case $1 in
    -r|--run)
    startSkillifier
    ;;
    -s|--stop)
    stopSkillifier
    ;;
    -rs|--restart)
    stopSkillifier
    checkStatus
    startSkillifier
    ;;
    -st|--status)
    checkStatus
    if [ $RETVAL == -1 ]
    then
        echo "Application is not running"
    else
        echo "Application is runnig with pid:" $RETVAL
    fi
    ;;
    *)
    printHelp
    ;;
esac

exit 0

