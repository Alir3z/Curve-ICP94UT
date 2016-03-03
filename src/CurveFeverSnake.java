import java.util.Random;

/**
 * Created by mareal on 1/27/16.
 */
public class CurveFeverSnake {

    public static final double rotateConstant = .02;
    public static final double speedConstant = .4;

    private double speed = .4;
    private Random rnd;
    private double x, y, angle;
    private boolean isAlive;
    private int rotateSign;
    private boolean isLeft, isRight;

    CurveFeverSnake(Random rnd) {
        this.rnd = rnd;
        x = CurveFever.defaultGameFieldSize * (rnd.nextDouble() * .8 + .1);
        y = CurveFever.defaultGameFieldSize * (rnd.nextDouble() * .8 + .1);
        angle = rnd.nextDouble() * Math.PI;
        isAlive = true;
        rotateSign = 0;
        isLeft = isRight = false;
    }

    public void move() {
        rotate();
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
    }

    public void rotate() {

        if (isRight ^ isLeft)
            rotateSign = isLeft ? -1 : +1;
        else
            rotateSign = 0;
        angle += rotateSign * rotateConstant;
    }

    public void rotateLeft() {
        isLeft = true;
    }

    public void rotateRight() {
        isRight = true;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public void setLeftFree() {
        isLeft = false;
    }

    public void setRightFree() {
        isRight = false;
    }

    public void setFree() {
        setLeftFree();
        setRightFree();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void increaseSpeed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speed = 2 * speedConstant;
                System.out.println(speed);
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speed = speedConstant;
                System.out.println(speed);
            }
        }).start();
    }
}