package game.units;

import com.diogonunes.jcolor.Attribute;

public abstract class Unit {
    protected char chr;
    protected Attribute color;
    protected int type;

    public Unit(char chr, String color) {
        this.chr = chr;

        switch (color) {
            case "BLACK":
                this.color = Attribute.BLACK_BACK();
                break;
            case "RED":
                this.color = Attribute.RED_BACK();
                break;
            case "GREEN":
                this.color = Attribute.GREEN_BACK();
                break;
            case "YELLOW":
                this.color = Attribute.YELLOW_BACK();
                break;
            case "BLUE":
                this.color = Attribute.BLUE_BACK();
                break;
            case "MAGENTA":
                this.color = Attribute.MAGENTA_BACK();
                break;
            case "CYAN":
                this.color = Attribute.CYAN_BACK();
                break;
            case "WHITE":
                this.color = Attribute.WHITE_BACK();
                break;
            default:
                this.color = Attribute.NONE();
                break;
        }
    }

    public void setChr(char chr) { this.chr = chr; }

    public char getChr() { return chr; }

    public String getChrToString() { return String.valueOf(chr); }

    public void setColor(Attribute color) { this.color = color; }

    public Attribute getColor() { return color; }

    public int getType() { return this.type; }
}
