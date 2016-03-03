import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by mareal on 1/29/16.
 */
public class CurveFeverScorePanel extends JPanel {

    private CurveFeverPlayer[] players;
    private final int numberOfInPlayers;
    private final int fontSize = 25;
    private JLabel[] scoreLabels;
    private CurveFeverPlayer[] inPlayers;
    private Dimension compDim = new Dimension(CurveFever.defaultGameFrameWidth - CurveFever.defaultGameFieldSize, 60);

    public CurveFeverScorePanel(int numberOfInPlayers, CurveFeverPlayer[] players) {
        this.numberOfInPlayers = numberOfInPlayers;
        this.players = players;
        this.setPreferredSize(new Dimension(CurveFever.defaultGameFrameWidth - CurveFever.defaultGameFieldSize,
                CurveFever.defaultGameFieldSize));
        this.setLayout(new FlowLayout());
        this.setBackground(Color.black);
        panelInit();
    }

    public void updateScores() {

        sort();

        for (int i = 0; i < numberOfInPlayers; i++) {
            String tmp = "" + inPlayers[i].getName();
            while (tmp.length() < 15)
                tmp += " ";
            scoreLabels[i].setText(tmp + inPlayers[i].getScore());
            scoreLabels[i].setForeground(inPlayers[i].getColor());
        }
    }

    private void panelInit() {
        JLabel label = new JLabel("Goal");
        label.setFont(new Font("Serif", Font.PLAIN, 2 * fontSize));
        this.add(label);
        label = new JLabel("" + (numberOfInPlayers * 5 - 5));
        label.setFont(new Font("Serif", Font.PLAIN, 2 * fontSize));
        this.add(label);
        label = new JLabel("2 points diff");
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        this.add(label);

        scoreLabels = new JLabel[numberOfInPlayers];
        inPlayers = new CurveFeverPlayer[numberOfInPlayers];
        int j = 0;
        for (int i = 0; i < CurveFever.numberOfPlayers; i++)
            if (players[i].isIn())
                inPlayers[j++] = players[i];
        for (int i = 0; i < numberOfInPlayers; i++) {
            scoreLabels[i] = new JLabel();
            scoreLabels[i].setPreferredSize(compDim);
            scoreLabels[i].setBackground(Color.black);
            scoreLabels[i].setFont(new Font(Font.SERIF, 0, 22));
            this.add(scoreLabels[i]);
        }
    }

    private void sort() {
        for (int i = 0; i < numberOfInPlayers; i++)
            for (int j = i + 1; j < numberOfInPlayers; j++)
                if (inPlayers[i].getScore() < inPlayers[j].getScore()) {
                    CurveFeverPlayer tmp = inPlayers[i];
                    inPlayers[i] = inPlayers[j];
                    inPlayers[j] = tmp;
                }
    }
}
