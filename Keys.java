import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Keys extends JPanel implements KeyListener {
    
    public Keys() {
        addKeyListener(this);
        setFocusable(true); // Required for the panel to receive keyboard events
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This method is called when a key is typed
        System.out.println("Key Typed: " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // This method is called when a key is pressed down
        System.out.println("Key Pressed: " + e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // This method is called when a key is released
        System.out.println("Key Released: " + e.getKeyCode());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Keyboard Listener Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.add(new Keys());
        frame.setVisible(true);
    }
}
