package game;

import utils.GameScore;
import utils.LeaderboardManager;

public class GameController {
    private GameBoard board;
    private Piece currentPiece;
    private Piece nextPiece;
    private Difficulty difficulty;
    private GameMode gameMode;
    private LeaderboardManager leaderboardManager;
    private boolean gameOver = false;
    private long lastFallTime = 0;
    private long startTime = 0;
    private int currentFallSpeed; // Tốc độ rơi hiện tại (cho Marathon mode)
    private int piecesPlaced = 0; // Số viên gạch đã đặt (cho Challenge mode)
    
    // Combo tracking
    private int consecutiveClears = 0; // Số lần xóa dòng liên tiếp
    private int bestCombo = 0; // Best combo cao nhất
    
    // Speed bonus tracking
    private long pieceSpawnTime = 0; // Thời gian piece được spawn
    
    // Sprint mode
    private static final int SPRINT_TARGET_ROWS = 40;
    
    // Challenge mode
    private int challengeTargetScore = 1000; // Điểm mục tiêu
    private int challengeMaxPieces = 50; // Số viên gạch tối đa

    public GameController(Difficulty difficulty, GameMode gameMode) {
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.board = new GameBoard();
        this.board.initializeWithRows(difficulty.getInitialRows());
        this.leaderboardManager = new LeaderboardManager();
        this.currentPiece = new Piece();
        this.nextPiece = new Piece();
        this.lastFallTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.pieceSpawnTime = System.currentTimeMillis();
        
        // Apply fall speed multiplier from settings
        ui.GameSettings settings = ui.GameSettings.getInstance();
        int baseSpeed = difficulty.getFallSpeed();
        this.currentFallSpeed = (int)(baseSpeed / settings.getFallSpeedMultiplier());
        
        // Reset rows cleared cho Sprint mode
        if (gameMode == GameMode.SPRINT) {
            board.resetRowsCleared();
        }
    }

    public void update() {
        if (gameOver) return;

        // Zen mode không tự động rơi
        if (gameMode == GameMode.ZEN) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        
        // Apply fall speed multiplier from settings
        ui.GameSettings settings = ui.GameSettings.getInstance();
        int baseSpeed = difficulty.getFallSpeed();
        
        // Marathon mode: tăng tốc độ theo thời gian
        if (gameMode == GameMode.MARATHON) {
            // Tăng tốc độ mỗi 30 giây
            int speedLevel = (int)(elapsedTime / 30000);
            baseSpeed = Math.max(100, baseSpeed - (speedLevel * 50));
        }
        
        // Apply speed multiplier
        currentFallSpeed = (int)(baseSpeed / settings.getFallSpeedMultiplier());

        if (currentTime - lastFallTime >= currentFallSpeed) {
            dropPiece();
            lastFallTime = currentTime;
        }
        
        // Kiểm tra điều kiện kết thúc cho các mode
        checkGameOverConditions();
    }

    public void moveLeft() {
        currentPiece.moveLeft();
        if (!board.canPlace(currentPiece)) {
            currentPiece.moveRight();
        }
    }

    public void moveRight() {
        currentPiece.moveRight();
        if (!board.canPlace(currentPiece)) {
            currentPiece.moveLeft();
        }
    }

    public void rotate() {
        ui.GameSettings settings = ui.GameSettings.getInstance();
        
        if (settings.isAssistMode()) {
            // Assist mode: thử tất cả các hướng xoay để tìm vị trí tốt nhất
            Piece bestPiece = null;
            int bestY = currentPiece.getY();
            int originalY = currentPiece.getY();
            
            // Thử 4 hướng xoay
            for (int rotation = 0; rotation < 4; rotation++) {
                currentPiece.rotateClockwise();
                // Thử di chuyển xuống để tìm vị trí tốt nhất
                int testY = originalY;
                while (board.canPlace(currentPiece)) {
                    currentPiece.setY(testY);
                    if (isGoodPosition(currentPiece)) {
                        bestPiece = currentPiece;
                        bestY = testY;
                        break;
                    }
                    testY++;
                }
                if (bestPiece != null) break;
            }
            
            // Nếu tìm thấy vị trí tốt, sử dụng nó
            if (bestPiece != null) {
                currentPiece.setY(bestY);
                return;
            }
            
            // Nếu không tìm thấy, xoay bình thường
            currentPiece.setY(originalY);
            currentPiece.rotateClockwise();
            if (!board.canPlace(currentPiece)) {
                currentPiece.rotateCounterClockwise();
            }
        } else {
            // Normal mode: xoay bình thường
            currentPiece.rotateClockwise();
            if (!board.canPlace(currentPiece)) {
                // Xoay lại nếu không thể đặt
                currentPiece.rotateCounterClockwise();
            }
        }
    }
    
