package Awale;

import Awale.moteur.AdvancedOware;
import Awale.moteur.GameState;
import Awale.moteur.Move;

public class Main {

    public static void main(String[] args){
        AdvancedOware game = new AdvancedOware();
        game.play();
        /*GameState test = new GameState(new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0});
        GameState res = test.applyMove(new Move(1, false, 18), 1, true, new Move(0, false, 0));
        System.out.println(res);*/
    }

}
