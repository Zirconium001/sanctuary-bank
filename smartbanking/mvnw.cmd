@REM ----------------------------------------------------------------------------
@REM Maven Wrapper - downloads and runs Maven
@REM ----------------------------------------------------------------------------
@setlocal
set WRAPPER_JAR="%USERPROFILE%\.m2\wrapper\maven-wrapper.jar"
set WRAPPER_URL=https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
if exist %WRAPPER_JAR% goto run
echo Downloading Maven Wrapper...
mkdir "%USERPROFILE%\.m2\wrapper" 2>nul
powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%')" 2>nul || curl -sS -o "%WRAPPER_JAR%" %WRAPPER_URL% 2>nul
:run
@java -jar %WRAPPER_JAR% %*
@endlocal
