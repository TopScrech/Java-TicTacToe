package nl.isy_games;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Welkom bij Gaming Library!");
        System.out.println("Kies spel:");
        System.out.println("1. TicTacToe");
        System.out.println("2. Nog niet beschikbaar");
        int keuzeSpel = scanner.nextInt();

        BoardGame game;
        if (keuzeSpel == 1) {
            game = new TicTacToeGame();
        } else {
            System.out.println("Dit spel is nog niet beschikbaar.");
            return;
        }

        System.out.println("Welkom bij TicTacToe!");
        System.out.println("Kies modus:");
        System.out.println("1. Mens vs Computer");
        System.out.println("2. Computer vs Computer");
        int keuze;

        if (scanner.hasNextInt()) {
            keuze = scanner.nextInt();
        } else {
            System.out.println("Ongeldige invoer, standaard modus 1 gekozen");
            keuze = 1;
            scanner.next(); // Continue
        }

        Player player1, player2;

        if (keuze == 1) {
            player1 = new Player("X");
            player2 = new AI();
        } else {
            player1 = new AI();
            player2 = new AI();
        }

        Player current;

        if (keuze == 1) {
            System.out.println("Kop of Munt! Kies: 0 = Kop, 1 = Munt");
            int userChoice = scanner.nextInt();
            int flip = random.nextInt(2); // 0 of 1
            if (userChoice == flip) {
                System.out.println("Je mag beginnen!");
                current = player1;
            } else {
                System.out.println("Computer begint!");
                current = player2;
            }
        } else {
            // Voor twee computers, willekeurige start
            current = random.nextBoolean() ? player1 : player2;
            System.out.println(current.getName() + " begint!");
        }


        while (!game.isGameOver()) {
            game.printBoard();
            System.out.println("Beurt: " + current.getName());
            boolean moveDone = false;

            while (!moveDone) {
                if (current instanceof AI) {
                    int[] zet = ((AI) current).chooseMove(game);
                    game.makeMove(current, zet[0], zet[1]); // zet uitvoeren
                    System.out.println("Computer zet op: " + zet[0] + "," + zet[1]);
                    moveDone = true;

                    if (keuze == 2) {
                        try {
                            Thread.sleep(1000); // 1000 ms = 1 seconde
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    System.out.println("Voer rij (0-2) in:");
                    int row = scanner.nextInt();
                    System.out.println("Voer kolom (0-2) in:");
                    int col = scanner.nextInt();
                    moveDone = game.makeMove(current, row, col);
                    if (!moveDone) {
                        System.out.println("Ongeldige zet, probeer opnieuw!");
                    }
                }
            }
            current = (current == player1) ? player2 : player1;
        }

        game.printBoard();
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("Winnaar: " + winner);
        } else {
            System.out.println("Gelijkspel!");
        }

        scanner.close();
    }
}
