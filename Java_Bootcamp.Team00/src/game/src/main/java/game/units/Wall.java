package game.units;

public class Wall extends Unit{
    public Wall(char chr, String color) {
        super(chr, color);
        this.type = 2;
    }
}
