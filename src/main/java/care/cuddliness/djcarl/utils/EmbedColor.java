package care.cuddliness.djcarl.utils;

public enum EmbedColor {
    PRIMARY("#BEADFA"), SECONDARY("#82A0D8"), WARNING("#fcc44c"), ERROR("#f74040"), SUCCESS("#6df76d");

    private String color;

    EmbedColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
