package Awale.moteur;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.IntSupplier;


public class AdvancedOware {
    int enemyRange;
    int myRange;

    private final int DEPTH = 8;
    private final int SPECIAL_DEPTH = 6;

    public AdvancedOware() {
    }


    /**
     * Permet de jouer
     */
    public void play() {
        play(init(), specialInits());
    }


    private int getDeph(GameState node){
        int nb_special = 0;
        for(int i = 0; i < node.specialSeeds.length ;i++) {
            if (node.specialSeeds[i] > 0) nb_special++;
        }

        if(nb_special == 0){
            return DEPTH;
        }else{
            return SPECIAL_DEPTH;
        }
    }


    /**
     * play function
     *
     * @param computerStart
     * @param game
     */
    private void play(boolean computerStart, GameState game) {
        System.out.println(game);
        boolean robotPlay = computerStart;
        int currentPlayer = 1;
        int expected;
        Move bestMove = new Move();

        while (!GameState.gameOver(game)) {
            if (GameState.playerNoMoves(game, currentPlayer)) {
                game.captureNoMoves();
                break;
            }
            if (robotPlay) {
                //System.out.println(Arrays.toString(game.legalMoves(currentPlayer).toArray()));
                expected = game.minimax(game, bestMove, getDeph(game), currentPlayer, true, -10000, 10000);
                System.out.printf("expected value : %d\n", expected);
            } else {
                //System.out.println(game.toString());
                bestMove = nextRequest(game);
            }

            game = game.applyMove(bestMove, currentPlayer, true);
            System.out.println(game.toString());
            currentPlayer = GameState.nextPlayer(currentPlayer);
            robotPlay = !robotPlay;

        }

        System.out.printf("Partie terminée! Score joueur 1: %d, score joueur 2: %d\n", game.score1, game.score2);
    }

    /**
     * Permet d'avoir le moove suivant
     *
     * @return
     */
    private Move nextRequest(GameState gameState) {

        String res = "";
        Move request;

        while (!res.matches("[0-9]*[a-zA-Z][0-9]*")) {
            System.out.print("Taper le coup à jouer:\n");
            Scanner in = new Scanner(System.in);
            res = in.nextLine();
        }

        request = new Move(res);

        if (!(gameState.blackSeeds[request.position] + gameState.redSeeds[request.position] + gameState.specialSeeds[request.position] > 0)) {
            System.out.println("[WARNING] Placement illegal");
            return nextRequest(gameState);
        }

        //}
        return request;
    }

    private int scanInt() {
        int res = -1;

        while (res <= 0) {
            Scanner in = new Scanner(System.in);
            res = in.nextInt();
        }

        return res;
    }

    private GameState specialInits() {
        int[] specialSeeds = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < 2; i++) {
            System.out.printf("Quel est la position de la seed numéro %d?\n", i);
            int pos = scanInt();
            //specialSeeds[pos - 1] += 5; //TODO : remettre à 1
            specialSeeds[pos - 1] += 1;
        }

        return new GameState(specialSeeds);
    }

    /**
     * Permet d'initialiser la partie
     *
     * @return
     */
    private boolean init() {
        System.out.print("Initialisation de la partie...\n");
        System.out.print("Quel est le joueur qui commence en premier ? [robot|player]\n");
        Scanner in = new Scanner(System.in);
        String res = in.nextLine();

        System.out.println(res);

        if (res.equalsIgnoreCase("robot")) {
            myRange = 0;
            enemyRange = 6;
            return true;
        } else if (res.equalsIgnoreCase("player")) {
            myRange = 6;
            enemyRange = 0;
            return false;
        } else {
            return init();
        }
    }
}






