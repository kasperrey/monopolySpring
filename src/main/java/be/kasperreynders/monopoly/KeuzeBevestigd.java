package be.kasperreynders.monopoly;

import java.util.ArrayList;

public record KeuzeBevestigd(ArrayList<Integer> gekocht, ArrayList<Integer[]> huisjesGekocht,
                             ArrayList<Integer[]> huisjesVerkocht, boolean gebruikVrij, boolean koopVrij,
                             ArrayList<Integer> verkocht, boolean gooiDubbel) {
}
