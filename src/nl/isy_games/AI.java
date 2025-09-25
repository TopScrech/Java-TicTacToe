package nl.isy_games;

import java.util.Random;

public class AI extends Player {
    private final Random random;

    public AI(String name, String mark) {
        super(name, mark);
        random = new Random();
    }

    public int[] chooseMove(BoardGame game) {
        int pos;
        int[] move = new int[2];
        boolean valid = false;

        while (!valid) {
            pos = random.nextInt(9);

            if (((TicTacToeGame) game).isCellEmpty(pos)) {
                move[0] = pos;
                valid = true;
            }
        }

        return move;
    }
}
