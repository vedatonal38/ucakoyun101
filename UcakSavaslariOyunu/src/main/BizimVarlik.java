package main;

public class BizimVarlik extends Varlik {

    private OyunAlani oyun;

    /**
     *
     * @param oyun
     * @param x
     * @param y
     */
    public BizimVarlik(OyunAlani oyun, int x, int y) {
        super("img/ucak2.png", x, y);
        this.oyun = oyun;
        dy = -5;
    }

    @Override
    public void hareketEt() {
        if (x < 0) {
            x = 0;
            return;
        }
        if (x > oyun.getWidth() - this.getWidth()) {
            x = oyun.getWidth() - this.getWidth();
            return;
        }
        if (y < 826) {
            dy = 0;
            y = 826;
            return;
        }
        super.hareketEt();
    }

    @Override
    public void carpismaKontrol(Varlik karis) {
        if (karis instanceof DusmanVarlik) {
            karis.setY(-100);
            if (oyun.getBizimHearrt() == 1) {
                oyun.kaldirVarlik(this);
            }
            this.setX(10);
            this.setY(oyun.getHeight() - 60);
            this.setDikelHizin(-5);
            oyun.setOyunucuOldu(true);

        }
    }

}
