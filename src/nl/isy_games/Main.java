package nl.isy_games;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Scanner scanner = new Scanner(System.in);

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

        BoardGame game = new TicTacToeGame();
        Player player1, player2;

        if (keuze == 1) {
            player1 = new Player("X");
            player2 = new AI("O");
        } else {
            player1 = new AI("X");
            player2 = new AI("O");
        }
    }

