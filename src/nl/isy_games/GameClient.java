package nl.isy_games;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;

    public GameClient(String host, int port, String playerName) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.playerName = playerName;
    }

    public void login() throws IOException {
        send("login " + playerName);

        String response;
        while ((response = in.readLine()) != null) {
            System.out.println("Server response: " + response);
            if (response.startsWith("OK")) {
                System.out.println("Login geslaagd!");
                return;
            } else if (response.startsWith("ERR")) {
                throw new IOException("Login failed: " + response);
            }
        }

        throw new IOException("Login failed: geen antwoord van server");
    }



    public void subscribe(String gameType) {
        send("subscribe " + gameType);
    }

    public void sendMove(int move) {
        send("move " + move);
    }

    public void forfeit() {
        send("forfeit");
    }

    public void listen() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println("Server: " + message);
            handleServerMessage(message);
        }
    }

    private void handleServerMessage(String message) {
        if (message.contains("SVR GAME YOURTURN")) {
            int zet = getNextMove();
            sendMove(zet);
        } else if (message.contains("SVR GAME MOVE")) {
        } else if (message.contains("SVR GAME WIN") || message.contains("SVR GAME LOSS") || message.contains("SVR GAME DRAW")) {
            System.out.println("Match einde: " + message);
        }
    }

    private int getNextMove() {
        return (int)(Math.random() * 9);
    }

    private void send(String msg) {
        out.println(msg);
    }

    public void close() throws IOException {
        send("logout");
        socket.close();
    }

    public static void main(String[] args) {
        try {
            GameClient client = new GameClient("127.0.0.1", 7789, "jouwnaam");
            client.login();
            client.subscribe("TicTacToe");
            client.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPlayerList() throws IOException {
        send("get playerlist");
        String response = in.readLine(); // OK
        response = in.readLine();         // SVR PLAYERLIST ["player1","player2",...]

        List<String> players = new ArrayList<>();
        if (response != null && response.startsWith("SVR PLAYERLIST")) {
            int start = response.indexOf('[');
            int end = response.indexOf(']');
            if (start >= 0 && end > start) {
                String list = response.substring(start + 1, end);
                String[] names = list.replaceAll("\"", "").split(",");
                for (String name : names) {
                    if (!name.trim().isEmpty()) players.add(name.trim());
                }
            }
        }
        return players;
    }



}
