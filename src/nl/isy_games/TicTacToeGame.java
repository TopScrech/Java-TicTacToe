package nl.isy_games;

public class TicTacToeGame extends BoardGame {
    private String[][] board;
    private final int SIZE = 3;

    public TicTacToeGame() {
        board = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = " ";
            }
        }
    }

    public boolean makeMove(Player player, int row, int col) {
        return true;
    }

    public boolean isGameOver() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == "") {
                    return false;
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        return null;
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            System.out.println("| " + String.join(" | ", board[i]) + " |");
        }
    }
}
