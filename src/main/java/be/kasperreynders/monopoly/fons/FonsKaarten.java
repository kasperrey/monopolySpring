package be.kasperreynders.monopoly.fons;

import be.kasperreynders.monopoly.spel.Bord;
import be.kasperreynders.monopoly.spel.LoaderJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FonsKaarten {
    private final ArrayList<FonsKaart> fonsKaarten;
    private final Random random = new Random();

    public FonsKaarten(Bord bord) {
        try {
            fonsKaarten = LoaderJson.leesFons("bord data/kans fons/", bord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FonsKaart getRandomFonsKaart() {
        return fonsKaarten.get(random.nextInt(0, fonsKaarten.size()));
    }
}
