package classes;

public class Player {
    String name;

    // protected makes the constructor accessible within the same package and by subclasses
    // Init example:
    // Player p1 = new Player("Wijmar");
    protected Player(String name) {
        this.name = name;
    }

}