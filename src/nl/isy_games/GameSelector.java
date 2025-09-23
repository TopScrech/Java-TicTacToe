package classes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameSelector extends JFrame {

    private final GameClient client;

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

            for (String game : games) {
                JButton button = new JButton(game);
                add(button);

                button.addActionListener(e -> {
                    if (game.equalsIgnoreCase("Tic-tac-toe")) {
                        try {
                            client.subscribe("Tic-tac-toe");
                            JOptionPane.showMessageDialog(this,
                                    "Ingeschreven voor " + game + "!\nWachten op tegenstander...");
                            // Start luisteren naar server events
                            new Thread(() -> {
                                try {
                                    client.listen(message -> {
                                        // Hier verwerk je alle serverberichten
                                        System.out.println("Server bericht: " + message);

                                        // Voorbeeld: popup bij match einde
                                        if (message.contains("SVR GAME WIN") || message.contains("SVR GAME LOSS") || message.contains("SVR GAME DRAW")) {
                                            SwingUtilities.invokeLater(() ->
                                                    JOptionPane.showMessageDialog(this, "Match einde: " + message)
                                            );
                                        }
                                    });
                                } catch (IOException ex) {
                                    SwingUtilities.invokeLater(() ->
                                            JOptionPane.showMessageDialog(this, "Fout bij luisteren naar server: " + ex.getMessage())
                                    );
                                }
                            }).start();


                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Kan niet inschrijven: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Het spel '" + game + "' is nog niet beschikbaar.");
                    }
                });
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Kon geen gamelist ophalen: " + e.getMessage());
        }

        setVisible(true);
    }

    // Test-startpunt
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameClient client = new GameClient("127.0.0.1", 7789, "testplayer");
                client.login();
                new GameSelector(client);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Kan geen verbinding maken: " + e.getMessage());
            }
        });
    }
}
