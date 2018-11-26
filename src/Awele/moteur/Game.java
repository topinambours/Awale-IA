package Awele.moteur;


public class Game {
    private int[] blackSeeds;
    private int[] redSeeds;
    private int specialSeed;
    private int score1;
    private int score2;

    public Game() {

        score1 = 0;
        score2 = 0;

        for (int i = 1; i <=12;i++){
            blackSeeds[i] = 3;
            redSeeds[i] = 3;
        }
    }

}

class GameState {
    private int[] blackSeeds;
    private int[] redSeeds;
    private int specialSeed;
    private int score1;
    private int score2;

    public GameState() {
        score1 = 0;
        score2 = 0;

        //blackSeeds[12] = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        //redSeeds = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    }
}