    /**
     * Kiểm tra xem vị trí có tốt không (cho assist mode)
     * Một vị trí tốt là vị trí gần sát các block khác hoặc tạo được đường thẳng
     */
    private boolean isGoodPosition(Piece piece) {
        // Đơn giản: kiểm tra xem piece có ở vị trí hợp lý không
        // Có thể cải thiện bằng cách check xem có tạo được line không
        return board.canPlace(piece) && piece.getY() >= 0;
    }

    public void dropPiece() {
        currentPiece.moveDown();
        if (!board.canPlace(currentPiece)) {
            currentPiece.setY(currentPiece.getY() - 1);
            board.placePiece(currentPiece);
            piecesPlaced++;
            
            // Tính speed bonus
            long currentTime = System.currentTimeMillis();
            long timeTaken = currentTime - pieceSpawnTime;
            int speedBonus = calculateSpeedBonus(timeTaken);
            
            // Xóa hàng và tính điểm
            int rowsCleared = board.clearFullRows();
            
            if (rowsCleared > 0) {
                // Tính combo multiplier
                consecutiveClears++;
                // Update best combo
                if (consecutiveClears > bestCombo) {
                    bestCombo = consecutiveClears;
                }
                double comboMultiplier = 1.0 + (consecutiveClears - 1) * 0.5;
                
                // Lấy chain multiplier
                double chainMultiplier = GameBoard.getChainMultiplier(rowsCleared);
                
                // Tính T-Spin bonus
                int tSpinBonus = 0;
                if (currentPiece.getType() == PieceType.T && currentPiece.wasRotated()) {
                    tSpinBonus = getTSpinBonus(rowsCleared);
                }
                
                // Tính điểm với tất cả bonus và multiplier (skip nếu practice mode)
                ui.GameSettings settings = ui.GameSettings.getInstance();
                if (!settings.isPracticeMode()) {
                    board.calculateScore(rowsCleared, chainMultiplier, comboMultiplier, 
                                       speedBonus, tSpinBonus, consecutiveClears);
                } else {
                    // Practice mode: chỉ xóa dòng, không tính điểm
                    board.resetScore();
                }
            } else {
                // Reset combo nếu không xóa hàng
                consecutiveClears = 0;
            }
            
            // Reset rotation flag và spawn piece mới
            currentPiece.resetRotationFlag();
            currentPiece = nextPiece;
            nextPiece = new Piece();
            pieceSpawnTime = System.currentTimeMillis();

            if (board.isGameOver(currentPiece)) {
                gameOver = true;
            }
        }
    }
    
    /**
     * Tính speed bonus: (1000 - timeTaken) / 100 * 100
     * Minimum: 0 (không phạt)
     */
    private int calculateSpeedBonus(long timeTakenMs) {
        if (timeTakenMs >= 1000) {
            return 0;
        }
        return (int)((1000 - timeTakenMs) / 100) * 100;
    }
    
    /**
     * Tính T-Spin bonus
     */
    private int getTSpinBonus(int rowsCleared) {
        switch (rowsCleared) {
            case 1: return 500;  // Single T-Spin
            case 2: return 1000; // Double T-Spin
            case 3: return 1500; // Triple T-Spin
            default: return 500; // Default
        }
    }
    
    // Cho Zen mode - rơi từng bước một
    public void stepDown() {
        if (gameMode == GameMode.ZEN && !gameOver) {
            dropPiece();
        }
    }

