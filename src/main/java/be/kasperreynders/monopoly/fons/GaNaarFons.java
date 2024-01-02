package be.kasperreynders.monopoly.fons;

import be.kasperreynders.monopoly.spel.Speler;

public class GaNaarFons implements FonsKaart {
    public final int pos;
    private final String text;

    public GaNaarFons(int pos, String text) {
        this.pos = pos;
        this.text = text;
    }

    @Override
    public void voerUit(Speler speler) {
        if ((pos-speler.getPos()) < 0) {
            speler.addGeld(200);
        }
        speler.setPos(pos);
    }

    @Override
    public String getText() {
        return text;
    }
}
