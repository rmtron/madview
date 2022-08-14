rem
rem Create a runnable application for Windows.
rem
rem java -cp madview-1.0-SNAPSHOT.jar --module-path /opt/javafx-sdk-18.0.1/lib \
rem --add-modules javafx.controls,javafx.fxml net.relapps.madview.Main
rem
echo "Start creating installer"

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.3.7-hotspot
set JAVA_VERSION=17
set PROJECT_VERSION=madview-1.0-SNAPSHOT
set APP_VERSION=0.0.12
set MAIN_JAR=madview-1.0-SNAPSHOT.jar
set JAVAFX_HOME=C:\rmt\software\javafx-sdk-18.0.1
set JAVAFX_JMODS=C:\rmt\software\javafx-jmods-18.0.1
set MAIN_CLASS=net.relapps.madview.main.Main
rem set type=msi
set type=app-image

cd ../target
rmdir /s /q input
rmdir /s /q java-runtime
rmdir /s /q installer

mkdir input
copy *.jar input
copy libs\*.* input


set modules=java.base,javafx.media,javafx.web,javafx.fxml,java.logging,jdk.crypto.ec
echo "Use modules: %modules%"

echo "Create Java runtime"
"%JAVA_HOME%/bin/jlink" ^
    --no-header-files ^
    --no-man-pages ^
    --compress=2 ^
    -p %JAVAFX_HOME% ^
    --strip-debug ^
    --add-modules "%modules%" ^
    --module-path %JAVAFX_JMODS%;mods ^
    --output java-runtime

rem type: "app-image" "dmg" "pkg"

echo "Create installer"
"%JAVA_HOME%/bin/jpackage" ^
--type %type% ^
--dest installer ^
--input input ^
--name madview ^
--main-class %MAIN_CLASS% ^
--main-jar ..\%MAIN_JAR% ^
--runtime-image java-runtime ^
--app-version %APP_VERSION% ^
--vendor "RELapps.net" ^
--copyright "Copyright (c) 2022 RELapps.net" ^
--icon ../installer/madview.ico

rem To run in console mode.
rem --win-console ^

