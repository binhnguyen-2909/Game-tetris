# Hướng Dẫn Port Game Sang iOS

## ⚠️ Lưu ý Quan Trọng

**JavaFX không chạy trực tiếp trên iOS**. iOS sử dụng Objective-C/Swift và không hỗ trợ Java runtime. Để chạy game trên iOS, bạn cần sử dụng các công cụ port đặc biệt.

## Các Giải Pháp Để Chạy Trên iOS

### 1. **Gluon Mobile (Khuyến Nghị)**

Gluon Mobile là framework cho phép port JavaFX applications sang iOS và Android.

#### Yêu Cầu:
- **Java 11+** (khuyến nghị Java 17)
- **Gluon Mobile Plugin**
- **Xcode** (chỉ trên macOS) - để build iOS app
- **Apple Developer Account** (để publish lên App Store)

#### Các Bước:

1. **Cài đặt Gluon Plugin:**
```bash
# Thêm vào build.gradle hoặc pom.xml
# Xem hướng dẫn tại: https://docs.gluonhq.com/
```

2. **Cấu hình Project:**
   - Chuyển project sang Maven hoặc Gradle
   - Thêm Gluon Mobile dependencies
   - Cấu hình iOS build settings

3. **Build cho iOS:**
```bash
# Trên macOS với Xcode
./gradlew iosBuild
# hoặc
mvn gluonfx:build
```

4. **Chạy trên Simulator:**
```bash
./gradlew iosRun
```

#### Tài Liệu:
- Website: https://gluonhq.com/products/mobile/
- Docs: https://docs.gluonhq.com/

### 2. **JavaFXPorts (Deprecated - Không Khuyến Nghị)**

JavaFXPorts đã bị deprecated và không còn được maintain. Khuyến nghị sử dụng Gluon Mobile thay thế.

### 3. **Rewrite Sang Native iOS (Swift/Objective-C)**

Nếu muốn tối ưu hiệu năng và trải nghiệm người dùng tốt nhất, bạn có thể rewrite toàn bộ game sang Swift:

- **Ưu điểm:**
  - Hiệu năng tốt nhất
  - Tích hợp tốt với iOS ecosystem
  - Có thể tận dụng các tính năng iOS đặc biệt

- **Nhược điểm:**
  - Cần viết lại toàn bộ code
  - Mất nhiều thời gian
  - Cần kiến thức Swift/Objective-C

### 4. **Web Version (PWA)**

Một giải pháp thay thế là chuyển game sang web application và chạy trên iOS Safari:

- Sử dụng **JavaFX Web** hoặc **JavaFXPorts Web**
- Hoặc rewrite sang **HTML5/JavaScript** (React, Vue, Angular)
- Có thể đóng gói thành **Progressive Web App (PWA)**

## Khuyến Nghị

### Cho Người Dùng Thông Thường:
**Sử dụng macOS/Linux script** (`run-game.sh`) để chạy game trên máy tính Mac hoặc Linux.

### Cho Developer Muốn Port Sang iOS:
1. **Bắt đầu với Gluon Mobile** - đây là cách dễ nhất và được hỗ trợ tốt nhất
2. **Cần macOS** - iOS development chỉ có thể thực hiện trên macOS với Xcode
3. **Cần Apple Developer Account** - để test trên thiết bị thật và publish lên App Store

## Script Cho macOS

Nếu bạn đang sử dụng **macOS** (không phải iOS), hãy sử dụng file `run-game.sh`:

```bash
# Cấp quyền thực thi
chmod +x run-game.sh

# Chạy game
./run-game.sh
```

## Liên Kết Hữu Ích

- [Gluon Mobile Documentation](https://docs.gluonhq.com/)
- [OpenJFX Documentation](https://openjfx.io/)
- [Apple Developer Portal](https://developer.apple.com/)
- [Xcode Download](https://developer.apple.com/xcode/)

