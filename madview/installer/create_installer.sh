#!/bin/bash
#
# Create a runnable application for Linux.
#
# java -cp madview-1.0-SNAPSHOT.jar --module-path /opt/javafx-sdk-18.0.1/lib \
# --add-modules javafx.controls,javafx.fxml net.relapps.madview.Main
#
echo "Start creating installer"

export JAVA_HOME=/opt/jdk-17
export JAVA_VERSION=17
export PROJECT_VERSION=madview-1.0-SNAPSHOT
export APP_VERSION=0.0.12
export MAIN_JAR=madview-1.0-SNAPSHOT.jar
export JAVAFX_HOME=/opt/javafx-sdk-18.0.1
export JAVAFX_JMODS=/opt/javafx-jmods-18.0.1
export APP_VERSION=0.0.12
export MAIN_CLASS=net.relapps.madview.main.Main
export type=app-image

cd ../target
rm -rf input java-runtime installer

mkdir input
cp *.jar input
cp libs/* input


# echo "Find dependencies"
# classes=`find classes -name "*.class"`
# modules=`$JAVA_HOME/bin/jdeps \
#   --multi-release ${JAVA_VERSION} \
#   --print-module-deps \
#   --module-path $JAVAFX_HOME/lib \
#   --class-path "classes/net/relapps/madview" \
#     classes/net/relapps/madview/main \
#     classes/net/relapps/fx \
#     classes/net/relapps/madview/md \
#     classes/net/relapps/madview/cntrl \
#     libs`


# echo "Modules found: $modules"
# modules=$modules,javafx.base,java.logging,javafx.fxml,javafx.media,javafx.web
modules=java.base,javafx.media,javafx.web,javafx.fxml,java.logging,jdk.crypto.ec
echo "Use modules: $modules"
# echo "Modules found: $modules"

echo "Create Java runtime"
$JAVA_HOME/bin/jlink \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    -p $JAVAFX_HOME \
    --strip-debug \
    --add-modules "${modules}" \
    --module-path $JAVAFX_JMODS:mods \
    --output java-runtime

# type: "app-image" "dmg" "pkg"

echo "Create installer"
$JAVA_HOME/bin/jpackage \
--type $type \
--dest installer \
--input input \
--name madview \
--main-class $MAIN_CLASS \
--main-jar ${MAIN_JAR} \
--runtime-image java-runtime \
--app-version ${APP_VERSION} \
--vendor "RELapps.net" \
--copyright "Copyright © 2022 RELapps.net" \
--icon ../installer/madview.png


# --java-options "--add-modules=javafx.controls,javafx.fxml" \

# --mac-package-identifier com.acme.app
# --mac-package-name ACME
# --icon src/main/logo/macosx/duke.icns
