package Awele.moteur;

import java.util.ArrayList;

public class Plateau {
    private ArrayList<Hole> holes;



    public Plateau() {
        holes = new ArrayList<Hole>();

        for (int i = 1; i <=12;i++){
            holes.add(new Hole(1));
        }

    }


}
