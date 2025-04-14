import java.awt.Color;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    String type;
    int width;
    int length;
    int x, y;
    Color color;

    public Room(String type, int width, int length, int x, int y, Color color) {
        this.type = type;
        this.width = width;
        this.length = length;
        this.x = x;
        this.y = y;
        this.color = color;
    }
}