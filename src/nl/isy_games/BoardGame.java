package nl.isy_games;

public abstract class BoardGame {
    public abstract boolean makeMove(Player player, int pos);

    public abstract boolean isGameOver();

    public abstract Player getWinner();

    public abstract void printBoard();

//    public abstract void printBoard();
}
