package be.kasperreynders.monopoly.spel;

import be.kasperreynders.monopoly.KeuzeBevestigd;
import be.kasperreynders.monopoly.KeuzesData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Speler {
    private final Bord bord;
    private int geld;
    private int pos = 0;
    private boolean gaNaarKansFons = false;

    public boolean inGevangenis = false;
    public boolean magUitGevangenis = false;

    private ArrayList<Integer> koopKeuzesPos;

    public Speler(int geld, Bord bord) {
        this.geld = geld;
        this.bord = bord;
    }

    public void addGeld(int geld) {
        this.geld += geld;
    }

    public int getGeld() {
        return geld;
    }

    private void specialeKaart(int stappen, boolean kopen) {
        Optional<SpecialeKaart> specialeKaartOptional = bord.getSpecialeKaart(pos);
        if (specialeKaartOptional.isPresent()) {
            SpecialeKaart specialeKaart = specialeKaartOptional.get();
            if (specialeKaart.bezitter().isPresent()) {
                int prijs = specialeKaart.maal() * stappen;
               verkopen(prijs);
                specialeKaart.bezitter().get().addGeld(prijs);
                geld -= prijs;
            } else {
                if (geld >= specialeKaart.prijs() && kopen) {
                    bord.koopSpeciaalBezit(this, pos);
                    geld -= specialeKaart.prijs();
                }
            }
        }
    }

    private void treinKaart(boolean kopen) {
        Optional<TreinKaart> treinKaartOptional = bord.getTreinKaart(pos);
        if (treinKaartOptional.isPresent()) {
            TreinKaart treinKaart = treinKaartOptional.get();
            if (treinKaart.bezitter().isPresent()) {
                int prijs = treinKaart.huur();
                verkopen(prijs);
                treinKaart.bezitter().get().addGeld(prijs);
                geld -= prijs;
            } else {
                if (geld >= treinKaart.prijs() && kopen) {
                    bord.koopTreinBezit(this, pos);
                    geld -= treinKaart.prijs();
                }
            }
        }
    }

    private void verkopen(int prijs) {
        while (geld < (prijs)) {
            int p = bord.verkoopBezit(this);
            if (p == 0)
                break;
            geld += p/2;
        }
    }

    private void kaart(boolean kopen) {
        Optional<Kaart> kaart = bord.getKaart(pos);
        if (kaart.isPresent()) {
            Kaart echteKaart = kaart.get();
            if (echteKaart.bezitter().isPresent()) {
                verkopen(echteKaart.huur());
                echteKaart.bezitter().get().addGeld(echteKaart.huur());
                geld -= echteKaart.huur();
            } else {
                if (geld >= echteKaart.prijs() && kopen) {
                    bord.koopBezet(this, pos);
                    geld -= echteKaart.prijs();
                }
            }
        }
    }

    public int overLijn(int pos) {
        if (pos >= 40) {
            geld += 200;
            return pos - 40;
        }
        return pos;
    }

    public boolean verloren() {
        return (geld < 0);
    }

    private void koopHuisjes(int huisjes) {
        for (int i = 0; i < huisjes; i++) {
            geld -= bord.koopHuisje(this, geld);
        }
    }

    public void gevangen() {
        pos = 10;
        inGevangenis = true;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void betalen(int prijs, Speler speler) {
        verkopen(prijs);
        speler.addGeld(prijs);
        geld -= prijs;
    }

    public int getPos() {
        return pos;
    }

    public void koopKaart(int pos, int prijs, String type) {
        koopKeuzesPos.add(pos);
    }

    public void betaalGeld(int betalen) {
        geld -= betalen;
    }

    public KeuzesData keuzes(int[] stappen) {
        gaNaarKansFons = false;
        int oudePos = pos;
        int totaalStappen = stappen[0]+stappen[1];
        koopKeuzesPos = new ArrayList<>();
        int nieuwePos = 0;
        if (!inGevangenis){
            nieuwePos = overLijn(pos + totaalStappen);
        }
        int betalen = bord.betalen(pos);
        Optional<Kaart> kaartOptional = bord.getKaart(nieuwePos);
        if (kaartOptional.isPresent()) {
            Kaart kaart = kaartOptional.get();
            if (kaart.bezitter().isEmpty()) {
                koopKeuzesPos.add(nieuwePos);
            }
        }
        Optional<TreinKaart> treinKaartOptional = bord.getTreinKaart(nieuwePos);
        if (treinKaartOptional.isPresent()) {
            TreinKaart treinKaart = treinKaartOptional.get();
            if (treinKaart.bezitter().isEmpty()) {
                koopKeuzesPos.add(nieuwePos);
            }
        }
        Optional<SpecialeKaart> specialeKaartOptional = bord.getSpecialeKaart(nieuwePos);
        if (specialeKaartOptional.isPresent()) {
            SpecialeKaart specialeKaart = specialeKaartOptional.get();
            if (specialeKaart.bezitter().isEmpty()) {
                koopKeuzesPos.add(nieuwePos);
            }
        }
        String text = bord.kansEnFons(this, nieuwePos);
        if (text.equals("Ga verder naar \"Start\".") || text.equals("Ga naar Illinois Avenue.\n Als je Start passeert, verzamel je $200.") ||
                text.equals("Ga verder naar St. Charles Place.\n Als je Start passeert, verzamel dan $200.") ||
                text.equals("Ga naar het dichtstbijzijnde hulpprogramma.\n Als u het niet in uw bezit heeft, kunt u het bij de bank kopen.\n Als je eigenaar bent, gooi je de dobbelstenen en betaal je de eigenaar in totaal 10 keer het gegooide bedrag.") ||
                text.equals("Maak een uitstapje naar de Reading Railroad.") || text.equals("Maak een wandeling over de Boardwalk.\n Ga naar Boardwalk.") ||
                text.equals("Ga naar de dichtstbijzijnde spoorweg.\n Als u het niet in uw bezit heeft, kunt u het bij de bank kopen.\n Als u geen eigenaar bent, betaalt u de eigenaar tweemaal de huur waar hij anders recht op heeft.\n Als Railroad geen eigendom is, kunt u het bij de bank kopen.")) {
            gaNaarKansFons = true;
        }
        KeuzesData k = new KeuzesData(koopKeuzesPos, inGevangenis, magUitGevangenis, betalen, oudePos, geld, text);
        geld -= betalen+bord.betaalHuur(nieuwePos, totaalStappen);
        return k;
    }

    public void doeKeuzes(KeuzeBevestigd keuzeBevestigd, int[] stappen) {
        int totaalStappen = stappen[0] + stappen[1];
        if (pos + totaalStappen == 30) {
            gevangen();
        } else if (keuzeBevestigd.koopVrij()) {
            geld -= 50;
            inGevangenis = false;
        } else if (keuzeBevestigd.gebruikVrij()) {
            magUitGevangenis = false;
            inGevangenis = false;
        } else {
            if (keuzeBevestigd.gooiDubbel() && stappen[0] == stappen[1]) {
                inGevangenis = false;
            }
            if (!inGevangenis) {
                if (!gaNaarKansFons) {
                    pos = overLijn(pos + totaalStappen);
                }
                geld += bord.verkoopOpPos(keuzeBevestigd.verkocht(), this);
                geld += bord.verkoopHuisjesOpPos(keuzeBevestigd.huisjesVerkocht(), this);
                geld -= bord.betalen(pos);
                geld -= bord.koopOpPos(keuzeBevestigd.gekocht(), this);
                geld -= bord.koopHuisjesOpPos(keuzeBevestigd.huisjesGekocht(), this);
            }
        }
    }
}
