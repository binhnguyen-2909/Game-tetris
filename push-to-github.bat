@echo off
chcp 65001 >nul
REM Script để push code mới lên GitHub

echo ========================================
echo  Push Code Len GitHub
echo ========================================
echo.

REM Kiểm tra git có được cài đặt không
where git >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Git chua duoc cai dat!
    echo Vui long cai dat Git truoc: https://git-scm.com/downloads
    pause
    exit /b 1
)

echo [OK] Git da duoc cai dat
echo.

REM Kiểm tra xem có remote chưa
git remote -v >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Chua co remote repository!
    echo Ban can them remote truoc:
    echo   git remote add origin https://github.com/binhnguyen-2909/Game-tetris.git
    pause
    exit /b 1
)

echo [OK] Remote repository da duoc cau hinh
echo.

REM Hiển thị trạng thái
echo ========================================
echo  Trang thai hien tai:
echo ========================================
git status
echo.

REM Hỏi người dùng có muốn tiếp tục không
set /p CONTINUE="Ban co muon tiep tuc push? (y/n): "
IF /I NOT "%CONTINUE%"=="y" (
    echo Da huy.
    pause
    exit /b 0
)

echo.
echo ========================================
echo  Dang them cac file vao staging...
echo ========================================
git add .

IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Them file that bai!
    pause
    exit /b 1
)

echo [OK] Da them cac file
echo.

REM Hỏi message commit
set /p COMMIT_MSG="Nhap message cho commit (hoac Enter de dung message mac dinh): "
IF "%COMMIT_MSG%"=="" (
    SET "COMMIT_MSG=Update: Add new features and improvements"
)

echo.
echo ========================================
echo  Dang commit...
echo ========================================
git commit -m "%COMMIT_MSG%"

IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Commit that bai!
    echo Co the khong co thay doi nao de commit.
    pause
    exit /b 1
)

echo [OK] Commit thanh cong!
echo.

echo ========================================
echo  Dang push len GitHub...
echo ========================================
git push origin main

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Push that bai!
    echo.
    echo Neu gap loi xac thuc, ban can:
    echo 1. Tao Personal Access Token: GitHub -^> Settings -^> Developer settings -^> Personal access tokens
    echo 2. Hoac su dung: git push origin main
    echo 3. Nhap username va token khi duoc hoi
    pause
    exit /b 1
)

echo.
echo [OK] Push thanh cong len GitHub!
echo.
echo Repository: https://github.com/binhnguyen-2909/Game-tetris
echo.

pause

