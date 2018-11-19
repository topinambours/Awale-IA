package Awele.moteur;

import java.util.ArrayList;

public class Hole {
    private int id;
    private ArrayList<Seed> seeds;

    public Hole(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public ArrayList<Seed> getSeeds() {
        return seeds;
    }
}
