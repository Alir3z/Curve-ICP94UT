
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.*;
import java.util.List;

/**
 * Created by mareal on 1/27/16.
 */

public class CurveFeverGameController {


    private BufferedImage gameImage;
    private CurveFeverGameFrame gameFrame;
    private Random rnd;
    private int numberOfInPlayers;
    private CurveFeverPlayer[] players;
    private List<CurveFeverSpeedPower> powerups = new ArrayList<CurveFeverSpeedPower>();
    public static boolean isPaused = false, exit = false, doNotLetThreadMakePowerUp = false;

    CurveFeverGameController(CurveFeverPlayer[] players, final JFrame menuFrame) {
        rnd = new Random();
        numberOfInPlayers = 0;
        this.players = players;
        gameImage = new BufferedImage(CurveFever.defaultGameFieldSize, CurveFever.defaultGameFieldSize, BufferedImage.TYPE_INT_ARGB);
        for (CurveFeverPlayer player : players)
            if (player.isIn()) {
                numberOfInPlayers++;
                player.reset();
                if (player.isAI())
                    ((CurveFeverAIPlayer) player).setImage(gameImage);
            }
        gameFrame = new CurveFeverGameFrame(CurveFever.title, numberOfInPlayers, players, powerups);
        final Thread Game = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!makeNextRound()) ;
                sayCongratToTheWinner();
            }
        });
        Game.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit)
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                Game.stop();
                gameFrame.dispose();
                menuFrame.setVisible(true);
                menuFrame.setFocusable(true);
                menuFrame.requestFocus();
                exit = false;
            }
        }).start();
    }

    private void sayCongratToTheWinner() {
        int best = -1;
        CurveFeverPlayer winner = null;
        for (CurveFeverPlayer player : players)
            if (player.isIn() && player.getScore() > best) {
                winner = player;
                best = winner.getScore();
            }
        final JFrame cong = new JFrame();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int height = CurveFever.defaultGameFrameHeigth / 2, width = CurveFever.defaultGameFrameWidth / 2;
        cong.setBounds((int) dim.getWidth() / 2 - width / 2, (int) dim.getHeight() / 2 - height / 2, width, height);
        JPanel cPanel = new JPanel();
        cong.setLayout(new FlowLayout());
        cong.add(cPanel);
        cPanel.setPreferredSize(new Dimension(width, height));
        cPanel.setBackground(winner.getColor());
        JLabel cLabel = new JLabel("Congratulation " + winner.getName());
        cLabel.setPreferredSize(new Dimension(width / 2, height / 2));
        cLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 16));
        cPanel.add(cLabel);
        cong.pack();
        cong.setVisible(true);
        cong.setFocusable(true);
        cong.requestFocus();
        cong.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                cong.setVisible(false);
                cong.dispose();
                exit = true;
            }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

    }

    private boolean makeNextRound() {
        powerups.clear();
        for (CurveFeverPlayer player : players)
            if (player.isIn())
                player.snake = new CurveFeverSnake(rnd);

        for (int i = 0; i < gameImage.getWidth(); i++)
            for (int j = 0; j < gameImage.getHeight(); j++)
                gameImage.setRGB(i, j, Color.black.getRGB());
        for(int i = 0;i < gameImage.getWidth();i ++){
            gameImage.setRGB(i, 0, Color.red.getRGB());
            gameImage.setRGB(i, gameImage.getHeight() -1 , Color.red.getRGB());
        }
        for(int i = 0;i < gameImage.getHeight();i ++){
            gameImage.setRGB(0, i, Color.red.getRGB());
            gameImage.setRGB(gameImage.getWidth() -1, i, Color.red.getRGB());
        }
        boolean firstTimePause = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; ;i ++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i % 15 == 0 && !doNotLetThreadMakePowerUp && powerups.size() < 4) {
                        int b = rnd.nextInt(2);
                        CurveFeverSpeedPower sp ;
                        if(b != 0)
                            sp = CurveFeverSpeedPower.GREEN;
                        else
                            sp = CurveFeverSpeedPower.RED;
                        sp.x = (int) (CurveFever.defaultGameFieldSize * (rnd.nextDouble() * .8 + .1));
                        sp.y = (int) (CurveFever.defaultGameFieldSize * (rnd.nextDouble() * .8 + .1));
                        powerups.add(sp);
                    }
                }
            }
        }).start();
        while (goGoGo()) {
            if (firstTimePause) {
                goGoGo();
                goGoGo();
                goGoGo();
            }
            while (isPaused) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(4);
                gameFrame.update(gameImage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (firstTimePause) {
                isPaused = true;
                firstTimePause = false;
            }
        }

        int firstMax = -1, secondMax = -1;
        for (CurveFeverPlayer player : players)
            if (player.isIn())
                if (player.getScore() > firstMax) {
                    secondMax = firstMax;
                    firstMax = player.getScore();
                } else if (player.getScore() >= secondMax)
                    secondMax = player.getScore();
        return (firstMax - secondMax) > 1 && 5 * (numberOfInPlayers - 1) <= firstMax;
    }

    private boolean goGoGo() {
        int nOAP = 0; //number of alive players
        for (CurveFeverPlayer player : players)
            if (player.isIn() && player.snake.isAlive()) {
                if (player.isAI())
                    ((CurveFeverAIPlayer) player).whatToDo();
                checkPowerups(player);
                if (!paintSnake(player)) {
                    player.snake.kill();
                    giveScore();
                }
            }
        for (CurveFeverPlayer player : players)
            if (player.isIn() && player.snake.isAlive())
                nOAP++;
        return nOAP > 1;
    }

    private void giveScore() {
        for (int i = 0; i < CurveFever.numberOfPlayers; i++)
            if (players[i].isIn() && players[i].snake.isAlive())
                players[i].increaseScore();
    }

    private void checkPowerups(CurveFeverPlayer player) {
        CurveFeverSpeedPower tmp = null;
        for(CurveFeverSpeedPower power : powerups){
            int px = power.x, py = power.y;
            int x = (int) player.snake.getX(), y = (int) player.snake.getY();
            x = Math.abs(x - px);
            y = Math.abs(y - py);
            if(x + y <= 13){
                tmp = power;
                if(power == CurveFeverSpeedPower.GREEN)
                    player.snake.increaseSpeed();
                else
                    for(CurveFeverPlayer opponents : players)
                        if(opponents.isIn() && opponents.snake.isAlive() && opponents != player) {
                            opponents.snake.increaseSpeed();
                        }
            }
        }
        if(tmp != null)
            powerups.remove(tmp);
    }

    private boolean paintSnake(CurveFeverPlayer player) {
        double px = (int) player.snake.getX(), py = (int) player.snake.getY();
        player.snake.move();
        double x = (int) player.snake.getX(), y = (int) player.snake.getY(), r = 3;
        for (double i = x - r; i < x + r; i++)
            for (double j = y - r; j < y + r; j++)
                if (i > 0 && j > 0 && i < gameImage.getWidth(null) && j < gameImage.getWidth(null)) {
                    double X = (i - x) * (i - x), Y = (j - y) * (j - y);

                    if (X + Y <= r * r && gameImage.getRGB((int) i, (int) j) != Color.black.getRGB() &&
                            gameImage.getRGB((int) i, (int) j) != Color.white.getRGB() &&
                            gameImage.getRGB((int) i, (int) j) != player.getColor().getRGB())
                        return false;
                } else
                    return false;
        r += 2;
        double angle = player.snake.getAngle();
        int xx = (int) (x + Math.cos(angle) * r + .5), yy = (int) (y + Math.sin(angle) * r + .5);
        if (xx > 0 && yy > 0 && xx < gameImage.getWidth() && yy < gameImage.getHeight() && gameImage.getRGB(xx, yy) != Color.black.getRGB())
            return false;
        r -= 2;
        Graphics2D g2 = (Graphics2D) gameImage.createGraphics();
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(player.getColor());
        g2.fillOval((int) (px - r), (int) (py - r), (int) r, (int) r);
        //g2.fill(new Ellipse2D.Double(px - r, py - r, r, r));
        g2.setColor(Color.white);
        g2.fillOval((int) (x - r), (int) (y - r), (int) r, (int) r);
        //g2.fill(new Ellipse2D.Double(x - r, y - r, r, r));
        for (double i = px - r; i < px + r; i++)
            for (double j = py - r; j < py + r; j++) {
                double X = (i - px) * (i - px), Y = (j - py) * (j - py);
                if (X + Y <= r * r)
                    gameImage.setRGB((int) i, (int) j, player.getColor().getRGB());
            }
        for (double i = x - r; i < x + r; i++)
            for (double j = y - r; j < y + r; j++) {
                double X = (i - x) * (i - x), Y = (j - y) * (j - y);
                if (X + Y <= r * r)
                    gameImage.setRGB((int) i, (int) j, Color.white.getRGB());
            }
        return true;
    }
}