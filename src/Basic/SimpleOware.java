package Basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SimpleOware {
    public static void main(String[] args) {

        play(true);
    }

    static void play(boolean computerStart) {
        GameState game = new GameState();

        boolean robotPlay = computerStart;
        int currentPlayer = 1;
        MinimaxResult nextMove;

        while(!GameState.gameOver(game, currentPlayer)) {
            if (robotPlay) {
                System.out.println(Arrays.toString(game.legalMoves(currentPlayer).toArray()));
                nextMove = game.minimax(game, 6, currentPlayer, true);
            } else {
                System.out.println(game.toString());
                System.out.printf("Taper le numéro de la cellule à jouer:\n");
                Scanner in = new Scanner(System.in);
                nextMove = new MinimaxResult(0, in.nextInt());
            }

            game = game.applyMove(nextMove.position, currentPlayer, true);
            currentPlayer = GameState.nextPlayer(currentPlayer);
            robotPlay = !robotPlay;

        }

        System.out.printf("Partie terminée! Score joueur 1: %d, score joueur 2: %d\n", game.score1, game.score2);
    }
}

class GameState {
    public int[] seeds;
    public int score1;
    public int score2;
    public int lastMove;

    public GameState() {
        seeds = new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        score1 = 0;
        score2 = 0;
        lastMove = -1;
    }

    private GameState(int[] seeds, int score1, int score2, int lastMove) {
        this.seeds = seeds;
        this.score1 = score1;
        this.score2 = score2;
        this.lastMove = lastMove;
    }

    public List<Integer> legalMoves(int playerNo) {
        ArrayList<Integer> res = new ArrayList<>();
        int offset = 6 * (playerNo - 1);

        for (int i = 0; i < 6; i++) {
            if (seeds[i + offset] != 0) res.add(i + offset);
        }

        return res;
    }

    public GameState applyMove(int move, int playerNo, boolean print) {
        int lastPos;
        int tracker = 1;

        int[] newSeeds = seeds.clone();

        int noSeeds = newSeeds[move];
        int pos = 0;
        while (noSeeds > 0) {
            pos = (move + tracker) % 12;
            if (pos != move) {
                newSeeds[pos]++;
                noSeeds--;
            }
            tracker++;
        }
        newSeeds[move] = 0;
        lastPos = pos;

        if (print) System.out.printf("Player %d plays from hole %d with %d seeds\n", playerNo, move, seeds[move]);
        GameState res;
        res = capture(newSeeds, lastPos, move, playerNo, print);

        return res;
    }

    public GameState capture(int[] seeds, int lastPos, int firstPos, int playerNo, boolean print) {
        boolean fail = false;
        int i = lastPos - firstPos;
        if (i < 0) i += 12;
        int count = 0;
        while (fail != true && (i >= 0)) {
            if (playerNo == 1 && (firstPos + i) % 12 < 6) {
                fail = true;
                break;
            } else if (playerNo == 2 && (firstPos + i) % 12 > 5) {
                fail = true;
                break;
            }
            if (seeds[(firstPos + i) % 12] == 2 || seeds[(firstPos + i) % 12] == 3) {
                count += seeds[i];
                seeds[(firstPos + i) % 12] = 0;
                if (count > 0 && print) System.out.printf("Player %d captures %d seeds from hole %d\n", playerNo, count, i);
                i--;
            } else fail = true;
        }
        GameState res;
        if (playerNo == 1) res = new GameState(seeds, score1 + count, score2, firstPos);
        else res = new GameState(seeds, score1, score2 + count, firstPos);
        return res;
    }

        public MinimaxResult minimax(GameState node, int depth, int playerNo, boolean maximisingPlayer) {
            if (depth == 0 || gameOver(node, playerNo)) return new MinimaxResult(evalNode(node, playerNo), node.lastMove);
            List<GameState> newNodes  = new ArrayList<>();
            List<Integer> moves = node.legalMoves(playerNo);
            for (int move : moves) {
                newNodes.add(applyMove(move, playerNo, false));
            }
            List<MinimaxResult> moveResults = new ArrayList<>();
        for (GameState nextNode : newNodes) {
            moveResults.add(minimax(nextNode, depth - 1, nextPlayer(playerNo), !maximisingPlayer));
        }

        MinimaxResult res = new MinimaxResult(0, moves.get(0));
        int val;
        int oldval;
        if (maximisingPlayer) {
            val = -10000;
            oldval = val;
            for (int i = 0; i < moveResults.size(); i++) {
                oldval = val;
                val = Math.max(val, moveResults.get(i).valeur);
                if (val > oldval) res = moveResults.get(i);
            }
        } else {
            val = 10000;
            oldval = val;
            for (int i = 0; i < moveResults.size(); i++) {
                oldval = val;
                val = Math.min(val, moveResults.get(i).valeur);
                if (val < oldval) res = moveResults.get(i);
            }
        }

        return res;
    }

    public int evalNode(GameState node, int playerNo) {
        if (playerNo == 1) {
            if (node.score1 >= 25) return 100;
            else return node.score1 - score1;
        } else {
            if (node.score2 >= 25) return -100;
            else return -(node.score2 - score2);
        }
    }

    public static boolean gameOver(GameState node, int playerNo) {
        return (node.score1 >= 25 || node.score2 >= 25 || playerNoMoves(node, playerNo));
    }

    public static boolean playerNoMoves(GameState node, int playerNo) {
        int offset = 0;
        if (playerNo == 2) offset = 6;
        int acc = 0;
        for (int i = offset; i < 6 + offset; i++) {
            acc += node.seeds[i];
        }
        return (acc == 0);
    }

    public static int nextPlayer(int playerNo) {
        if (playerNo == 1) return 2;
        else return 1;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "seeds=" + Arrays.toString(seeds) +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", lastMove=" + lastMove +
                '}';
    }
}

class MinimaxResult {
    public int valeur;
    public int position;

    public MinimaxResult(int valeur, int position) {
        this.valeur = valeur;
        this.position = position;
    }
}