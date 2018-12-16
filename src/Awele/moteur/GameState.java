package Awele.moteur;

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
