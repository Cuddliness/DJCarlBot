package care.cuddliness.djcarl.utils;

public enum EmbedColor {
    PRIMARY("#8b6f96"), SECONDARY("#82A0D8"), WARNING("#ffbd86"), ERROR("#ff7472"), SUCCESS("#acbcac");

    private String color;

    EmbedColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
