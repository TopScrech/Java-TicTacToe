package nl.isy_games;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welkom bij Gaming Library!");
        System.out.println("Kies spel:");
        System.out.println("1. TicTacToe");
        System.out.println("2. Nog niet beschikbaar");

        Scanner scanner = new Scanner(System.in);
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

        Player current = player1;

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