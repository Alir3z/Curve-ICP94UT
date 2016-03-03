import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.awt.image.BufferedImage;
/**
 * Created by mareal on 1/29/16.
 */
public class CurveFeverGamePanel extends JPanel {

    BufferedImage gameImage;
    List<CurveFeverSpeedPower> powerups;
    Image redPup, greenPup;
    private CurveFeverPlayer[] players ;

    public CurveFeverGamePanel(List<CurveFeverSpeedPower> powerups, CurveFeverPlayer [] players) {
        this.players = players;
        this.powerups = powerups;
        this.setPreferredSize(new Dimension(CurveFever.defaultGameFieldSize, CurveFever.defaultGameFieldSize));
        try {
            redPup = ImageIO.read(new File("icons/red.png"));
            greenPup = ImageIO.read(new File("icons/green.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateGameImage(BufferedImage gameImage) {
        this.gameImage = gameImage;
        this.repaint();
    }

    public void paintComponent(Graphics t) {
        super.paintComponent(t);
        Graphics2D g = (Graphics2D) t;
        g.drawImage(gameImage, 0, 0, null);
        /*
        for(CurveFeverPlayer player : players)
            if(player.isIn() && player.snake.isAlive()) {
                int x = (int) player.snake.getX(), y = (int) player.snake.getY();
                g.draw(new Ellipse2D.Double(x, y, 2, 2));
            }
        */
        CurveFeverGameController.doNotLetThreadMakePowerUp = true;
        try {
            for (CurveFeverSpeedPower pu : powerups)
                if (pu == CurveFeverSpeedPower.GREEN)
                    g.drawImage(greenPup, pu.x - greenPup.getWidth(null) / 2, pu.y - greenPup.getHeight(null) / 2, null);
                else
                    g.drawImage(redPup, pu.x - redPup.getWidth(null) / 2, pu.y - redPup.getHeight(null) / 2, null);
        }catch (ConcurrentModificationException ex){
            powerups.clear();
        }
        CurveFeverGameController.doNotLetThreadMakePowerUp = false;
    }
}