package Basic;

import Awele.moteur.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleOware {
    public static void main(String[] args) {
        GameState game = new GameState();

        int currentPlayer = 1;
        boolean maximisingPlayer = true;
        int nextMove;
        while (!GameState.gameOver(game, currentPlayer)) {
            System.out.println(Arrays.toString(game.legalMoves(currentPlayer).toArray()));
            nextMove = game.minimax(game, 1, currentPlayer, true);
            game = game.applyMove(nextMove, currentPlayer, true);
            currentPlayer = GameState.nextPlayer(currentPlayer);
            maximisingPlayer = !maximisingPlayer;
        }

        System.out.printf("Partie terminée! Score joueur 1: %d, score joueur 2: %d\n", game.score1, game.score2);
    }
}

class GameState {
    public int[] seeds;
    public int score1;
    public int score2;

    public GameState() {
        seeds = new int[]{4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4};
        score1 = 0;
        score2 = 0;
    }

    private GameState(int[] seeds, int score1, int score2) {
        this.seeds = seeds;
        this.score1 = score1;
        this.score2 = score2;
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
            if (seeds[(firstPos + i) % 12] == 2 || seeds[(firstPos + i) % 12] == 3) {
                count += seeds[i];
                seeds[(firstPos + i) % 12] = 0;
                if (count > 0 && print) System.out.printf("Player %d captures %d seeds from hole %d.", playerNo, count, i);
                i--;
            } else fail = true;
        }
        GameState res;
        if (playerNo == 1) res = new GameState(seeds, score1 + count, score2);
        else res = new GameState(seeds, score1, score2 + count);
        return res;
    }

    /*public int minimax(GameState node, int depth, int playerNo, boolean maximisingPlayer) {
        int value;
        if (depth == 0 || gameOver(node, playerNo)) return evalNode(node, playerNo);

        List<GameState> newNodes = new ArrayList<>();
        List<Integer> moves = node.legalMoves(playerNo);
        for (int move : moves) {
            newNodes.add(applyMove(move, playerNo, false));
        }
        if (maximisingPlayer) {
            value = -100000;
            for (GameState newNode : newNodes) {
                value = Math.max(value, minimax(newNode, depth - 1, nextPlayer(playerNo), false));
            }
            return value;
        } else {
            value = 100000;
            for (GameState newNode : newNodes) {
                value = Math.min(value, minimax(newNode, depth - 1, nextPlayer(playerNo), true));
            }
            return value;
        }
    }*/

    public int minimax(GameState node, int depth, int playerNo, boolean meximisingPlayer) {
        int value;
        if (depth == 0 || gameOver(node, playerNo)) return evalNode(node, playerNo);
        List<GameState> newNodes  = new ArrayList<>();
        List<Integer> moves = node.legalMoves(playerNo)
    }

    public int evalNode(GameState node, int playerNo) {
        if (playerNo == 1) {
            if (node.score1 >= 25) return 1000000;
            else return node.score1 - score1;
        } else {
            if (node.score2 >= 25) return 1000000;
            else return node.score2 - score2;
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
}