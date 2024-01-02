package be.kasperreynders.monopoly.spel;

import java.util.Optional;

public record TreinKaart(int prijs, int huur, int pos, Optional<Speler> bezitter, int[] huures) {
    public TreinKaart setBezet(Optional<Speler> speler) {
        return new TreinKaart(prijs(), huur(), pos(), speler, huures());
    }

    public TreinKaart setHuur(int huur) {
        return new TreinKaart(prijs(), huur, pos(), bezitter(), huures());
    }
}
