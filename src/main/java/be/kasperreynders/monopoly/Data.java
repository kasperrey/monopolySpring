package be.kasperreynders.monopoly;

import java.util.ArrayList;

public record Data(int id, String textKansFons,
                   int posSpeler1, ArrayList<Integer> kaartBezittenSpeler1, int geldSpeler1, ArrayList<int[]> huisjesSpeler1,
                   int posSpeler2, ArrayList<Integer> kaartBezittenSpeler2, int geldSpeler2, ArrayList<int[]> huisjesSpeler2,
                   int posSpeler3, ArrayList<Integer> kaartBezittenSpeler3, int geldSpeler3, ArrayList<int[]> huisjesSpeler3) {
}
