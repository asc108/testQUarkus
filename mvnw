#!/bin/sh
# Maven Wrapper script
MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$(cd "$(dirname "$0")" && pwd)"}
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

exec java -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  $WRAPPER_LAUNCHER $@