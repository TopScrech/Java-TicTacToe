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

        TicTacToeGame game = new TicTacToeGame();

        if (keuze == 1) {
            System.out.println("Speler X en Computer O");
        } else {
            System.out.println("Computer X en Computer O");
        }
    }
}
