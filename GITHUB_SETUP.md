# Hướng dẫn đẩy code lên GitHub

## Các bước cần thực hiện:

### 1. Tạo tài khoản GitHub (nếu chưa có)
- Truy cập: https://github.com
- Đăng ký tài khoản miễn phí

### 2. Tạo repository mới trên GitHub
- Đăng nhập vào GitHub
- Click nút "+" ở góc trên bên phải → "New repository"
- Đặt tên repository (ví dụ: "block-blast-game")
- Chọn Public hoặc Private
- **KHÔNG** tích vào "Initialize with README" (vì bạn đã có code sẵn)
- Click "Create repository"

### 3. Cấu hình Git (chỉ cần làm 1 lần)
```bash
git config --global user.name "Tên của bạn"
git config --global user.email "email@example.com"
```

### 4. Khởi tạo Git trong project
```bash
git init
git add .
git commit -m "Initial commit"
```

### 5. Kết nối với GitHub và đẩy code
```bash
git remote add origin https://github.com/TEN_USERNAME/TEN_REPOSITORY.git
git branch -M main
git push -u origin main
```

**Lưu ý:** Thay `TEN_USERNAME` và `TEN_REPOSITORY` bằng tên GitHub và tên repository của bạn.

### 6. Nếu GitHub yêu cầu xác thực
- GitHub không còn hỗ trợ password, cần dùng Personal Access Token
- Tạo token: GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token
- Quyền: chọn "repo" (full control)
- Copy token và dùng thay cho password khi push

## Các lệnh Git thường dùng:

### Đẩy code mới lên GitHub (sau khi đã setup):

**Cách 1: Sử dụng script (Khuyến nghị)**

**Windows:**
```bash
# Chạy script tự động
push-to-github.bat
```

**macOS/Linux:**
```bash
# Cấp quyền thực thi
chmod +x push-to-github.sh

# Chạy script
./push-to-github.sh
```

**Cách 2: Chạy thủ công**

```bash
# 1. Xem trạng thái
git status

# 2. Thêm tất cả file mới và thay đổi vào staging
git add .

# 3. Commit với message mô tả
git commit -m "Mô tả thay đổi của bạn"

# 4. Push lên GitHub
git push origin main
```

### Các lệnh Git cơ bản:

```bash
# Xem trạng thái
git status

# Thêm file vào staging
git add .                    # Thêm tất cả
git add file.txt            # Thêm file cụ thể

# Commit thay đổi
git commit -m "Mô tả thay đổi"

# Đẩy lên GitHub
git push                    # Nếu đã set upstream
git push origin main        # Push lên branch main

# Lấy code mới từ GitHub
git pull

# Xem lịch sử commit
git log

# Xem các remote repository
git remote -v
```

## Lưu ý quan trọng:

1. **Personal Access Token**: GitHub không còn hỗ trợ password, cần dùng token:
   - GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token
   - Quyền: chọn "repo" (full control)
   - Dùng token thay cho password khi push

2. **Commit message**: Nên viết message rõ ràng, mô tả những thay đổi:
   - `"Add pause menu feature"`
   - `"Update UI with new theme system"`
   - `"Fix bug in scoring calculation"`

3. **Pull trước khi push**: Nếu có người khác đã push code, nên pull trước:
   ```bash
   git pull origin main
   git push origin main
   ```

