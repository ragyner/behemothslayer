@ECHO OFF
SET DIR=%~dp0
SET APP_HOME=%DIR%
SET JAVA_EXE=java.exe
SET CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
"%JAVA_EXE%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
