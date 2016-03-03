import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by mareal on 1/26/16.
 */
public class CurveFeverUserPlayer extends CurveFeverPlayer {

    private int LEFT_KEY, RIGHT_KEY;
    public boolean waitForLeft, waitForRight, isKeysSet;

    public CurveFeverUserPlayer(CurveFeverPlayer player) {
        super(player);
        this.isAI = false;
        this.isKeysSet = false;
    }

    public int getLeftKeyCode(){
        return LEFT_KEY;
    }

    public int getRightKeyCode(){
        return RIGHT_KEY;
    }
    public void setFixLeftButton(){
        leftKeyButton.setIcon(null);
        waitForLeft = false;
        isKeysSet = false;
    }

    public void setFixRightButton(){
        rightKeyButton.setIcon(null);
        waitForRight = false;
        isKeysSet = false;
    }
    public void setLeftButtonWaitForKey(){
        leftKeyButton.setIcon(new ImageIcon("icons/?.png"));
        waitForLeft = true;
    }

    public void setRightButtonWaitForKey(){
        rightKeyButton.setIcon(new ImageIcon("icons/?.png"));
        waitForRight = true;
    }

    public void setRightKey(KeyEvent e){
        RIGHT_KEY = e.getKeyCode();
        isKeysSet = true;
        waitForRight = false;
        rightKeyButton.setIcon(new ImageIcon("icons/" + KeyEvent.getKeyText(e.getKeyCode()) + ".png"));
    }

    public void setLeftKey(KeyEvent e){
        LEFT_KEY = e.getKeyCode();
        waitForLeft = false;
        leftKeyButton.setIcon(new ImageIcon("icons/" + KeyEvent.getKeyText(e.getKeyCode()) + ".png"));
    }
}