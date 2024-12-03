package be.kasperreynders.monopoly.spel;

import be.kasperreynders.monopoly.fons.FonsKaart;
import be.kasperreynders.monopoly.fons.FonsKaarten;
import be.kasperreynders.monopoly.fons.GaNaarFons;
import be.kasperreynders.monopoly.kans.GaNaarKans;
import be.kasperreynders.monopoly.kans.KansKaart;
import be.kasperreynders.monopoly.kans.KansKaarten;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Bord {
    private final ArrayList<Kaart> kaarten;
    private final ArrayList<SpecialeKaart> specialeKaarten;
    private final ArrayList<TreinKaart> treinKaarten;
    private final ArrayList<Tax> taxen;
    private final ArrayList<Speler> spelers = new ArrayList<>();
    private final KansKaarten kansKaarten = new KansKaarten(this);
    private final FonsKaarten fonsKaarten = new FonsKaarten(this);
    private final ArrayList<Integer> posKans;
    private final ArrayList<Integer> posFons;

    public Bord() {
        String map = "bord data/bord/";
        try {
            kaarten = LoaderJson.leesKaarten(map);
            specialeKaarten = LoaderJson.leesSpecialeKaarten(map);
            treinKaarten = LoaderJson.leesTreinKaarten(map);
            taxen = LoaderJson.leesTax(map);
            posKans = LoaderJson.getKansPos(map);
            posFons = LoaderJson.getFonsPos(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSpeler(Speler speler) {
        spelers.add(speler);
    }

    public ArrayList<Speler> getSpelers() {
        return spelers;
    }

    public Optional<Kaart> getKaart(int pos) {
        for (Kaart kaart: kaarten) {
            if (kaart.pos() == pos) {
                return Optional.of(kaart);
            }
        }
        return Optional.empty();
    }

    public Optional<TreinKaart> getTreinKaart(int pos) {
        for (TreinKaart treinKaart: treinKaarten) {
            if (treinKaart.pos() == pos) {
                return Optional.of(treinKaart);
            }
        }
        return Optional.empty();
    }

    public Optional<SpecialeKaart> getSpecialeKaart(int pos) {
        for (SpecialeKaart specialeKaart: specialeKaarten) {
            if (specialeKaart.pos() == pos) {
                return Optional.of(specialeKaart);
            }
        }
        return Optional.empty();
    }

    public void koopSpeciaalBezit(Speler speler, int pos) {
        for (int specialeKaart = 0; specialeKaart < specialeKaarten.size(); specialeKaart++) {
            if (specialeKaarten.get(specialeKaart).pos() == pos) {
                specialeKaarten.set(specialeKaart, specialeKaarten.get(specialeKaart).setBezet(Optional.of(speler)));
            }
        }
        speciaalPrijs();
    }

    public int verkoopSpeciaalBezit(Speler speler) {
        for (int specialeKaart = 0; specialeKaart < specialeKaarten.size(); specialeKaart++) {
            SpecialeKaart kaart = specialeKaarten.get(specialeKaart);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                specialeKaarten.set(specialeKaart, kaart.setBezet(Optional.empty()));
                speciaalPrijs();
                return kaart.prijs();
            }
        }
        return 0;
    }

    public void koopTreinBezit(Speler speler, int pos) {
        for (int treinKaart = 0; treinKaart < treinKaarten.size(); treinKaart++) {
            if (treinKaarten.get(treinKaart).pos() == pos) {
                treinKaarten.set(treinKaart, treinKaarten.get(treinKaart).setBezet(Optional.of(speler)));
            }
        }
        treinPrijs(speler);
    }

    public int verkoopTreinBezit(Speler speler) {
        for (int treinKaart = 0; treinKaart < treinKaarten.size(); treinKaart++) {
            if (treinKaarten.get(treinKaart).bezitter().equals(Optional.of(speler))) {
                treinKaarten.set(treinKaart, treinKaarten.get(treinKaart).setBezet(Optional.empty()));
                return treinKaarten.get(treinKaart).prijs();
            }
        }
        treinPrijs(speler);
        return 0;
    }

    public void koopBezet(Speler speler, int pos) {
        for (int kaart = 0; kaart < kaarten.size(); kaart++) {
            if (kaarten.get(kaart).pos() == pos) {
                kaarten.set(kaart, kaarten.get(kaart).setBezet(Optional.of(speler)));
            }
        }
        kaartPrijs(speler);
    }

    public int verkoopBezet(Speler speler) {
        for (int kaart = 0; kaart < kaarten.size(); kaart++) {
            if (kaarten.get(kaart).bezitter().equals(Optional.of(speler))) {
                kaarten.set(kaart, kaarten.get(kaart).setBezet(Optional.empty()));
                return kaarten.get(kaart).prijs();
            }
        }
        kaartPrijs(speler);
        return 0;
    }

    private int aantalVanSpeler(Speler speler) {
        int aantal = 0;
        for (TreinKaart treinKaart: treinKaarten) {
            if (treinKaart.bezitter().equals(Optional.of(speler))) {
                aantal++;
            }
        }
        return aantal-1;
    }

    private void treinPrijs(Speler speler) {
        int aantal = aantalVanSpeler(speler);
        for (int kaartI = 0; kaartI < treinKaarten.size(); kaartI++) {
            TreinKaart treinKaart = treinKaarten.get(kaartI);
            if (treinKaart.bezitter().equals(Optional.of(speler))) {
                treinKaarten.set(kaartI, treinKaart.setHuur(treinKaart.huures()[aantal]));
            }
        }
    }

    private void speciaalPrijs() {
        boolean allebij = allebij();
        for (int kaartI = 0; kaartI < specialeKaarten.size(); kaartI++) {
            SpecialeKaart specialeKaart = specialeKaarten.get(kaartI);
            if (specialeKaart.bezitter().isPresent()) {
                specialeKaarten.set(kaartI, specialeKaart.setMaal(allebij ? specialeKaart.malers()[1]: specialeKaart.malers()[0]));
            } else {
                specialeKaarten.set(kaartI, specialeKaart.setMaal(specialeKaart.malers()[0]));
            }
        }
    }

    private void kaartPrijs(Speler speler) {
        for (int kaartI = 0; kaartI < kaarten.size(); kaartI++) {
            Kaart kaart = kaarten.get(kaartI);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                if (heeftSpelerStraat(speler, kaart.kleur())) {
                    if (kaart.huisjes() == 0) {
                        kaarten.set(kaartI, kaart.setHuur(kaart.huures()[0] * 2));
                    } else {
                        kaarten.set(kaartI, kaart.setHuur(kaart.huures()[kaart.huisjes()]));
                    }
                } else {
                    kaarten.set(kaartI, kaart.setHuur(kaart.huures()[0]));
                }
            }
        }
    }

    public ArrayList<Kaart> getKaartenBySpeler(Speler speler) {
        ArrayList<Kaart> kaartenVanSpeler = new ArrayList<>();
        for (Kaart kaart: kaarten) {
            if (kaart.bezitter().equals(Optional.of(speler))) {
                kaartenVanSpeler.add(kaart);
            }
        }
        return kaartenVanSpeler;
    }

    public ArrayList<TreinKaart> getTreinKaartenBySpeler(Speler speler) {
        ArrayList<TreinKaart> kaartenVanSpeler = new ArrayList<>();
        for (TreinKaart treinKaart: treinKaarten) {
            if (treinKaart.bezitter().equals(Optional.of(speler))) {
                kaartenVanSpeler.add(treinKaart);
            }
        }
        return kaartenVanSpeler;
    }

    public ArrayList<SpecialeKaart> getSpecialeKaartenBySpeler(Speler speler) {
        ArrayList<SpecialeKaart> specialeKaartenVanSpeler = new ArrayList<>();
        for (SpecialeKaart specialeKaart: specialeKaarten) {
            if (specialeKaart.bezitter().equals(Optional.of(speler))) {
                specialeKaartenVanSpeler.add(specialeKaart);
            }
        }
        return specialeKaartenVanSpeler;
    }

    private boolean allebij() {
        return (specialeKaarten.get(0).bezitter() == specialeKaarten.get(1).bezitter());
    }

    public int betalen(int pos) {
        for (Tax tax: taxen) {
            if (tax.pos() == pos) {
                return tax.betalen();
            }
        }
        return 0;
    }

    public int verkoopBezit(Speler speler) {
        if (!getKaartenMetHuisjesBySpeler(speler).isEmpty()) {
            kaartPrijs(speler);
            return verkoopHuisje(speler);
        } else if (!getKaartenBySpeler(speler).isEmpty()) {
            kaartPrijs(speler);
            return verkoopBezet(speler);
        } else if (!getTreinKaartenBySpeler(speler).isEmpty()) {
            treinPrijs(speler);
            return verkoopTreinBezit(speler);
        } else if (!getSpecialeKaartenBySpeler(speler).isEmpty()) {
            speciaalPrijs();
            return verkoopSpeciaalBezit(speler);
        }
        return 0;
    }

    public boolean heeftSpelerStraat(Speler speler, String kleur) {
        int aantalVanKleur = 0;
        int aantalVanSpeler = 0;
        for (Kaart kaart: kaarten) {
            if (kaart.kleur().equals(kleur)) {
                aantalVanKleur += 1;
                if (kaart.bezitter().equals(Optional.of(speler))) {
                    aantalVanSpeler += 1;
                } else {
                    return false;
                }
            }
        }
        return aantalVanKleur == aantalVanSpeler;
    }

    public int koopHuisje(Speler speler, int geld) {
        ArrayList<Kaart> kaartenBySpeler = getKaartenBySpeler(speler);
        for (Kaart kaart : kaartenBySpeler) {
            if (kaart.bezitter().equals(Optional.of(speler))) {
                if (heeftSpelerStraat(speler, kaart.kleur())) {
                    if (kaart.bouwPrijs() <= geld) {
                        if (kaart.huisjes() < 5) {
                            for (int i = 0; i < kaarten.size(); i++) {
                                if (kaarten.get(i).equals(kaart)) {
                                    kaarten.set(i, kaart.setHuisjes(kaart.huisjes()+1));
                                    return kaart.bouwPrijs();
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public ArrayList<Kaart> getKaartenMetHuisjesBySpeler(Speler speler) {
        ArrayList<Kaart> kaartenMetHuis = new ArrayList<>();
        for (Kaart kaart: kaarten) {
            if (kaart.bezitter().equals(Optional.of(speler))) {
                if (kaart.huisjes() > 0) {
                    kaartenMetHuis.add(kaart);
                }
            }
        }
        return kaartenMetHuis;
    }

    private int verkoopHuisje(Speler speler) {
        for (int i = 0; i < kaarten.size(); i++) {
            Kaart kaart = kaarten.get(i);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                if (kaart.huisjes() > 0) {
                    kaarten.set(i, kaart.setHuisjes(kaart.huisjes()-1));
                    return kaart.bouwPrijs();
                }
            }
        }
        return 0;
    }

    public String kansEnFons(Speler speler, int pos) {
        if (posKans.contains(pos)) {
            KansKaart kaart = kansKaarten.getRandomKansKaart();
            kaart.voerUit(speler);
            return kaart.getText();
        } else if (posFons.contains(pos)) {
            FonsKaart kaart = fonsKaarten.getRandomFonsKaart();
            kaart.voerUit(speler);
            return kaart.getText();
        }
        return "";
    }

    public int verkoopOpPos(ArrayList<Integer> possen, Speler speler) {
        int krijgen = 0;
        for (int i = 0; i < kaarten.size(); i++) {
            Kaart kaart = kaarten.get(i);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                for (int pos: possen) {
                    if (pos == kaart.pos()) {
                        kaarten.set(i, kaart.setBezet(Optional.empty()));
                        krijgen += kaart.prijs()/2;
                    }
                }
            }
        }
        for (int i = 0; i < specialeKaarten.size(); i++) {
            SpecialeKaart specialeKaart = specialeKaarten.get(i);
            if (specialeKaart.bezitter().equals(Optional.of(speler))) {
                for (int pos: possen) {
                    if (pos == specialeKaart.pos()) {
                        specialeKaarten.set(i, specialeKaart.setBezet(Optional.empty()));
                        krijgen += specialeKaart.prijs()/2;
                    }
                }
            }
        }
        for (int i = 0; i < treinKaarten.size(); i++) {
            TreinKaart treinKaart = treinKaarten.get(i);
            if (treinKaart.bezitter().equals(Optional.of(speler))) {
                for (int pos: possen) {
                    if (pos == treinKaart.pos()) {
                        treinKaarten.set(i, treinKaart.setBezet(Optional.empty()));
                        krijgen += treinKaart.prijs()/2;
                    }
                }
            }
        }
        return krijgen;
    }

    public int verkoopHuisjesOpPos(ArrayList<Integer[]> possen, Speler speler) {
        int krijgen = 0;
        for (int i = 0; i < kaarten.size(); i++) {
            Kaart kaart = kaarten.get(i);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                for (Integer[] huisjes: possen) {
                    if (huisjes[0] == kaart.pos()) {
                        kaarten.set(i, kaart.setHuisjes(kaart.huisjes()-huisjes[1]));
                        krijgen += (kaart.bouwPrijs()/2)*huisjes[1];
                    }
                }
            }
        }
        return krijgen;
    }

    public int koopOpPos(ArrayList<Integer> possen, Speler speler) {
        int betalen = 0;
        for (int i = 0; i < kaarten.size(); i++) {
            Kaart kaart = kaarten.get(i);
            for (int pos: possen) {
                if (pos == kaart.pos()) {
                    kaarten.set(i, kaart.setBezet(Optional.of(speler)));
                    betalen += kaart.prijs();
                }
            }
        }
        for (int i = 0; i < specialeKaarten.size(); i++) {
            SpecialeKaart specialeKaart = specialeKaarten.get(i);
            for (int pos: possen) {
                if (pos == specialeKaart.pos()) {
                    specialeKaarten.set(i, specialeKaart.setBezet(Optional.of(speler)));
                    betalen += specialeKaart.prijs();
                }
            }
        }
        for (int i = 0; i < treinKaarten.size(); i++) {
            TreinKaart treinKaart = treinKaarten.get(i);
            for (int pos: possen) {
                if (pos == treinKaart.pos()) {
                    treinKaarten.set(i, treinKaart.setBezet(Optional.of(speler)));
                    betalen += treinKaart.prijs();
                }
            }
        }
        return betalen;
    }

    public int koopHuisjesOpPos(ArrayList<Integer[]> possen, Speler speler) {
        int betalen = 0;
        for (int i = 0; i < kaarten.size(); i++) {
            Kaart kaart = kaarten.get(i);
            if (kaart.bezitter().equals(Optional.of(speler))) {
                for (Integer[] huisjes: possen) {
                    if (huisjes[0] == kaart.pos()) {
                        kaarten.set(i, kaart.setHuisjes(kaart.huisjes()+huisjes[1]));
                        betalen += kaart.bouwPrijs()*huisjes[1];
                    }
                }
            }
        }
        return betalen;
    }

    public int betaalHuur(int pos, int stappen) {
        Optional<Kaart> kaartOptional = getKaart(pos);
        Optional<TreinKaart> treinKaartOptional = getTreinKaart(pos);
        Optional<SpecialeKaart> specialeKaartOptional = getSpecialeKaart(pos);
        if (kaartOptional.isPresent()) {
            Kaart kaart = kaartOptional.get();
            if (kaart.bezitter().isPresent()) {
                Speler bezitter = kaart.bezitter().get();
                if (heeftSpelerStraat(bezitter, kaart.kleur())) {
                    bezitter.addGeld(kaart.huur()*2);
                    return kaart.huur()*2;
                } else {
                    bezitter.addGeld(kaart.huur());
                    return kaart.huur();
                }
            }
        } else if (treinKaartOptional.isPresent()) {
            TreinKaart treinKaart = treinKaartOptional.get();
            if (treinKaart.bezitter().isPresent()) {
                Speler bezitter = treinKaart.bezitter().get();
                int aantal = (int) treinKaarten.stream().filter(treinKaart1 -> treinKaart1.bezitter().isPresent()).filter(treinKaart1 -> treinKaart1.bezitter().get().equals(bezitter)).count();
                bezitter.addGeld(treinKaart.huures()[aantal-1]);
                return treinKaart.huures()[aantal-1];
            }
        } else if (specialeKaartOptional.isPresent()) {
            SpecialeKaart specialeKaart = specialeKaartOptional.get();
            if (specialeKaart.bezitter().isPresent()) {
                Speler bezitter = specialeKaart.bezitter().get();
                if (allebij()) {
                    bezitter.addGeld(specialeKaart.malers()[1]*stappen);
                    return specialeKaart.malers()[1]*stappen;
                } else {
                    bezitter.addGeld(specialeKaart.maal()*stappen);
                    return specialeKaart.maal()*stappen;
                }
            }
        }
        return 0;
    }
}
