import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CurveFeverMenu {

    private static int numberOfPlayers = CurveFever.numberOfPlayers;
    private Dimension compDim = new Dimension(CurveFever.defaultMenuWidth / 4, CurveFever.defaultGameFrameHeigth / (numberOfPlayers + 1));
    private JFrame frame;
    private CurveFeverPlayer[] players = new CurveFeverPlayer[numberOfPlayers];
    private int width;
    private int height;

    public CurveFeverMenu(){
        this.width = CurveFever.defaultMenuWidth;
        this.height = CurveFever.defaultMenuHeigth;

        frame = new JFrame(CurveFever.title);
        frame.setFocusable(true);
        frame.setLayout(new GridLayout(numberOfPlayers + 1, 4));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dim.width/2 - width/2, dim.height/2 - height/2, width, height);

        for(int i = 0;i < numberOfPlayers;i ++)
            players[i] = new CurveFeverPlayer(CurveFeverPlayer.playersName[i], CurveFeverPlayer.playersColor[i]);
        {
            frame.add(getTextLabel("", Color.black));
            frame.add(getTextLabel("Left Key", Color.white));
            frame.add(getTextLabel("Right Key", Color.white));
            frame.add(getTextLabel("AI or User", Color.white));
        }

        for(int i = 0;i < numberOfPlayers;i ++) {
            frame.add(players[i].firstColumnLabel = getTextLabel(players[i].getName(), players[i].getColor()));
            frame.add(players[i].leftKeyButton = new JButton());
            players[i].leftKeyButton.setPreferredSize(compDim);
            players[i].leftKeyButton.setBackground(Color.black);
            frame.add(players[i].rightKeyButton = new JButton());
            players[i].rightKeyButton.setPreferredSize(compDim);
            players[i].rightKeyButton.setBackground(Color.black);
            frame.add(players[i].userOrAIButton = new JButton());
            players[i].userOrAIButton.setPreferredSize(compDim);
            players[i].userOrAIButton.setBackground(Color.black);

        }
        for(int i = 0;i < numberOfPlayers;i ++) {
            final int curI = i;
            players[curI].userOrAIButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    players[curI] = players[curI].rotateAIButton();
                    frame.requestFocus();
                }
            });

            players[curI].leftKeyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final CurveFeverPlayer cur = players[curI];
                    if(cur.isUser()){
                        final CurveFeverUserPlayer curUser = (CurveFeverUserPlayer)cur ;
                        curUser.setFixRightButton();
                        curUser.setFixLeftButton();
                        curUser.setLeftButtonWaitForKey();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                if(!curUser.isKeysSet) {
                                    curUser.setFixLeftButton();
                                    frame.requestFocus();
                                }
                            }
                        }).start();
                    }
                }
            });
            players[curI].leftKeyButton.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) { }

                @Override
                public void keyPressed(KeyEvent e) {
                    final CurveFeverPlayer cur = players[curI];
                    if(!cur.isUser())
                        return;
                    final CurveFeverUserPlayer curUser = (CurveFeverUserPlayer)cur ;
                    if(curUser.waitForLeft){
                        curUser.setLeftKey(e);
                        curUser.setRightButtonWaitForKey();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1200);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                if(!curUser.isKeysSet) {
                                    curUser.setFixRightButton();
                                    frame.requestFocus();
                                }
                            }
                        }).start();
                    }else if(curUser.waitForRight) {
                        curUser.setRightKey(e);
                        frame.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) { }
            });
        }
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                int inPlayers = 0;
                for(int i = 0;i < numberOfPlayers;i ++)
                    if(players[i] instanceof CurveFeverUserPlayer && ((CurveFeverUserPlayer)(players[i])).isKeysSet)
                        inPlayers ++;
                    else if(players[i].isIn())
                        inPlayers ++;
                if(e.getKeyChar() == ' ' && inPlayers > 1){
                    frame.setVisible(false);
                    new CurveFeverGameController(players, frame);
                    //frame.setVisible(true);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { }
        });
    }
    public JLabel getTextLabel(String s, Color foreground){
        JLabel label = new JLabel(s);
        label.setPreferredSize(compDim);
        label.setForeground(foreground);
        label.setBackground(Color.black);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}
