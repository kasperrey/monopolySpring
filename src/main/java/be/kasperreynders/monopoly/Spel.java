package be.kasperreynders.monopoly;

import be.kasperreynders.monopoly.spel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Spel {
    private final Dobbelsteen dobbelsteen = new Dobbelsteen();
    private final Random random = new Random();
    private final Speler speler1;
    private final Speler speler2;
    private final Speler speler3;
    private final Bord bord  = new Bord();
    private int aanBeurd = 0;
    public final int id;
    private LocalDate date = LocalDate.now();
    private String text = "";
    private int[] gedobbelt;

    public Spel(int id) {
        this.id = id;
        this.speler1 = new Speler(1500, bord);
        this.speler2 = new Speler(1500, bord);
        this.speler3 = new Speler(1500, bord);
        bord.addSpeler(speler1);
        bord.addSpeler(speler2);
        bord.addSpeler(speler3);
    }

    public void zet(KeuzeBevestigd keuzeBevestigd) {
        if (aanBeurd == 0) {
            speler1.doeKeuzes(keuzeBevestigd, gedobbelt);
        } else if (aanBeurd == 1) {
            speler2.doeKeuzes(keuzeBevestigd, gedobbelt);
        } else if (aanBeurd == 2) {
            speler3.doeKeuzes(keuzeBevestigd, gedobbelt);
            aanBeurd = -1;
        }
        aanBeurd += 1;
        date = LocalDate.now();
    }

    public KeuzesData keuzes() {
        gedobbelt = dobbelsteen.dobbelenMet2();
        KeuzesData keuzesData = null;
        if (aanBeurd == 0) {
            keuzesData = speler1.keuzes(gedobbelt);
        } else if (aanBeurd == 1) {
            keuzesData = speler2.keuzes(gedobbelt);
        } else if (aanBeurd == 2) {
            keuzesData = speler3.keuzes(gedobbelt);
        }
        date = LocalDate.now();
        return keuzesData;
    }

    public Data getData() {
        return new Data(id, text, speler1.getPos(), getKaartenInBezit(speler1), speler1.getGeld(), getHuisjes(speler1),
                speler2.getPos(), getKaartenInBezit(speler2), speler2.getGeld(), getHuisjes(speler2),
                speler3.getPos(), getKaartenInBezit(speler3), speler3.getGeld(), getHuisjes(speler3));
    }

    private ArrayList<Integer> getKaartenInBezit(Speler speler) {
        ArrayList<Integer> kaartenPossen = new ArrayList<>();
        for (Kaart kaart: bord.getKaartenBySpeler(speler)) {
            kaartenPossen.add(kaart.pos());
        }
        for (TreinKaart treinKaart: bord.getTreinKaartenBySpeler(speler)) {
            kaartenPossen.add(treinKaart.pos());
        }
        for (SpecialeKaart specialeKaart: bord.getSpecialeKaartenBySpeler(speler)) {
            kaartenPossen.add(specialeKaart.pos());
        }
        return kaartenPossen;
    }

    private ArrayList<int[]> getHuisjes(Speler speler) {
        ArrayList<int[]> list = new ArrayList<>();
        for (Kaart kaart: bord.getKaartenMetHuisjesBySpeler(speler)) {
            list.add(new int[] {kaart.pos(), kaart.huisjes()});
        }
        return list;
    }

    public LocalDate laatsteZet() {
        return date;
    }
}
