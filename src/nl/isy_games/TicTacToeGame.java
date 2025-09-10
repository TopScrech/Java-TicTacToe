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

    @Override
    public boolean makeMove(Player player, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (!board[row][col].equals(" ")) {
            return false;
        }
        board[row][col] = player.name.substring(0, 1);
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


    @Override
    public Player getWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (!board[i][0].equals(" ") && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return new Player(board[i][0]); // X of O
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (!board[0][i].equals(" ") && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return new Player(board[0][i]);
            }
        }

        if (!board[0][0].equals(" ") && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return new Player(board[0][0]);
        }
        if (!board[0][2].equals(" ") && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return new Player(board[0][2]);
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
