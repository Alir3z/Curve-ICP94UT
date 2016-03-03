import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by mareal on 1/26/16.
 */
public class CurveFeverPlayer {
    public static final String[] playersName =
            {"Blue", "Red", "Green", "Magenta", "Gray", "Yellow"};
    public static final Color[] playersColor =
            {Color.blue, Color.red, Color.green, Color.magenta, Color.gray, Color.yellow};

    protected String  name;
    protected Color   color;
    protected boolean isAI;
    protected boolean isIn;
    protected int userState;
    private int score;

    public CurveFeverSnake snake;

    public JButton userOrAIButton;
    public JButton leftKeyButton;
    public JButton rightKeyButton;
    public JLabel  firstColumnLabel;

    public CurveFeverPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
        this.isIn = this.isAI = false;
        this.userState = 1;
    }
    public CurveFeverPlayer(CurveFeverPlayer player) {
        this(player.getName(), player.getColor());

        this.leftKeyButton = player.leftKeyButton;
        this.rightKeyButton = player.rightKeyButton;
        this.userOrAIButton = player.userOrAIButton;
        this.firstColumnLabel = player.firstColumnLabel;
        this.userState = player.userState;
        this.isAI = player.isAI;
        this.isIn = player.isIn;
    }

    public String getName() {
        return this.name;
    }
    public Color getColor() {
        return this.color;
    }

    public void setLastIcon() {
        if(!isIn())
            this.userOrAIButton.setIcon(null);
        else if(isAI)
            this.userOrAIButton.setIcon(new ImageIcon("icons/AI.jpg"));
        else
            this.userOrAIButton.setIcon(new ImageIcon("icons/User.jpg"));
    }

    public CurveFeverPlayer rotateAIButton() {
        if(this.isUser()){
            ((CurveFeverUserPlayer)(this)).setFixRightButton();
            ((CurveFeverUserPlayer)(this)).setFixLeftButton();
        }
        userState = (userState + 1) % 3;
        isIn = (userState & 1) == 0;
        isAI = userState == 2;
        setLastIcon();
        if(!this.isIn)
            return new CurveFeverPlayer(this);
        else if(this.isAI)
            return new CurveFeverAIPlayer(this);
        else
            return new CurveFeverUserPlayer(this);
    }

    public boolean isAI() {
        return this instanceof CurveFeverAIPlayer;
    }

    public boolean isUser(){
        return this instanceof CurveFeverUserPlayer;
    }

    public boolean isIn(){
        return this.isIn;
    }

    public void reset(){
        this.score = 0;
    }

    public void makeNewSnake(Random rnd){
        this.snake = new CurveFeverSnake(rnd);
    }

    public int getScore(){
        return this.score;
    }

    public void increaseScore(){
        score ++;
    }
}