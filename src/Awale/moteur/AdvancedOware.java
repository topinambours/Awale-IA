package Awale.moteur;



import java.util.Arrays;
import java.util.Scanner;


public class AdvancedOware {
    int enemyRange;
    int myRange;

    public AdvancedOware() {
    }


    /**
     * Permet de jouer
     */
    public void play(){
        play(init(),specialInits());
    }


    /**
     * play function
     * @param computerStart
     * @param game
     */
    public void play(boolean computerStart,GameState game) {
        System.out.println(game);
        boolean robotPlay = computerStart;
        int currentPlayer = 1;
        MinimaxResult nextMove;

        while(!GameState.gameOver(game)) {
            if (GameState.playerNoMoves(game)) {
                game.captureNoMoves();
                break;
            }
            if (robotPlay) {
                System.out.println(Arrays.toString(game.legalMoves(currentPlayer).toArray()));
                nextMove = game.minimax(game, 3, currentPlayer, true, true, -10000, 10000);
                System.out.printf("expected value : %d\n", nextMove.valeur);
            } else {
                System.out.println(game.toString());

                Move request = nextRequest(game);

                nextMove = new MinimaxResult(0, request);
            }

            game = game.applyMove(nextMove.position, currentPlayer, true, null);
            currentPlayer = GameState.nextPlayer(currentPlayer);
            robotPlay = !robotPlay;

        }

        System.out.printf("Partie terminée! Score joueur 1: %d, score joueur 2: %d\n", game.score1, game.score2);
    }

    /**
     * Permet d'avoir le moove suivant
     * @return
     */
    private Move nextRequest(GameState gameState){

        String res = "";
        Move request = new Move();
        //while (!(request.position > enemyRange) && (request.position < 12 - enemyRange)) {

            res = "";

            while (!res.matches("[0-9]*[a-zA-Z][0-9]*")) {
                System.out.printf("Taper le coup à jouer:\n");
                Scanner in = new Scanner(System.in);
                res = in.nextLine();
            }

            request = new Move(res);

            if(!(gameState.blackSeeds[request.position] + gameState.redSeeds[request.position] + gameState.specialSeeds[request.position] > 0)){
                System.out.println("[WARNING] Placement illegal");
                return nextRequest(gameState);
            }

        //}
        return request;
    }

    private int scanInt(){
        int res = -1;

        while (res <= 0){
            Scanner in = new Scanner(System.in);
            res = in.nextInt();
        }

        return res;
    }

    private GameState specialInits(){
        int[] specialSeeds = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < 2; i++){
            System.out.printf("Quel est la position de la seed numéro %d?\n",i);
            int pos = scanInt();
            specialSeeds[pos - 1] += 1;
        }

        return new GameState(specialSeeds);
    }

    /**
     * Permet d'initialiser la partie
     * @return
     */
    private boolean init(){
        System.out.printf("Initialisation de la partie...\n");
        System.out.printf("Quel est le joueur qui commence en premier ? [robot|player]\n");
        Scanner in = new Scanner(System.in);
        String res = in.nextLine();

        System.out.println(res);

        if(res.equalsIgnoreCase("robot")){
            myRange = 0;
            enemyRange = 6;
            return true;
        }else if(res.equalsIgnoreCase("player")){
            myRange = 6;
            enemyRange = 0;
            return false;
        }else{
            return  init();
        }
    }
}






