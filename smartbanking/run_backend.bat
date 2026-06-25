@echo off
set DB_PASSWORD=admin123
set JWT_SECRET=a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2
cd /d "D:\Test Subjects\smartbanking app\smartbanking"
"C:\Users\User\AppData\Local\Temp\maven\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
pause
