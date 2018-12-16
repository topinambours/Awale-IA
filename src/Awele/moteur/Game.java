package Awele.moteur;


import java.util.Scanner;

public class Game {
    private static final int NB_HOLE = 12;
    private int[] blackSeeds;
    private int[] redSeeds;
    private int specialSeed;
    private int score1;
    private int score2;
    private int range;

    public Game() {

        score1 = 0;
        score2 = 0;
        blackSeeds = new int[NB_HOLE];
        redSeeds = new int[NB_HOLE];

        for (int i = 0; i <12;i++){
            blackSeeds[i] = 3;
            redSeeds[i] = 3;
        }

        init();
        nextRequest();

    }

    /**
     * Permet d'initialiser la game
     * @return
     */
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

    /**
     * Permet d'avoir le coups suivant que le joueur souhaite
     * @return
     */
    private Request nextRequest(){

        String res = "";
        Request request = new Request();
        while (!(request.play > range) && (request.play < 12 - range)) {

            res = "";

            while (!res.matches("[0-9]*[a-zA-Z][0-9]*")) {
                System.out.printf("Taper le coups à jouer:\n");
                Scanner in = new Scanner(System.in);
                res = in.nextLine();
            }

            request = new Request(res);

        }
        return request;
    }



}



