package classes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameSelector extends JFrame {

    private final GameClient client;
    private TicTacToeGame game;
    private Player me, opponent;
    private Player currentPlayer;
    private JFrame gameFrame;
    private JButton[][] buttons;

    public GameSelector(GameClient client) {
        this.client = client;

        setTitle("Game Selector");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1, 10, 10));

        JLabel label = new JLabel("Kies een spel:", SwingConstants.CENTER);
        add(label);

        try {
            List<String> games = client.getGameList();

            for (String gameName : games) {
                JButton button = new JButton(gameName);
                add(button);

                button.addActionListener(e -> {
                    if (gameName.equalsIgnoreCase("Tic-tac-toe")) {
                        try {
                            client.subscribe("Tic-tac-toe");
                            JOptionPane.showMessageDialog(this,
                                    "Ingeschreven voor " + gameName + "!\nWachten op tegenstander...");

                            new Thread(() -> {
                                try {
                                    client.listen(message -> SwingUtilities.invokeLater(() -> handleServerMessage(message)));
                                } catch (IOException ex) {
                                    SwingUtilities.invokeLater(() ->
                                            JOptionPane.showMessageDialog(this, "Fout bij luisteren naar server: " + ex.getMessage()));
                                }
                            }).start();

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Kan niet inschrijven: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Het spel '" + gameName + "' is nog niet beschikbaar.");
                    }
                });
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Kon geen gamelist ophalen: " + e.getMessage());
        }

        setVisible(true);
    }

    private void handleServerMessage(String message) {
        System.out.println("Server: " + message);

        if (message.startsWith("SVR GAME MATCH")) {
            String meName = message.split("PLAYERTOMOVE:")[1].split(",")[0].replace("\"", "").trim();
            String opponentName = message.split("OPPONENT:")[1].replace("}", "").replace("\"", "").trim();

            me = new Player(meName, "X"); 
            opponent = new Player(opponentName, "O");
            game = new TicTacToeGame();
            game.setPlayers(me, opponent);
            currentPlayer = me;

            setupBoard();
        }

        else if (message.startsWith("SVR GAME MOVE")) {
            String[] parts = message.replace("{", "").replace("}", "").split(",");
            String playerName = parts[0].split(":")[1].replace("\"", "").trim();
            int row = Integer.parseInt(parts[1].split(":")[1].trim());
            int col = Integer.parseInt(parts[2].split(":")[1].trim());

            Player player = playerName.equals(me.getName()) ? me : opponent;
            game.makeMove(player, row, col);
            buttons[row][col].setText(player.getMark());
            currentPlayer = (player == me) ? opponent : me;
        }

        else if (message.startsWith("SVR GAME WIN") ||
                message.startsWith("SVR GAME LOSS") ||
                message.startsWith("SVR GAME DRAW")) {
            JOptionPane.showMessageDialog(this, "Match einde: " + message);
            gameFrame.dispose();
        }
    }

    private void setupBoard() {
        gameFrame = new JFrame("TicTacToe: " + me.getName() + " vs " + opponent.getName());
        gameFrame.setSize(400, 400);
        gameFrame.setLayout(new GridLayout(3, 3));
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buttons = new JButton[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton btn = new JButton("");
                buttons[r][c] = btn;
                int row = r, col = c;
                btn.addActionListener(e -> {
                    if (currentPlayer == me && game.isCellEmpty(row, col)) {
                        try {
                            client.sendMove(row * 3 + col); 
                            game.makeMove(me, row, col);
                            btn.setText(me.getMark());
                            currentPlayer = opponent;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(gameFrame, "Fout bij verzenden van zet: " + ex.getMessage());
                        }
                    }
                });
                gameFrame.add(btn);
            }
        }

        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameClient client = new GameClient("127.0.0.1", 7789, "testplayer");
                client.login();
                new GameSelector(client);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Kan geen verbinding maken: " + e.getMessage());
            }
        });
    }
}
