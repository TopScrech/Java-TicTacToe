package nl.isy_games;

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

        AI ai = new AI();
        System.out.print(ai.name);
    }
}
