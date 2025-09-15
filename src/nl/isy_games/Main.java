package nl.isy_games;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    private static TicTacToeGame game;
    private static Player player1, player2, current;
    private static final JButton[][] buttons = new JButton[3][3];
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showMainMenu);
    }

    private static void showMainMenu() {
        JFrame mainMenu = new JFrame("Gaming Library");
        mainMenu.setSize(400, 200);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Kies een spel:", SwingConstants.CENTER);
        JButton ticTacToeButton = new JButton("1. TicTacToe");
        JButton notAvailableButton = new JButton("2. Nog niet beschikbaar");

        mainMenu.add(label);
        mainMenu.add(ticTacToeButton);
        mainMenu.add(notAvailableButton);

        mainMenu.setLocationRelativeTo(null); // Centers the window
        mainMenu.setVisible(true);

        ticTacToeButton.addActionListener(ignored -> {
            mainMenu.dispose();
            showModeMenu();
        });

        notAvailableButton.addActionListener(ignored ->
                JOptionPane.showMessageDialog(mainMenu, "Dit spel is nog niet beschikbaar.")
        );
    }

    private static void showModeMenu() {
        JFrame modeMenu = new JFrame("TicTacToe - Kies modus");
        modeMenu.setSize(400, 200);
        modeMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modeMenu.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Kies spelmodus:", SwingConstants.CENTER);
        JButton pVcButton = new JButton("1. Player vs CPU");
        JButton cVcButton = new JButton("2. CPU vs CPU");

        modeMenu.add(label);
        modeMenu.add(pVcButton);
        modeMenu.add(cVcButton);

        modeMenu.setLocationRelativeTo(null); // Centers the window
        modeMenu.setVisible(true);

        pVcButton.addActionListener(ignored -> {
            modeMenu.dispose();
            startPlayerVsCpu();
        });

        cVcButton.addActionListener(ignored -> {
            modeMenu.dispose();
            startCpuVsCpu();
        });
    }

    private static void startPlayerVsCpu() {
        Random random = new Random();
        game = new TicTacToeGame();
        player1 = new Player("Mens", "X");
        player2 = new AI("Computer", "O");
        game.setPlayers(player1, player2);

        // Kop of munt
        String[] options = {"Kop", "Munt"};

        int userChoice = JOptionPane.showOptionDialog(
                null,
                "Kies Kop of Munt",
                "Kop of Munt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        int flip = random.nextInt(2);

        if (userChoice == flip) {
            JOptionPane.showMessageDialog(null, "Je hebt gewonnen! Jij begint.");
            current = player1;
        } else {
            JOptionPane.showMessageDialog(null, "Computer mag beginnen!");
            current = player2;
        }

        setupBoard();
    }

    private static void startCpuVsCpu() {
        Random random = new Random();
        game = new TicTacToeGame();
        player1 = new AI("Computer 1", "X");
        player2 = new AI("Computer 2", "O");
        game.setPlayers(player1, player2);

        current = random.nextBoolean() ? player1 : player2;
        JOptionPane.showMessageDialog(null, current.getName() + " mag beginnen!");

        setupBoard();

        new Thread(() -> {
            try {
                while (!game.isGameOver()) {
                    Thread.sleep(800);
                    int[] zet = ((AI) current).chooseMove(game);
                    game.makeMove(current, zet[0], zet[1]);
                    buttons[zet[0]][zet[1]].setText(current.getMark());
                    current = (current == player1) ? player2 : player1;
                }
                showWinner();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private static void setupBoard() {
        frame = new JFrame("TicTacToe");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton("");
                buttons[row][col] = button;
                int r = row, c = col;

                button.addActionListener(ignored -> {
                    if (current != null) {
                        handleMove(r, c);
                    }
                });

                frame.add(button);
            }
        }

        frame.setLocationRelativeTo(null); // Centers the window
        frame.setVisible(true);

        if (current instanceof AI) {
            cpuMove();
        }
    }

    private static void handleMove(int row, int col) {
        if (game.makeMove(current, row, col)) {
            buttons[row][col].setText(current.getMark());
            current = (current == player1) ? player2 : player1;

            if (game.isGameOver()) {
                showWinner();
            } else if (current instanceof AI) {
                cpuMove();
            }
        }
    }

    private static void cpuMove() {
        SwingUtilities.invokeLater(() -> {
            int[] zet = ((AI) current).chooseMove(game);
            if (game.makeMove(current, zet[0], zet[1])) {
                buttons[zet[0]][zet[1]].setText(current.getMark());
                current = (current == player1) ? player2 : player1;

                if (game.isGameOver()) {
                    showWinner();
                }
            }
        });
    }

    private static void showWinner() {
        Player winner = game.getWinner();
        String message = (winner != null) ? "Winnaar: " + winner.getName() : "Gelijkspel!";
        JOptionPane.showMessageDialog(frame, message);
    }
}
