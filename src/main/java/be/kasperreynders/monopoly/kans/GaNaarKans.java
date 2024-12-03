package be.kasperreynders.monopoly.kans;

import be.kasperreynders.monopoly.spel.*;

import java.util.ArrayList;
import java.util.Optional;

public class GaNaarKans implements KansKaart {
    public final ArrayList<Integer> plekken;
    private final String type;
    private final Bord bord;
    private final Dobbelsteen dobbelsteen;
    private final int terug;
    private final String text;

    public GaNaarKans(ArrayList<Integer> plekken, String type, Bord bord, Dobbelsteen dobbelsteen, int terug, String text) {
        this.plekken = plekken;
        this.type = type;
        this.bord = bord;
        this.dobbelsteen = dobbelsteen;
        this.terug = terug;
        this.text = text;
    }

    @Override
    public void voerUit(Speler speler) {
        if (!type.equals("terug")) {
            if (!type.equals("go")) {
                int dicht = dichtsBij(speler.getPos());
                if ((dicht - speler.getPos()) < 0) {
                    speler.addGeld(200);
                }
                speler.setPos(dicht);
                switch (type) {
                    case "treinDicht" -> treinDichtKaart(dicht, speler);
                    case "speciaal" -> specialeKaart(dicht, speler);
                    case "kaart" -> kaart(dicht, speler);
                    case "trein" -> treinKaart(speler, dicht);
                }
            } else {
                speler.addGeld(200);
                speler.setPos(0);
            }
        } else {
            int pos = speler.getPos()-terug;
            if (pos < 0) {
                pos += 40;
            }
            speler.setPos(pos);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    private int dichtsBij(int pos) {
        int dichts = plekken.getFirst();
        for (int p: plekken) {
            if (Math.abs(p-pos) < Math.abs(dichts-pos)) {
                dichts = p;
            }
        }
        return dichts;
    }

    private void kaart(int dicht, Speler speler) {
        Optional<Kaart> kaartOptional = bord.getKaart(dicht);
        if (kaartOptional.isPresent()) {
            Kaart kaart = kaartOptional.get();
            if (kaart.bezitter().isPresent()) {
                speler.betalen(kaart.huur(), kaart.bezitter().get());
            } else {
                speler.koopKaart(dicht, kaart.prijs(), "kaart");
            }
        }
    }

    private void treinDichtKaart(int dicht, Speler speler) {
        Optional<TreinKaart> kaartOptional = bord.getTreinKaart(dicht);
        if (kaartOptional.isPresent()) {
            TreinKaart kaart = kaartOptional.get();
            if (kaart.bezitter().isPresent()) {
                speler.betalen(kaart.huur()*2, kaart.bezitter().get());
            } else {
                speler.koopKaart(dicht, kaart.prijs(), type);
            }
        }
    }

    private void treinKaart(Speler speler, int dicht) {
        Optional<TreinKaart> kaartOptional = bord.getTreinKaart(dicht);
        if (kaartOptional.isPresent()) {
            TreinKaart kaart = kaartOptional.get();
            if (kaart.bezitter().isPresent()) {
                speler.betalen(kaart.huur(), kaart.bezitter().get());
            }
        }
    }

    private void specialeKaart(int dicht, Speler speler) {
        Optional<SpecialeKaart> kaartOptional = bord.getSpecialeKaart(dicht);
        if (kaartOptional.isPresent()) {
            SpecialeKaart kaart = kaartOptional.get();
            if (kaart.bezitter().isPresent()) {
                int[] gedobbeld = dobbelsteen.dobbelenMet2();
                speler.betalen(10*(gedobbeld[0]+gedobbeld[1]), kaart.bezitter().get());
            } else {
                speler.koopKaart(dicht, kaart.prijs(), type);
            }
        }
    }
}
