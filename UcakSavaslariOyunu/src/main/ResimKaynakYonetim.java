package main;

import java.awt.Graphics2D;
import java.awt.Image;

public class ResimKaynakYonetim {

    private Image goruntu;

    public ResimKaynakYonetim(Image goruntu) {
        this.goruntu = goruntu;
    }

    public int getWidth() {
        return this.goruntu.getWidth(null);
    }

    public int getHeidht() {
        return this.goruntu.getHeight(null);
    }

    public Image getGoruntu() {
        return goruntu;
    }

    /**
     * Resim Kaynak Yonetim sağlanan grafik bağlamına çizin
     *
     * @param g Hareketli grafiğin üzerine çizileceği grafik bağlamı
     * @param x Hareketli grafiğin çizileceği x konumu
     * @param y Hareketli grafiğin çizileceği y konumu
     */
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(goruntu, x, y, null);
    }
}
