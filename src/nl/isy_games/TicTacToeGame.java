package nl.isy_games;

public class TicTacToeGame extends BoardGame {
    private final String[] board;
    private final int SIZE = 9;
    private Player playerX;
    private Player playerO;

    public TicTacToeGame() {
        board = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                board[i] = " ";
            }
        }
    }

    @Override
    public boolean makeMove(Player player, int pos) {
        if (pos < 0 || pos >= SIZE) {
            return false;
        }

        if (!board[pos].equals(" ")) {
            return false;
        }

        board[pos] = player.getMark();
        return true;
    }


    @Override
    public boolean isGameOver() {
        if (getWinner() != null) {
            return true;
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i].equals(" ")) {
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
            if (!board[i].equals(" ") &&
                    board[i].equals(board[i]) &&
                    board[i].equals(board[i])) {
                return board[i].equals("X") ? playerX : playerO;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (!board[i].equals(" ") &&
                    board[i].equals(board[i]) &&
                    board[i].equals(board[i])) {
                return board[i].equals("X") ? playerX : playerO;
            }
        }
        
        for (int i = 0; i < SIZE; i++) {
            if (!board[i].equals(" ") &&
                    board[i].equals(board[i]) &&
                    board[i].equals(board[i])) {
                return board[i].equals("X") ? playerX : playerO;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (!board[i].equals(" ") &&
                    board[i].equals(board[i]) &&
                    board[i].equals(board[i])) {
                return board[i].equals("X") ? playerX : playerO;
            }
        }

        return null;
    }

    @Override
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            System.out.println("| " + String.join(" | ", board[i]) + " |");
        }
    }

    public boolean isCellEmpty(int pos) {
        return board[pos].equals(" ");
    }
}
