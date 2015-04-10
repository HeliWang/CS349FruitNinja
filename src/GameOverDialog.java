import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameOverDialog extends JOptionPane {

    private final Object[] options = {"Exit", "Play Again"};
    private JDialog dialog;

    public GameOverDialog(String message, String title) {
        super(message);

        setOptions(options);

        dialog = createDialog(new JFrame(), title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public boolean returnAnswer() {
        Object promptAnswer = getValue();
        boolean answer = true;

        if(promptAnswer.equals(options[0])) {
            answer = false;
        }

        dialog.dispose();

        return answer;
    }
}
