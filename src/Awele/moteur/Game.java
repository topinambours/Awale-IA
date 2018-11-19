package Awele.moteur;


public class Game {
    private int[] blackHoles;
    private int[] redHoles;
    private int specialHole;
    private int score1;
    private int score2;



    public Game() {

        score1 = 0;
        score2 = 0;

        for (int i = 1; i <=12;i++){
            blackHoles[i] = 3;
            redHoles[i] = 3;
        }
    }



    public void vider(int id, int special, String couleur){
        if (couleur == "black"){
            int i = 1;
            while (blackHoles[id] > 0){
                int index = id;
                if index 
                blackHoles[id+1] += 1;
            }

            vider(id,special,"rouge");
        }else{

        }

    }


}
