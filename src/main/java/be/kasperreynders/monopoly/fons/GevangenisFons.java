package be.kasperreynders.monopoly.fons;

import be.kasperreynders.monopoly.spel.Speler;

public class GevangenisFons implements FonsKaart {
    private final String type;
    private final String text;

    public GevangenisFons(String type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public void voerUit(Speler speler) {
        if (type.equals("uit")) {
            speler.magUitGevangenis = true;
        } else {
            speler.inGevangenis = true;
            speler.setPos(10);
        }
    }

    @Override
    public String getText() {
        return text;
    }
}
