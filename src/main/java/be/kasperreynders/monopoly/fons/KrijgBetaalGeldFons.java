package be.kasperreynders.monopoly.fons;

import be.kasperreynders.monopoly.spel.Bord;
import be.kasperreynders.monopoly.spel.Kaart;
import be.kasperreynders.monopoly.spel.Speler;

import java.util.ArrayList;

public class KrijgBetaalGeldFons implements FonsKaart {
    private final String type;
    private final int geld;
    private final Bord bord;
    private final String text;

    public KrijgBetaalGeldFons(String type, int geld, Bord bord, String text) {
        this.type = type;
        this.geld = geld;
        this.bord = bord;
        this.text = text;
    }

    @Override
    public void voerUit(Speler speler) {
        switch (type) {
            case "krijgenBank" -> speler.addGeld(geld);
            case "betaalBank" -> speler.betaalGeld(geld);
            case "krijgSpelers" -> krijgVanElkeSpeler(speler);
            case "betaalSpelers" -> betaalAanElkeSpeler(speler);
            case "betaalHuis" -> betaalVoorHuis(speler);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    private void betaalAanElkeSpeler(Speler speler) {
        ArrayList<Speler> spelers = bord.getSpelers();
        spelers.forEach(s -> s.addGeld(geld));
        speler.betaalGeld(geld*spelers.size());
    }

    private void krijgVanElkeSpeler(Speler speler) {
        ArrayList<Speler> spelers = bord.getSpelers();
        spelers.forEach(s -> {
            if (!s.equals(speler)) {
                s.betaalGeld(geld);
            }
        });
        speler.addGeld(geld*(spelers.size()-1));
    }

    private void betaalVoorHuis(Speler speler) {
        ArrayList<Kaart> kaarten = bord.getKaartenBySpeler(speler);
        kaarten.forEach(kaart -> {
            if (kaart.huisjes() > 0) {
                if (kaart.huisjes() < 5) {
                    speler.betaalGeld(geld);
                } else {
                    speler.betaalGeld((int) (geld*2.875));
                }
            }
        });
    }
}
