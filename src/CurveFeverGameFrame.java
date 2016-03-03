
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Alireza Mahmoudi on 1/29/16.
 */
public class CurveFeverGameFrame extends JFrame {

    private CurveFeverGamePanel  gamePanel;
    private CurveFeverScorePanel scorePanel;
    private CurveFeverPlayer []  players;

    public CurveFeverGameFrame(String title, int numberOfInPlayers, final CurveFeverPlayer [] players, List<CurveFeverSpeedPower> powerups){
        super(title);
        this.players = players;
        this.setLayout(new FlowLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(
                dim.width/2 - CurveFever.defaultGameFrameWidth/2,
                dim.height/2 - CurveFever.defaultGameFrameHeigth/2,
                CurveFever.defaultGameFrameWidth,
                CurveFever.defaultGameFrameHeigth);
        this.getContentPane().setBackground(Color.black);
        this.setFocusable(true);
        this.setResizable(false);
        this.add(gamePanel = new CurveFeverGamePanel(powerups, players));
        this.add(scorePanel = new CurveFeverScorePanel(numberOfInPlayers, players));
        this.pack();
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                for(int i = 0;i < CurveFever.numberOfPlayers;i ++)
                    if(players[i] instanceof CurveFeverUserPlayer)
                       if(e.getKeyCode() == ((CurveFeverUserPlayer)players[i]).getLeftKeyCode())
                           players[i].snake.rotateLeft();
                        else if(e.getKeyCode() == ((CurveFeverUserPlayer)players[i]).getRightKeyCode())
                           players[i].snake.rotateRight();
                if(e.getKeyChar() == ' ')
                    CurveFeverGameController.isPaused ^= true;
                if(e.getKeyCode() ==  KeyEvent.VK_ESCAPE)
                    CurveFeverGameController.exit = true;
            }
            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < CurveFever.numberOfPlayers; i++)
                    if (players[i] instanceof CurveFeverUserPlayer)
                        if (e.getKeyCode() == ((CurveFeverUserPlayer) players[i]).getLeftKeyCode())
                            players[i].snake.setLeftFree();
                        else if (e.getKeyCode() == ((CurveFeverUserPlayer) players[i]).getRightKeyCode())
                            players[i].snake.setRightFree();
            }
        });
        this.setVisible(true);
        this.requestFocus();
    }

    public void update(BufferedImage gameImage){
        scorePanel.updateScores();
        gamePanel.updateGameImage(gameImage);
    }
}
