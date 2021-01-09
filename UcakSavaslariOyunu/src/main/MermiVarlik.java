package main;

public class MermiVarlik extends Varlik {

    private int hareketHiz = -5;
    private OyunAlani oyun;
    private boolean oyunucuMu = false;

    /**
     *
     * @param oyun
     * @param x
     * @param y
     */
    public MermiVarlik(OyunAlani oyun, int x, int y) {
        super("img/mermi.png", x, y, 2);
        this.oyun = oyun;

        dy = hareketHiz;
    }

    @Override
    public void hareketEt() {
        if (y < -10) {
            oyun.kaldirVarlik(this);
        }
        super.hareketEt();
    }

    @Override
    public void carpismaKontrol(Varlik karis) {
        if (oyunucuMu) {
            return;
        }
        if (karis instanceof DusmanVarlik) {
            oyun.kaldirVarlik(this);
            oyun.kaldirVarlik(karis);
            oyun.dusmanVururdu();
            oyunucuMu = true;
        }
    }

}
