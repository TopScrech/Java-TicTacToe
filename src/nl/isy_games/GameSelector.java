package classes;

import javax.swing.*;
import java.awt.*;

public class GameSelector extends JFrame {

    private GameClient client;

    public GameSelector(GameClient client) {
        this.client = client;

        setTitle("Kies een spel");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Kies een spel:", SwingConstants.CENTER);
        JButton ticTacToeButton = new JButton("1. TicTacToe");
        JButton notAvailableButton = new JButton("2. Nog niet beschikbaar");

        add(label);
        add(ticTacToeButton);
        add(notAvailableButton);

        setLocationRelativeTo(null);
        setVisible(true);

        ticTacToeButton.addActionListener(e -> {
            try {
                client.subscribe("TicTacToe");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        notAvailableButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Dit spel is nog niet beschikbaar.")
        );
    }
}
