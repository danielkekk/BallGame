import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class Ball {

    static final int BALL_WIDTH=40;
    static final int BALL_HEIGHT=40;

    private boolean active;
    private int xPos;
    private int yPos;
    private int xVector;
    private int yVector;
    private Color color;

    public Ball() {
        this.setBallDefault();
    }

    public int getXVector() {
        return this.xVector;
    }

    public int getYVector() {
        return this.yVector;
    }

    public int getXPos() {
        return this.xPos;
    }

    public int getYPos() {
        return this.yPos;
    }

    public boolean getActive() {
        return this.active;
    }

    public Color getColor() {
        return this.color;
    }

    public void setXPos(int value) {
        this.xPos = value;
    }

    public void setYPos(int value) {
        this.yPos = value;
    }

    public void setXVector(int value) {
        this.xVector = value;
    }

    public void setYVector(int value) {
        this.yVector = value;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void pushBall() {
        this.xPos += this.xVector;
    }

    public void pullBall() {
        this.yPos += this.yVector;
    }

    public void turnXVector() {
        this.xVector *= -1;
    }

    public void turnYVector() {
        this.yVector *= -1;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setBallDefault() {
        Random random = new Random();
        xPos = 10 + random.nextInt(Game.WIDTH-100);
        yPos = 10 + random.nextInt(Game.HEIGHT-100);
        xVector = 1 + random.nextInt(5);
        yVector = 1 + random.nextInt(5);
        active = false;
        color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }

    public boolean isCollided(Point point, Dimension dim) {
        
        boolean xOverlap = (this.xPos < point.getX() + dim.getWidth()) && (this.xPos > point.getX());

        boolean yOverlap = (this.yPos < point.getY() + dim.getHeight()) && (this.yPos > point.getY());

        return xOverlap && yOverlap;
    }
}

