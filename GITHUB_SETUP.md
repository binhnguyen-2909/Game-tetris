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

```bash
# Xem trạng thái
git status

# Thêm file vào staging
git add .

# Commit thay đổi
git commit -m "Mô tả thay đổi"

# Đẩy lên GitHub
git push

# Lấy code mới từ GitHub
git pull
```

