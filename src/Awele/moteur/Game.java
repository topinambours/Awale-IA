package Awele.moteur;


import java.util.Scanner;

public class Game {
    private int[] blackSeeds;
    private int[] redSeeds;
    private int specialSeed;
    private int score1;
    private int score2;
    private int range;

    public Game() {

        score1 = 0;
        score2 = 0;

        for (int i = 1; i <=12;i++){
            blackSeeds[i] = 3;
            redSeeds[i] = 3;
        }

        init();

    }

    public boolean init(){
        System.out.printf("Initialisation de la partie...\n");
        System.out.printf("Quel est le joueur qui commence en premier ? [robot|player]\n");
        Scanner in = new Scanner(System.in);
        String res = in.nextLine();

        System.out.println(res);

        if(res.equalsIgnoreCase("robot")){
            range = 6;
            return true;
        }else if(res.equalsIgnoreCase("player")){
            range = 0;
            return false;
        }else{
            return  init();
        }
    }

}



