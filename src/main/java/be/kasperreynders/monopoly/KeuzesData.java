package be.kasperreynders.monopoly;

import java.util.ArrayList;

public record KeuzesData(ArrayList<Integer> koopKeuzes, boolean inGevangenis, boolean magVrij, int verkopen, int posSpeler, int geld, String textKans) { }