    public void speedDrop() {
        while (board.canPlace(currentPiece)) {
            currentPiece.moveDown();
        }
        currentPiece.setY(currentPiece.getY() - 1);
        board.placePiece(currentPiece);
        piecesPlaced++;
        
        // Tính speed bonus (speed drop có thể nhanh hơn)
        long currentTime = System.currentTimeMillis();
        long timeTaken = currentTime - pieceSpawnTime;
        int speedBonus = calculateSpeedBonus(timeTaken);
        
        // Xóa hàng và tính điểm
        int rowsCleared = board.clearFullRows();
        
        if (rowsCleared > 0) {
            // Tính combo multiplier
            consecutiveClears++;
            // Update best combo
            if (consecutiveClears > bestCombo) {
                bestCombo = consecutiveClears;
            }
            double comboMultiplier = 1.0 + (consecutiveClears - 1) * 0.5;
            
            // Lấy chain multiplier
            double chainMultiplier = GameBoard.getChainMultiplier(rowsCleared);
            
            // Tính T-Spin bonus
            int tSpinBonus = 0;
            if (currentPiece.getType() == PieceType.T && currentPiece.wasRotated()) {
                tSpinBonus = getTSpinBonus(rowsCleared);
            }
            
            // Tính điểm với tất cả bonus và multiplier
            board.calculateScore(rowsCleared, chainMultiplier, comboMultiplier, 
                               speedBonus, tSpinBonus, consecutiveClears);
        } else {
            // Reset combo nếu không xóa hàng
            consecutiveClears = 0;
        }
        
        // Reset rotation flag và spawn piece mới
        currentPiece.resetRotationFlag();
        currentPiece = nextPiece;
        nextPiece = new Piece();
        pieceSpawnTime = System.currentTimeMillis();

        if (board.isGameOver(currentPiece)) {
            gameOver = true;
        }
    }

    public GameBoard getBoard() {
        return board;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    public int getScore() {
        return board.getScore();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void saveScore(String playerName) {
        String modeInfo = gameMode.toString() + " - " + difficulty.toString();
        GameScore score = new GameScore(playerName, board.getScore(), modeInfo);
        leaderboardManager.addScore(score);
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
    
    public GameMode getGameMode() {
        return gameMode;
    }
    
    public int getTotalRowsCleared() {
        return board.getTotalRowsCleared();
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public int getPiecesPlaced() {
        return piecesPlaced;
    }
    
    private void checkGameOverConditions() {
        // Sprint mode: kết thúc khi xóa đủ 40 dòng
        if (gameMode == GameMode.SPRINT) {
            if (board.getTotalRowsCleared() >= SPRINT_TARGET_ROWS) {
                gameOver = true; // Chiến thắng
                return;
            }
        }
        // Challenge mode: kiểm tra điều kiện thử thách
        else if (gameMode == GameMode.CHALLENGE) {
            // Kiểm tra nếu đã đạt điểm mục tiêu
            if (board.getScore() >= challengeTargetScore) {
                gameOver = true; // Chiến thắng
                return;
            }
            // Kiểm tra nếu đã hết số viên gạch
            if (piecesPlaced >= challengeMaxPieces) {
                gameOver = true; // Thua - hết viên gạch nhưng chưa đạt điểm
                return;
            }
        }
        
        // Kiểm tra game over thông thường (board đầy)
        if (board.isGameOver(currentPiece)) {
            gameOver = true;
        }
    }
    
    public boolean isVictory() {
        if (!gameOver) {
            return false; // Game chưa kết thúc
        }
        
        if (gameMode == GameMode.SPRINT) {
            return board.getTotalRowsCleared() >= SPRINT_TARGET_ROWS;
        } else if (gameMode == GameMode.CHALLENGE) {
            return board.getScore() >= challengeTargetScore;
        }
        // Marathon và Zen mode không có victory condition, chỉ có game over
        return false;
    }
    
    public String getGameModeInfo() {
        switch (gameMode) {
            case MARATHON:
                return "MARATHON - Tốc độ: " + currentFallSpeed + "ms";
            case SPRINT:
                return "SPRINT - " + board.getTotalRowsCleared() + "/" + SPRINT_TARGET_ROWS + " dòng";
            case CHALLENGE:
                return "CHALLENGE - " + board.getScore() + "/" + challengeTargetScore + " điểm (" + piecesPlaced + "/" + challengeMaxPieces + " viên)";
            case ZEN:
                return "ZEN - Thư giãn";
            default:
                return "";
        }
    }
    
    public int getConsecutiveClears() {
        return consecutiveClears;
    }
    
    public int getBestCombo() {
        return bestCombo;
    }
    
    public ScoreBreakdown getLastScoreBreakdown() {
        return board.getLastScoreBreakdown();
    }
}
