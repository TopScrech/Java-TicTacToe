package nl.isy_games;

import java.util.Random;

public class AI extends Player {
    private final Random random;

    public AI(String name, String mark) {
        super(name, mark);
        random = new Random();
    }

    public int[] chooseMove(BoardGame game) {
        int row, col;
        int[] move = new int[2];
        boolean valid = false;

        while (!valid) {
            row = random.nextInt(3);
            col = random.nextInt(3);

            if (((TicTacToeGame) game).isCellEmpty(row, col)) {
                move[0] = row;
                move[1] = col;
                valid = true;
            }
        }

        return move;
    }
}
