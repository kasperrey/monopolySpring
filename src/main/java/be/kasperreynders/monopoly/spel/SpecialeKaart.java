package be.kasperreynders.monopoly.spel;

import java.util.Optional;

public record SpecialeKaart(int pos, int prijs, int maal, Optional<Speler> bezitter, int[] malers) {
    public SpecialeKaart setBezet(Optional<Speler> speler) {
        return new SpecialeKaart(pos(), prijs(), maal(), speler, malers());
    }

    public SpecialeKaart setMaal(int maal) {
        return new SpecialeKaart(pos(), prijs(), maal, bezitter(), malers());
    }
}
