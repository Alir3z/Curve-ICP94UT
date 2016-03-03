import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by mareal on 1/27/16.
 */
public class CurveFeverAIPlayer extends CurveFeverPlayer {

    private BufferedImage img;
    private Random rnd;
    public CurveFeverAIPlayer(CurveFeverPlayer player) {
        super(player);
        rnd = new Random();
    }

    public void setImage(BufferedImage img) {
        this.img = img;
    }

    public void whatToDo() {

        this.snake.setFree();

        double angle = this.snake.getAngle();

        if (immediate(angle - Math.PI / 3)) {
            this.snake.rotateRight();
            return;
        } else if (immediate(angle + Math.PI / 3)) {
            this.snake.rotateLeft();
            return;
        }
        int length = 15;
        int leftCount = countSectorSpace(angle - Math.PI / 2, angle - Math.PI / 6, length);
        int rightCount = countSectorSpace(angle + Math.PI / 6, angle + Math.PI / 2, length);
        int forward = countSectorSpace(angle - Math.PI / 6, angle + Math.PI / 6, length);
        if(forward == leftCount && leftCount == rightCount){
            int cur = rnd.nextInt(5);
            if(cur == 0)
                this.snake.setFree();
            else if(cur == 2 || cur == 1)
                this.snake.rotateLeft();
            else
                this.snake.rotateRight();
        }
        else if (forward >= leftCount && forward >= rightCount) {
            //System.out.println(" Free");
        } else if (leftCount >= forward && leftCount >= rightCount) {
            this.snake.rotateLeft();
            //System.out.println(" Left");
        } else {
            this.snake.rotateRight();
            //System.out.println(" Right");
        }
    }

    private int countSectorSpace(double lAngle, double rAngle, int length) {
        double x = this.snake.getX(), y = this.snake.getY();
        int ret = 0;
        for (double angle = lAngle; angle < rAngle; angle += .04)
            ret += getPathLenght(x, y, angle, length);
        return ret;
    }

    private int getPathLenght(double x, double y, double angle, int length) {
        int ret = 0;
        int size = CurveFever.defaultGameFieldSize;
        x += Math.cos(angle) * 3;
        y += Math.sin(angle) * 3;
        for (int i = 0; i < length; i++) {
            x += 2 * Math.cos(angle);
            y += 2 * Math.sin(angle);

            if (x < 0 || y < 0 || x > size || y > size || img.getRGB((int) x, (int) y) != Color.black.getRGB())
                return ret;
            else
                ret++;
        }
        return ret;
    }
    private boolean immediate(double angle) {
        double x = this.snake.getX(), y = this.snake.getY();
        int size = CurveFever.defaultGameFieldSize;
        x += 4.5 * Math.cos(angle);
    y += 4.5 * Math.sin(angle);
        return x < 0 || y < 0 || x >= size || y >= size || img.getRGB((int) x, (int) y) != Color.black.getRGB();
    }

}