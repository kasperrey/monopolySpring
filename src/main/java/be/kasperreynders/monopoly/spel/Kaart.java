package be.kasperreynders.monopoly.spel;

import java.util.Optional;

public record Kaart(int prijs, int huur, int pos, Optional<Speler> bezitter, int[] huures, String kleur, int huisjes, int bouwPrijs) {
    public Kaart setBezet(Optional<Speler> speler) {
        return new Kaart(prijs(), huur(), pos(), speler, huures(), kleur(), huisjes(), bouwPrijs());
    }

    public Kaart setHuur(int huur) {
        return new Kaart(prijs(), huur, pos(), bezitter(), huures(), kleur(), huisjes(), bouwPrijs());
    }

    public Kaart setHuisjes(int huisjes) {
        return new Kaart(prijs(), huur(), pos(), bezitter(), huures(), kleur(), huisjes, bouwPrijs());
    }
}
