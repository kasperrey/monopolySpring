package be.kasperreynders.monopoly.kans;

import be.kasperreynders.monopoly.spel.Bord;
import be.kasperreynders.monopoly.spel.LoaderJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class KansKaarten {
    private final ArrayList<KansKaart> kansKaarten;
    private final Random random = new Random();

    public KansKaarten(Bord bord) {
        try {
            kansKaarten = LoaderJson.leesKans("bord data/kans fons/", bord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KansKaart getRandomKansKaart() {
        return kansKaarten.get(random.nextInt(0, kansKaarten.size()));
    }
}
