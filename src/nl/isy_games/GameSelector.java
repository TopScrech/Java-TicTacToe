package nl.isy_games;

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
    private JLabel statusLabel;
    private JLabel turnLabel;
    
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

                            if (statusLabel == null) {
                                statusLabel = new JLabel("Ingeschreven voor " + gameName + " - Wachten op tegenstander...",
                                        SwingConstants.CENTER);
                                add(statusLabel);
                                revalidate();
                                repaint();
                            }

                            new Thread(() -> {
                                try {
                                    client.listen(message -> SwingUtilities.invokeLater(() -> handleServerMessage(message)));
                                } catch (IOException ex) {
                                    SwingUtilities.invokeLater(() ->
                                            JOptionPane.showMessageDialog(this,
                                                    "Fout bij luisteren naar server: " + ex.getMessage()));
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

    private void updateTurnLabel() {
        if (currentPlayer == me) {
            turnLabel.setText("Jouw beurt (" + me.getMark() + ")");
        } else {
            turnLabel.setText("Beurt van " + opponent.getName() + " (" + opponent.getMark() + ")");
        }
    }


    private void handleServerMessage(String message) {
        System.out.println("Server: " + message);
        System.out.println("This is handleServerMessage!");

        if (message.startsWith("SVR GAME MATCH")) {

            String myName = client.getPlayerName();
            String playerToMove = extractValue(message, "PLAYERTOMOVE");
            String opponentName = extractValue(message, "OPPONENT");

            if (opponentName == null || opponentName.isEmpty() || opponentName.equalsIgnoreCase(myName)) {
                System.out.println("WARNING: Ignoring invalid match: opponent='" + opponentName + "'");
                return;
            }

            if (gameFrame != null) {
                System.out.println("INFO: Board already exists for this client, ignoring duplicate match message.");
                return;
            }

            if (statusLabel != null) {
                remove(statusLabel);
                statusLabel = null;
                revalidate();
                repaint();
            }

            if (playerToMove.equals(myName)) {
                me = new Player(myName, "X");
                opponent = new Player(opponentName, "O");
            } else {
                me = new Player(myName, "O");
                opponent = new Player(opponentName, "X");
            }
            currentPlayer = (playerToMove.equals(myName)) ? me : opponent;

            game = new TicTacToeGame();
            game.setPlayers(me, opponent);

            System.out.println("DEBUG: Opening JFrame for player '" + me.getName() + "' vs opponent '" + opponent.getName() + "'");
            setupBoard();
        }

        else if (message.startsWith("SVR GAME MOVE")) {
            if (game == null) return;

            String[] parts = message.replace("{", "").replace("}", "").split(",");
            String playerName = parts[0].split(":")[1].replace("\"", "").trim();
            int row = Integer.parseInt(parts[1].split(":")[1].trim());
            int col = Integer.parseInt(parts[2].split(":")[1].trim());

            Player player = playerName.equals(me.getName()) ? me : opponent;
            game.makeMove(player, row, col);
            buttons[row][col].setText(player.getMark());
            currentPlayer = (player == me) ? opponent : me;

            updateTurnLabel();
        }

        else if (message.startsWith("SVR GAME WIN") ||
                message.startsWith("SVR GAME LOSS") ||
                message.startsWith("SVR GAME DRAW")) {
            JOptionPane.showMessageDialog(this, "Match einde: " + message);
            if (gameFrame != null) {
                gameFrame.dispose();
                gameFrame = null;
            }
        }
    }

    private String extractValue(String msg, String key) {
        int start = msg.indexOf(key + ":");
        if (start == -1) return "";
        start = msg.indexOf("\"", start) + 1;
        int end = msg.indexOf("\"", start);
        return msg.substring(start, end);
    }

    private void setupBoard() {
        gameFrame = new JFrame("TicTacToe: " + me.getName() + " vs " + opponent.getName());
        gameFrame.setSize(400, 450); // Slightly taller to fit label
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Turn label at the top
        turnLabel = new JLabel("", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameFrame.add(turnLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
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
                            updateTurnLabel();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(gameFrame,
                                    "Fout bij verzenden van zet: " + ex.getMessage());
                        }
                    }
                });
                boardPanel.add(btn);
            }
        }

        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.setLocationRelativeTo(null);

        updateTurnLabel();
        gameFrame.setVisible(true);
    }

}
