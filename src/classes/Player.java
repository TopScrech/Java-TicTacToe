package classes;

public class Player {
    String name;

    // protected makes the constructor accessible within the same package and by subclasses
    protected Player(String name) {
        this.name = name;
    }
}