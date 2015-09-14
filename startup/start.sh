#!/bin/bash
#
# start.sh
# Start application if not already running.

# Application directory where all applications must reside
APP_HOME="/opt/application"

# Import common functions and set common global environment variables like JAVA_HOME
source ${APP_HOME}/common.sh

# This could include additional custom environment variables
source ${APP_HOME}/application.env

if [[ -z ${APP_NAME} ]]
then
  echo "Error: $0: APP_NAME variable is not defined in application.env"
  exit 1
fi

# Check if app is currently running
PID=$( get_app_pid ${APP_NAME} )

if [[ ${PID} ]]; then
  # App is running, don't do anything
  echo "${APP_NAME} is already running (PID: ${PID})"
  exit 0
fi

# App is not running, start the app.
echo "Starting ${APP_NAME}"

# Change directory to application home so we know what our current directory is.
cd ${APP_HOME}

if [[ ${LOG_NAME} ]]; then
  LOG_OPTS="-Dlogging.config=${APP_HOME}/${LOG_NAME}"
fi

# Make sure the logs directory exists
mkdir -p ${APP_HOME}/logs

# Certain default options such as spring.config.location are assumed to be standard. Other options can be specified in .env file
nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} ${LOG_OPTS} -jar ${APP_HOME}/${APP_NAME}.jar < /dev/null > logs/start.out 2> logs/start.err &

# If we don't pause a few seconds here, it appears that jenkins kills the session too quickly before the background process can spawn
if [[ ${WAIT_SEC} ]]; then
  echo "${APP_NAME} launched. Pausing for ${WAIT_SEC} seconds ..."
  sleep ${WAIT_SEC}
fi

# Check if app is running
PID=$( get_app_pid ${APP_NAME} )

if [[ ${PID} ]]; then
  echo "${APP_NAME} is running (PID: ${PID})"
  exit 0
else
  echo "${APP_NAME} is NOT running!"
  exit 1
fi
