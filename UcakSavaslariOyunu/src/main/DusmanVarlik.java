package main;

public class DusmanVarlik extends Varlik {

    private int hareketHiz = 3;
    private OyunAlani oyun;

    /**
     *
     * @param oyun
     * @param path
     * @param x
     * @param y
     * @param donmeTur
     */
    public DusmanVarlik(OyunAlani oyun, String path, int x, int y, int donmeTur) {
        super(path, x, y, donmeTur);
        this.oyun = oyun;

        dy = hareketHiz;
    }
    
    /**
     *
     * @param oyun
     * @param path
     * @param x
     * @param y
     */
    public DusmanVarlik(OyunAlani oyun, String path, int x, int y) {
        super(path, x, y);
        this.oyun = oyun;

        dy = hareketHiz;
    }

    @Override
    public void hareketEt() {

        if (y > oyun.getHeight()) {
            y = -30;
        }

        super.hareketEt();
    }

    @Override
    public void carpismaKontrol(Varlik karis) {
    }

}
