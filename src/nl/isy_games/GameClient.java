package classes;

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
                return;
            } else if (response.startsWith("ERR")) {
                throw new IOException("Login failed: " + response);
            }
        }
        throw new IOException("Login failed: geen antwoord van server");
    }

    public void subscribe(String gameType) throws IOException {
        send("subscribe " + gameType);
        String response = in.readLine();
        System.out.println("Server: " + response);
        if (!response.startsWith("OK")) {
            throw new IOException("Subscribe failed: " + response);
        }
    }


    public void sendMove(int move) {
        send("move " + move);
    }

    public void forfeit() {
        send("forfeit");
    }

    public void listen(ServerListener listener) throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println("Server: " + message);
            listener.onMessage(message); // callback naar GUI of andere logica
//            handleServerMessage(message);
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

    public List<String> getGameList() throws IOException {
        send("get gamelist");
        String response;

        while ((response = in.readLine()) != null) {
            System.out.println("Server response: " + response);

            if (response.startsWith("OK")) {
                continue;
            }

            if (response.startsWith("SVR GAMELIST")) {
                String list = response.substring("SVR GAMELIST".length()).trim();

                list = list.replace("[", "").replace("]", "").replace("\"", "");

                String[] games = list.split(",");
                List<String> result = new java.util.ArrayList<>();
                for (String g : games) {
                    result.add(g.trim());
                }
                return result;
            }
            if (response.startsWith("ERR")) {
                throw new IOException("Kon gamelist niet ophalen: " + response);
            }
        }
        throw new IOException("Geen antwoord van server bij gamelist");
    }


    private void send(String msg) {
        out.println(msg);
    }

    public void close() throws IOException {
        send("logout");
        socket.close();
    }

    public interface ServerListener {
        void onMessage(String message);
    }
}
