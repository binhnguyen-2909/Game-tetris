# Block Blast Game

Game Tetris-style được viết bằng Java và JavaFX.

## Yêu cầu:
- **Java JDK 8** (khuyến nghị - có JavaFX đi kèm sẵn)
- Hoặc **Java 11+** với JavaFX SDK

## Cách chạy:

### Windows:
Chạy file `run-game.bat` - script sẽ tự động tìm Java và JavaFX

### Hoặc chạy thủ công:

#### Với Java 8:
```bash
javac -d . -encoding UTF-8 -cp "%JAVA_HOME%\jre\lib\ext\jfxrt.jar" src/main/java/game/*.java src/main/java/ui/*.java src/main/java/utils/*.java
java -cp .;"%JAVA_HOME%\jre\lib\ext\jfxrt.jar" game.BlockBlastGame
```

#### Với Java 11+ (cần JavaFX SDK):
```bash
javac --module-path "C:\javafx-sdk\lib" --add-modules javafx.controls -d . -encoding UTF-8 src/main/java/game/*.java src/main/java/ui/*.java src/main/java/utils/*.java
java --module-path "C:\javafx-sdk\lib" --add-modules javafx.controls game.BlockBlastGame
```

## Điều khiển:
- **Mũi tên trái/phải**: Di chuyển piece
- **Mũi tên xuống**: Rơi nhanh
- **Space**: Xoay piece

## Lưu ý:
- File leaderboard: `src/resources/leaderboard.txt`
- Nếu gặp lỗi "javac not found", cần cài JDK (không phải JRE)

