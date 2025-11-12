#!/bin/bash
# Script để push code mới lên GitHub trên macOS/Linux

echo "========================================"
echo " Push Code Lên GitHub"
echo "========================================"
echo ""

# Kiểm tra git có được cài đặt không
if ! command -v git &> /dev/null; then
    echo "[ERROR] Git chưa được cài đặt!"
    echo "Vui lòng cài đặt Git trước:"
    echo "  macOS: brew install git"
    echo "  Linux: sudo apt-get install git"
    exit 1
fi

echo "[OK] Git đã được cài đặt"
echo ""

# Kiểm tra xem có remote chưa
if ! git remote -v &> /dev/null; then
    echo "[ERROR] Chưa có remote repository!"
    echo "Bạn cần thêm remote trước:"
    echo "  git remote add origin https://github.com/binhnguyen-2909/Game-tetris.git"
    exit 1
fi

echo "[OK] Remote repository đã được cấu hình"
echo ""

# Hiển thị trạng thái
echo "========================================"
echo " Trạng thái hiện tại:"
echo "========================================"
git status
echo ""

# Hỏi người dùng có muốn tiếp tục không
read -p "Bạn có muốn tiếp tục push? (y/n): " CONTINUE
if [ "$CONTINUE" != "y" ] && [ "$CONTINUE" != "Y" ]; then
    echo "Đã hủy."
    exit 0
fi

echo ""
echo "========================================"
echo " Đang thêm các file vào staging..."
echo "========================================"
git add .

if [ $? -ne 0 ]; then
    echo "[ERROR] Thêm file thất bại!"
    exit 1
fi

echo "[OK] Đã thêm các file"
echo ""

# Hỏi message commit
read -p "Nhập message cho commit (hoặc Enter để dùng message mặc định): " COMMIT_MSG
if [ -z "$COMMIT_MSG" ]; then
    COMMIT_MSG="Update: Add new features and improvements"
fi

echo ""
echo "========================================"
echo " Đang commit..."
echo "========================================"
git commit -m "$COMMIT_MSG"

if [ $? -ne 0 ]; then
    echo "[ERROR] Commit thất bại!"
    echo "Có thể không có thay đổi nào để commit."
    exit 1
fi

echo "[OK] Commit thành công!"
echo ""

echo "========================================"
echo " Đang push lên GitHub..."
echo "========================================"
git push origin main

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Push thất bại!"
    echo ""
    echo "Nếu gặp lỗi xác thực, bạn cần:"
    echo "1. Tạo Personal Access Token: GitHub -> Settings -> Developer settings -> Personal access tokens"
    echo "2. Hoặc sử dụng: git push origin main"
    echo "3. Nhập username và token khi được hỏi"
    exit 1
fi

echo ""
echo "[OK] Push thành công lên GitHub!"
echo ""
echo "Repository: https://github.com/binhnguyen-2909/Game-tetris"
echo ""

