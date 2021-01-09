package main;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ResimKaynakDeposu {

    private static ResimKaynakDeposu depo = new ResimKaynakDeposu();
    private HashMap resimObj = new HashMap();

    public static ResimKaynakDeposu get() {
        return depo;
    }

    public ResimKaynakYonetim getResimYuklemeRotate(String path, int donmeTur) {
        if (resimObj.get(path) != null) {
            return (ResimKaynakYonetim) resimObj.get(path);
        }

        BufferedImage kaynakImg = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(path);

            if (url == null) {
                hata("Kaynak referans dosya yolu bulamıyorum : " + path);
            }

            kaynakImg = rotateImage(ImageIO.read(url), donmeTur);
            // donme tur ne demek : 1 -> 90 derece
            //                      2 -> 180 derece
            //                      3 -> 270 derece
            //                      4 -> 360 derece ımage dondur
        } catch (IOException e) {
            hata("Kaynak dosya yuklenemedi : " + path);
        }

        GraphicsConfiguration ayar = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = ayar.createCompatibleImage(kaynakImg.getWidth(), kaynakImg.getHeight(), Transparency.BITMASK);

        image.getGraphics().drawImage(kaynakImg, 0, 0, null);

        ResimKaynakYonetim rky = new ResimKaynakYonetim(image);
        this.resimObj.put(path, rky);

        return rky;
    }

    public ResimKaynakYonetim getResimYukleme(String path) {
        if (resimObj.get(path) != null) {
            return (ResimKaynakYonetim) resimObj.get(path);
        }

        BufferedImage kaynakImg = null;

        try {
//            URL url = this.getClass().getClassLoader().getResource(path);
            URL url = ResimKaynakDeposu.class.getClassLoader().getResource(path);

            if (url == null) {
                hata("Kaynak referans dosya yolu bulamıyorum : " + path);
            }

            kaynakImg = ImageIO.read(url);
        } catch (IOException e) {
            hata("Kaynak dosya yuklenemedi : " + path);
        }

        GraphicsConfiguration ayar = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = ayar.createCompatibleImage(kaynakImg.getWidth(), kaynakImg.getHeight(), Transparency.BITMASK);

        image.getGraphics().drawImage(kaynakImg, 0, 0, null);

        ResimKaynakYonetim rky = new ResimKaynakYonetim(image);
        this.resimObj.put(path, rky);

        return rky;
    }

    private void hata(String str) {
        System.err.println(str);
        System.exit(0);
    }

    private BufferedImage rotateImage(BufferedImage img, int donmeTuru) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newImage.setRGB(height - 1 - j, i, img.getRGB(i, j));
            }
        }

        donmeTuru--;
        if (donmeTuru == 0) {
            return newImage;
        } else {
            return rotateImage(newImage, donmeTuru);
        }
    }

}
