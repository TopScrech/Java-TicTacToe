package nl.isy_games;

package classes;

public class TicTacToeGame extends BoardGame {
    private String[][] board;
    private final int SIZE = 3;
    private Player playerX;
    private Player playerO;

    public TicTacToeGame() {
        board = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = " ";
            }
        }
    }

    @Override
    public boolean makeMove(Player player, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (!board[row][col].equals(" ")) {
            return false;
        }
        board[row][col] = player.getMark();
        return true;
    }


    @Override
    public boolean isGameOver() {
        if (getWinner() != null) {
            return true;
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setPlayers(Player p1, Player p2) {
        if (p1.getMark().equals("X")) {
            playerX = p1;
            playerO = p2;
        } else {
            playerX = p2;
            playerO = p1;
        }
    }

    @Override
    public Player getWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (!board[i][0].equals(" ") &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2])) {
                return board[i][0].equals("X") ? playerX : playerO;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (!board[0][i].equals(" ") &&
                    board[0][i].equals(board[1][i]) &&
                    board[1][i].equals(board[2][i])) {
                return board[0][i].equals("X") ? playerX : playerO;
            }
        }

        if (!board[0][0].equals(" ") &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            return board[0][0].equals("X") ? playerX : playerO;
        }

        if (!board[0][2].equals(" ") &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            return board[0][2].equals("X") ? playerX : playerO;
        }

        return null;
    }

    @Override
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            System.out.println("| " + String.join(" | ", board[i]) + " |");
        }
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col].equals(" ");
    }

}

