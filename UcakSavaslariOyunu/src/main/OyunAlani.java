package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

class OyunAlani extends JPanel implements
        ActionListener {

    private ResimKaynakYonetim arkaPlanYoneti;
    private ResimKaynakYonetim heartYoneti;
    private ResimKaynakYonetim sarojYonetim;
    private ResimKaynakYonetim logoYoneti;
    private ResimKaynakYonetim restart;
    private ResimKaynakYonetim[] tuslar;
    private int width, heigth;
    private Timer tmr;
    private final int DELAY = 30;
    private final int INITIAL_DELAY = 500;

    private Varlik bizimUcak;
    private Varlik dusman;
    private MermiVarlik tempMermi = new MermiVarlik(this, 0, 0);

    private int dusmanSayisi;
    private int dusmanTuru;
    private int level;
    private int maxLevel = 6;
    private boolean levelGectin;

    private int bizimUcakHareketHizi = 5;
    private int bizimHeart;
    private ArrayList varliklar = new ArrayList();
    private ArrayList kaldirVarliklarList = new ArrayList();
    private boolean beklemeBaslama;
    private boolean bekleme;
    private int beklemeInt;
    private boolean oyunucuOldu;//oyunucu öldü

    private int levelUpString_X = 1;
    private float alpha = 1;
    private int mermiSayisi;
    private int mermiAtisSayisi;
    private int skor;

    private int[][] konumlar = {
        {100, -70}, {275, -120}, {420, -30}, {580, -90}, {740, -135}, // 1
        {50, -180}, {190, -220}, {370, -190}, {500, -210}, {660, -235}, // 2
        {70, -290}, {215, -320}, {340, -350}, {490, -330}, {720, -310}, // 3
        {110, -390}, {290, -430}, {380, -460}, {540, -430}, {770, -410},// 4
        {65, -460}, {215, -520}, {285, -550}, {455, -565}, {595, -540}, // 5
        {40, -590}, {150, -600}, {250, -640}, {415, -650}, {675, -620} // 6
    };

    public OyunAlani(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
        init();
        sarojLoadImage();
        tuslarLoadImage();
        loadImageArkaPlan();
        heartLoadImage();
        logoLoadImage();
        restartLoadImage();
    }

    private void init() {
        addKeyListener(new GirisAdapter());
        setFocusable(true);
        tmr = new Timer(DELAY, this);
        tmr.setInitialDelay(INITIAL_DELAY);
        tmr.start();

        restart();

        beklemeBaslama = true;

        bekleme = true;
        beklemeInt = 0;
    }

    private void restart() {
        oyunucuOldu = false;

        bizimHeart = 3;
        level = 0;
        dusmanSayisi = 0;
        dusmanTuru = 0;
        mermiSayisi = 10;
        mermiAtisSayisi = 0;
        skor = 0;
        levelGectin = true;

        varliklar.clear();
        kaldirVarliklarList.clear();

        bizimUcakLoadImage();
        dusmanUcakLoadImage();
    }

    private void bizimUcakLoadImage() {
        bizimUcak = new BizimVarlik(this, width / 2, heigth + 50);
        varliklar.add(bizimUcak);
    }

    private void dusmanUcakLoadImage() {
        for (int[] item : konumlar) {
            if (dusmanSayisi < (level + 1) * 5) {
                dusmanYukle(item[0], item[1]);
            }
        }
    }

    private void dusmanYukle(int x, int y) {
        if (dusmanTuru % 4 == 0) {
            dusman = new DusmanVarlik(this, "img/dusmanJet4.png", x, y, 2);
        }
        if (dusmanTuru % 4 == 1) {
            dusman = new DusmanVarlik(this, "img/dusmanJet3.png", x, y, 2);
        }
        if (dusmanTuru % 4 == 2) {
            dusman = new DusmanVarlik(this, "img/dusmanJet2.png", x, y);
        }
        if (dusmanTuru % 4 == 3) {
            dusman = new DusmanVarlik(this, "img/dusmanJet1.png", x, y);
        }
        varliklar.add(dusman);
        dusmanSayisi++;
        dusmanTuru++;
    }

    private void loadImageArkaPlan() {
        this.arkaPlanYoneti = ResimKaynakDeposu.get().getResimYukleme("img/sky.jpg");
    }

    private void heartLoadImage() {
        this.heartYoneti = ResimKaynakDeposu.get().getResimYukleme("img/heart.png");
    }

    private void logoLoadImage() {
        this.logoYoneti = ResimKaynakDeposu.get().getResimYukleme("img/vedatonal.PNG");
    }

    private void sarojLoadImage() {
        this.sarojYonetim = ResimKaynakDeposu.get().getResimYukleme("img/mermi.png");
    }

    private void restartLoadImage() {
        this.restart = ResimKaynakDeposu.get().getResimYukleme("img/restart.png");
    }

    private void tuslarLoadImage() {
        this.tuslar = new ResimKaynakYonetim[5];
        this.tuslar[0] = ResimKaynakDeposu.get().getResimYukleme("img/b.png");
        this.tuslar[1] = ResimKaynakDeposu.get().getResimYukleme("img/ctrl.png");
        this.tuslar[2] = ResimKaynakDeposu.get().getResimYukleme("img/sol.png");
        this.tuslar[3] = ResimKaynakDeposu.get().getResimYukleme("img/sag.png");
        this.tuslar[4] = ResimKaynakDeposu.get().getResimYukleme("img/p.png");
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2d = (Graphics2D) g.create();
        arkaPlanYoneti.draw(g2d, 0, 0);
        if (beklemeBaslama) {// true bekleme
            drawBekleme(g2d);
        } else if (oyunucuOldu) {
            drawOyunucuOlumu(g2d);
        } else {
            if (level < maxLevel) {
                if (levelGectin) {// true 
                    drawLevelUp(g2d);
                } else {
                    doDraw(g2d);
                    tabloDraw(g2d);
                    heartBizim(g2d);
                    saroj(g2d);
                    if (!bekleme) {
                        durdur(g2d);
                    }
                }
            } else {
                oyunKazandi(g2d);
            }
        }

    }

    private void durdur(Graphics2D g2d) {
        g2d.setFont(new Font("Consolas", Font.BOLD, 30));
        String str = "((P))";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(str, width - fm.stringWidth(str) - 20, 50);
    }

    private void drawBekleme(Graphics2D g2d) {
        BufferedImage buff = new BufferedImage(600, 250, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = buff.createGraphics();

        gbi.setColor(Color.BLACK);
        gbi.setFont(new Font("Consolas", Font.BOLD, 25));
        String str = "Oyun Baslamak icin       tusuna basın";
        FontMetrics fm = gbi.getFontMetrics();
        gbi.drawString(str, (buff.getWidth() - fm.stringWidth(str)) / 2, 70);
        gbi.drawImage(this.tuslar[0].getGoruntu(), buff.getWidth() / 2, 35, null);

        int xKonum = 160, yKonum = 160;
        str = "Ates";
        gbi.drawString(str, (xKonum + fm.stringWidth(str) / 2) - 10, yKonum - 10);
        gbi.drawImage(this.tuslar[1].getGoruntu(), xKonum, yKonum, null);
        str = "Sol";
        gbi.drawString(str, (xKonum + this.tuslar[1].getWidth() + fm.stringWidth(str) / 2 + 10), yKonum - 10);
        gbi.drawImage(this.tuslar[2].getGoruntu(), xKonum + this.tuslar[1].getWidth() + 20, yKonum, null);
        str = "Sag";
        gbi.drawString(str, (xKonum + this.tuslar[1].getWidth() + this.tuslar[2].getWidth() + fm.stringWidth(str) / 2 + 20), yKonum - 10);
        gbi.drawImage(this.tuslar[3].getGoruntu(), xKonum + this.tuslar[1].getWidth() + this.tuslar[2].getWidth() + 40, yKonum, null);
        str = "Durdur";
        gbi.drawString(str, (xKonum + this.tuslar[1].getWidth() + this.tuslar[2].getWidth() + this.tuslar[3].getWidth() + fm.stringWidth(str) / 2 + 20), yKonum - 10);
        gbi.drawImage(this.tuslar[4].getGoruntu(), xKonum + this.tuslar[1].getWidth() + this.tuslar[2].getWidth() + this.tuslar[3].getWidth() + 60, yKonum, null);

        g2d.drawImage(buff, (width - buff.getWidth()) / 2, heigth / 2 - 100, null);
    }

    private void doDraw(Graphics2D g2d) {
        for (int i = 0; i < this.varliklar.size(); i++) {
            Varlik var = (Varlik) this.varliklar.get(i);

            var.draw(g2d);
        }
    }

    private void tabloDraw(Graphics2D g2d) {
        String mermiStok = "Mermi sayisi : " + (mermiSayisi - mermiAtisSayisi);
        String skorTablo = "Skor : " + skor;

        Font small = new Font("Consolas", Font.BOLD, 20);

        g2d.setColor(Color.red);
        g2d.setFont(small);
        g2d.drawRect(0, 0, 210, 40);

        g2d.drawString(mermiStok, 5, 15);
        g2d.drawString(skorTablo, 5, 35);

    }

    private void heartBizim(Graphics2D g2d) {
        BufferedImage heartBuff = new BufferedImage(75, 25, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = heartBuff.createGraphics();

        for (int i = 0; i < bizimHeart; i++) {
            gbi.drawImage(heartYoneti.getGoruntu(), (i * 25), 0, null);
        }

        g2d.drawImage(heartBuff, (width - 100), 0, null);
    }

    private void saroj(Graphics2D g2d) {
        if (this.mermiSayisi - this.mermiAtisSayisi != 0) {
            BufferedImage mermeBuff = new BufferedImage(15, (25 * (this.mermiSayisi - this.mermiAtisSayisi)), BufferedImage.TYPE_INT_ARGB);
            Graphics2D gbi = mermeBuff.createGraphics();
            for (int i = 0; i <= (this.mermiSayisi - this.mermiAtisSayisi); i++) {
                gbi.drawImage(sarojYonetim.getGoruntu(), 0, i * 25, null);
            }
            g2d.drawImage(mermeBuff, (width - 50), (heigth - 50) - 25 * (this.mermiSayisi - this.mermiAtisSayisi), null);
        }
    }

    public void drawOyunucuOlumu(Graphics2D g2d) {
        String msg = "OYUNU KAYBETTINIZ...";
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(msg, (width - fm.stringWidth(msg)) / 2, heigth / 2 - 100);
        msg = "Skor : " + skor;
        g2d.drawString(msg, (width - fm.stringWidth(msg)) / 2, heigth / 2 - 70);
        restart(g2d);
        yapimciLogo(g2d);
    }

    private void oyunKazandi(Graphics2D g2d) {
        String msg = "OYUNU KAZANDINIZ TEBRIKLER ";
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(msg, (width - fm.stringWidth(msg)) / 2, heigth / 2 - 100);
        msg = "Skor : " + skor;
        g2d.drawString(msg, (width - fm.stringWidth(msg)) / 2, heigth / 2 - 70);
        restart(g2d);
        yapimciLogo(g2d);
    }

    private void restart(Graphics2D g2d) {
        BufferedImage buff = new BufferedImage(250, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = buff.createGraphics();

        gbi.drawImage(this.restart.getGoruntu(), (buff.getWidth() - this.restart.getWidth()) / 2, 0, null);

        String msg = "Yeniden denemek";
        gbi.setColor(Color.BLACK);
        gbi.setFont(new Font("Consolas", Font.BOLD, 18));
        FontMetrics fm = gbi.getFontMetrics();
        gbi.drawString(msg, (buff.getWidth() - fm.stringWidth(msg)) / 2, 90);
        msg = "istersen < ENTER > bas";
        gbi.drawString(msg, (buff.getWidth() - fm.stringWidth(msg)) / 2, 110);

        g2d.drawImage(buff, (width - buff.getWidth()) / 2, (heigth - buff.getHeight()) / 2, null);
    }

    private void yapimciLogo(Graphics2D g2d) {
        BufferedImage buff = new BufferedImage(this.logoYoneti.getWidth(), this.logoYoneti.getHeidht() + 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = buff.createGraphics();

        gbi.setColor(Color.BLACK);
        gbi.setFont(new Font("Consolas", Font.BOLD, 30));
        String str = "YAPİMCİ";
        FontMetrics fm = gbi.getFontMetrics();
        gbi.drawString(str, (buff.getWidth() - fm.stringWidth(str)) / 2, fm.getAscent());
        gbi.drawImage(this.logoYoneti.getGoruntu(), 0, 30, null);

        g2d.drawImage(buff, (width - this.logoYoneti.getWidth()) / 2, (heigth / 2) + 80, null);
    }

    private void drawLevelUp(Graphics2D g2d) {
        RenderingHints rh
                = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        Font font = new Font("Dialog", Font.PLAIN, levelUpString_X);
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        String s = (level + 1) + ". SEVİYE BAŞLA";
        Dimension size = getSize();

        int w = (int) size.getWidth();
        int h = (int) size.getHeight();

        int stringWidth = fm.stringWidth(s);
        AlphaComposite ac = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(ac);

        g2d.drawString(s, (w - stringWidth) / 2, h / 2);

        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!beklemeBaslama) { // !true hareketsiz !false hareketli
            if (level < maxLevel) {
                if (levelGectin) {
                    step();

                } else if (bekleme) {
                    hareketlendime();
                }
            }
        }
        repaint();
    }

    private void step() {

        levelUpString_X += 1;

        if (levelUpString_X > 40) {
            alpha -= 0.01;
        }

        if (alpha <= 0.01) {
            levelGectin = false;
            alpha = 1;
            levelUpString_X = 1;
        }
    }

    private void hareketlendime() {
        for (int i = 0; i < this.varliklar.size(); i++) {
            Varlik var = (Varlik) this.varliklar.get(i);
            var.hareketEt();
        }
        carpismaKonrol();
        if (dusmanSayisi == 0) {
            level++;
            levelGectin = true;
            mermiSayisi = (mermiSayisi - mermiAtisSayisi) + mermiSayisi + 3;
            mermiAtisSayisi = 0;
            mermileriTemizle();
            dusmanUcakLoadImage();
        }
    }

    private void mermileriTemizle() {
        for (int i = 0; i < varliklar.size(); i++) {
            Varlik mermi = (Varlik) varliklar.get(i);
            if (mermi instanceof MermiVarlik) {
                kaldirVarliklarList.add(mermi);
            }
        }
        varliklar.removeAll(kaldirVarliklarList);
        kaldirVarliklarList.clear();
    }

    private void carpismaKonrol() {
        for (int i = 0; i < varliklar.size(); i++) {
            for (int j = i + 1; j < varliklar.size(); j++) {
                Varlik bizim = (Varlik) varliklar.get(i);
                Varlik dusman = (Varlik) varliklar.get(j);
                if (bizim.carpismalarKontrol(dusman)) {
                    bizim.carpismaKontrol(dusman);
                    dusman.carpismaKontrol(bizim);
                }
            }
        }
        varliklar.removeAll(kaldirVarliklarList);
        kaldirVarliklarList.clear();
    }

    private void atis() {
        MermiVarlik mermi = new MermiVarlik(this, bizimUcak.getX() + bizimUcak.getWidth() / 2 - 6, bizimUcak.getY());
        if (bizimUcak.y - tempMermi.y > 25 && mermiAtisSayisi < mermiSayisi) { // atis sayi mermi sınır dan kucuk oldukça atis serbest
            varliklar.add(mermi);
            mermiAtisSayisi++;
            tempMermi = mermi;
        }
    }

    public Timer getTimer() {
        return this.tmr;
    }

    public void kaldirVarlik(Varlik varlik) {
        kaldirVarliklarList.add(varlik);
    }

    public void dusmanVururdu() {
        dusmanSayisi--;
        skor += 10;
    }

    public void setOyunucuOldu(boolean oyunucuOldu) {
        if (bizimHeart == 1) {
            this.oyunucuOldu = oyunucuOldu;
        } else {
            bizimHeart--;
            skor -= 15;
        }
    }

    public int getBizimHearrt() {
        return bizimHeart;
    }

    private class GirisAdapter implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();

            if (code == KeyEvent.VK_LEFT) {//sol
                bizimUcak.setYatayHizin(-bizimUcakHareketHizi);
            }
            if (code == KeyEvent.VK_RIGHT) {//sag
                bizimUcak.setYatayHizin(bizimUcakHareketHizi);
            }

            if (code == KeyEvent.VK_CONTROL) {
                atis();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int code = e.getKeyCode();
            if (beklemeBaslama) {
                if (code == KeyEvent.VK_B) {
                    beklemeBaslama = false;
                } else {
                    return;
                }
            }

            if (code == KeyEvent.VK_P) {
                if (beklemeInt == 0) {
                    bekleme = false;
                    beklemeInt = 1;
                } else {
                    bekleme = true;
                    beklemeInt = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER) {
                restart();
            }

            if (code == KeyEvent.VK_LEFT) {
                bizimUcak.setYatayHizin(0);
            }
            if (code == KeyEvent.VK_RIGHT) {
                bizimUcak.setYatayHizin(0);
            }
        }

    }
}
