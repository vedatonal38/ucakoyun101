package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Varlik {
    public int x, y;
    public int dx, dy;
    private ResimKaynakYonetim yonetim;

    private Rectangle bizim = new Rectangle();
    private Rectangle dusman = new Rectangle();

    /**
     *
     * @param path
     * @param x
     * @param y
     * @param donmeTuru
     */
    public Varlik(String path, int x, int y, int donmeTuru) {
        this.yonetim = ResimKaynakDeposu.get().getResimYuklemeRotate(path, donmeTuru);
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param path
     * @param x
     * @param y
     */
    public Varlik(String path, int x, int y) {
        this.yonetim = ResimKaynakDeposu.get().getResimYukleme(path);
        this.x = x;
        this.y = y;
    }

    public void hareketEt() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {
        yonetim.draw(g, x, y);
    }

    public void setYatayHizin(int dx) {
        this.dx = dx;
    }
 
    public void setDikelHizin(int dy) {
        this.dy = dy;
    }    
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return this.yonetim.getWidth();
    }

    public int getHeidht() {
        return this.yonetim.getHeidht();
    }

    public boolean carpismalarKontrol(Varlik karis) {
        bizim.setBounds((int) x, (int) y, (int) yonetim.getWidth(), (int) yonetim.getHeidht());
        dusman.setBounds((int) karis.x, (int) karis.y, (int) karis.yonetim.getWidth(), (int) karis.yonetim.getHeidht());
        return bizim.intersects(dusman);
    }

    public abstract void carpismaKontrol(Varlik karis);
}
