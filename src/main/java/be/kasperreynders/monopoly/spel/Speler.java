package be.kasperreynders.monopoly.spel;

import java.util.Optional;

public class Speler {
    private final Bord bord;
    private int geld;
    private int pos = 0;
    public boolean inGevangenis = false;
    public boolean magUitGevangenis = false;

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

    public String stap(int[] stappen, boolean kopen, int huisjes, boolean vrijkopen) {
        int totaalStappen = stappen[0]+stappen[1];
        if (pos + totaalStappen == 30) {
            gevangen();
        } else if (magUitGevangenis) {
            inGevangenis = false;
            magUitGevangenis = false;
        } else if (vrijkopen && inGevangenis) {
            if (geld >= 50) {
                geld -= 50;
                inGevangenis = false;
            }
        } else {
            if (stappen[0] == stappen[1] && inGevangenis) {
                inGevangenis = false;
            }
            if (!inGevangenis) {
                pos = overLijn(pos + totaalStappen);
                int betalen = bord.betalen(pos);
                verkopen(betalen);
                geld -= betalen;
                kaart(kopen);
                treinKaart(kopen);
                specialeKaart(totaalStappen, kopen);
                if (kopen)
                    koopHuisjes(huisjes);
            }
        }
        return bord.kansEnFons(this);
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
        switch (type) {
            case "kaart" -> bord.koopBezet(this, pos);
            case "trein" -> bord.koopTreinBezit(this, pos);
            case "speciaal" -> bord.koopSpeciaalBezit(this, pos);
        }
        geld -= prijs;
    }

    public void betaalGeld(int betalen) {
        geld -= betalen;
    }
}
