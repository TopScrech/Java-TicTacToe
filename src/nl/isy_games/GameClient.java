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
            if (response.startsWith("OK")) return;
            if (response.startsWith("ERR")) throw new IOException("Login failed: " + response);
        }
        throw new IOException("Login failed: geen antwoord van server");
    }

    public void subscribe(String gameType) throws IOException {
        send("subscribe " + gameType);
        String response;
        while ((response = in.readLine()) != null) {
            System.out.println("Server: " + response);
            if (response.startsWith("OK") || response.startsWith("SVR GAME MATCH")) {
                return;
            } else if (response.startsWith("ERR")) {
                throw new IOException("Subscribe failed: " + response);
            }
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
            listener.onMessage(message);
        }
    }

    public List<String> getPlayerList() throws IOException {
        send("get playerlist");
        String response = in.readLine(); // OK
        response = in.readLine();        // SVR PLAYERLIST [...]
        List<String> players = new ArrayList<>();
        if (response != null && response.startsWith("SVR PLAYERLIST")) {
            int start = response.indexOf('[');
            int end = response.indexOf(']');
            if (start >= 0 && end > start) {
                String list = response.substring(start + 1, end);
                for (String name : list.replace("\"", "").split(",")) {
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
            if (response.startsWith("SVR GAMELIST")) {
                String list = response.substring("SVR GAMELIST".length()).replace("[", "").replace("]", "").replace("\"", "").trim();
                List<String> result = new ArrayList<>();
                for (String g : list.split(",")) result.add(g.trim());
                return result;
            }
            if (response.startsWith("ERR")) throw new IOException("Kon gamelist niet ophalen: " + response);
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
