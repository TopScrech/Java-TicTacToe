package nl.isy_games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class LoginGUI extends JFrame {

    private JTextField nameField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JLabel onlineCountLabel;

    private GameClient client;
    private boolean loggedIn = false;

    public LoginGUI() {
        setTitle("TicTacToe Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        nameField = new JTextField();
        loginButton = new JButton("Login");
        statusLabel = new JLabel("Status: Niet ingelogd");
        onlineCountLabel = new JLabel("Online spelers: 0");

        add(new JLabel("Voer je spelersnaam in:"));
        add(nameField);
        add(loginButton);
        add(statusLabel);
        add(onlineCountLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim().toLowerCase();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Voer een geldige naam in.");
                    return;
                }

                try {
                    // Maak verbinding met de server
                    client = new GameClient("127.0.0.1", 7789, playerName);
                    System.out.println("Sending: login " + playerName);
                    client.login(); // login versturen
                    loggedIn = true;
                    statusLabel.setText("Status: Ingelogd als " + playerName);

                    // Haal online spelers op
                    updateOnlineCount();

                    // Je kunt hier eventueel automatisch subscribe toevoegen
                    // client.subscribe("TicTacToe");

                } catch (IOException ex) {
                    loggedIn = false;
                    statusLabel.setText("Status: Login mislukt");
                    JOptionPane.showMessageDialog(LoginGUI.this, "Kan niet verbinden of login mislukt: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    private void updateOnlineCount() {
        if (!loggedIn) return;

        try {
            List<String> players = client.getPlayerList();
            onlineCountLabel.setText("Online spelers: " + players.size());
        } catch (IOException e) {
            onlineCountLabel.setText("Online spelers: ? (fout)");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
