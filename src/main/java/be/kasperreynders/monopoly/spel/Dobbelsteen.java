package be.kasperreynders.monopoly.spel;

import java.util.Random;

public class Dobbelsteen {
    private final Random random;

    public Dobbelsteen() {
        this.random = new Random();
    }

    private int dobbelen() {
        return random.nextInt(6) + 1;
    }

    public int[] dobbelenMet2() {
        return new int[] {dobbelen(), dobbelen()};
    }
}
