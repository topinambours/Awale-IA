package Advanced.moteur;



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
        play(init());
    }


    /**
     * Permet de jouer
     * @param computerStart
     */
    public void play(boolean computerStart) {
        GameState game = new GameState();

        boolean robotPlay = computerStart;
        int currentPlayer = 1;
        MinimaxResult nextMove;

        while(!GameState.gameOver(game)) {
            if (robotPlay) {
                System.out.println(Arrays.toString(game.legalMoves(currentPlayer).toArray()));
                nextMove = game.minimax(game, 6, currentPlayer, true, true);
            } else {
                System.out.println(game.toString());

                Move request = nextRequest();

                nextMove = new MinimaxResult(0, request);
            }

            game = game.applyMove(nextMove.position, currentPlayer, true);
            currentPlayer = GameState.nextPlayer(currentPlayer);
            robotPlay = !robotPlay;

        }

        System.out.printf("Partie terminée! Score joueur 1: %d, score joueur 2: %d\n", game.score1, game.score2);
    }

    /**
     * Permet d'avoir le moove suivant
     * @return
     */
    private Move nextRequest(){

        String res = "";
        Move request = new Move();
        while (!(request.position > enemyRange) && (request.position < 12 - enemyRange)) {

            res = "";

            while (!res.matches("[0-9]*[a-zA-Z][0-9]*")) {
                System.out.printf("Taper le coups à jouer:\n");
                Scanner in = new Scanner(System.in);
                res = in.nextLine();
            }

            request = new Move(res);

        }
        return request;
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






