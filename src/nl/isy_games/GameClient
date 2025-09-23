package classes;

import java.io.*;
import java.net.Socket;

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
        String response = in.readLine();
        System.out.println("Server: " + response);
        if (!response.startsWith("OK")) {
            throw new IOException("Login failed: " + response);
        }
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
}
