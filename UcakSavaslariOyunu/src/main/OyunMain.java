package main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class OyunMain extends JFrame {

    public OyunMain() {
        init();
    }

    private void init() {
        setSize(900, 1000);
        OyunAlani panel = new OyunAlani(getWidth(), getHeight());
        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                panel.getTimer().stop();
                System.exit(0);
            }
        });
        setTitle(setTitleCenter("UCAK SAVASLARI"));
        setIconImage(loadIcon());
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }

    private String setTitleCenter(String str) {

        setFont(new Font("System", Font.PLAIN, 14));
        Font f = getFont();
        FontMetrics fm = getFontMetrics(f);
        int x = fm.stringWidth(str);
        int y = fm.stringWidth(" ");
        int z = getWidth() / 2 - (x / 2);
        int w = z / y;
        String pad = "";

        pad = String.format("%" + w + "s", pad);

        return pad + str;
    }

    private Image loadIcon() {
        return ResimKaynakDeposu.get().getResimYukleme("img/frameIcon.png").getGoruntu();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                OyunMain ex = new OyunMain();
            }
        });
    }

}
