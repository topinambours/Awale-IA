package Advanced.moteur;


import Awele.moteur.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameState {
    public int[] redSeeds;
    public int[] blackSeeds;
    public int[] specialSeeds;
    public int score1;
    public int score2;
    public Move rootMove;

    public GameState() {
        redSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        blackSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        specialSeeds = new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        score1 = 0;
        score2 = 0;
        rootMove = null;
    }

    public GameState(int[] specialSeeds){
        redSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        blackSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        this.specialSeeds = specialSeeds;
        score1 = 0;
        score2 = 0;
        rootMove = null;
    }

    public GameState(int[] redSeeds, int[] blackSeeds, int[] specialSeeds, int score1, int score2, Move rootMove) {
        this.redSeeds = redSeeds;
        this.blackSeeds = blackSeeds;
        this.specialSeeds = specialSeeds;
        this.score1 = score1;
        this.score2 = score2;
        this.rootMove = rootMove;
    }

    public List<Move> legalMoves(int playerNo) {
        ArrayList<Move> res = new ArrayList<>();
        int offset = 6 * (playerNo - 1);

        for (int i = offset; i < offset + 6; i++) {
            if (redSeeds[i] > 0) {
                if (specialSeeds[i] > 0) {
                    for (int j = 1; j <= redSeeds[i] + blackSeeds[i]; j++) {
                        res.add(new Move(i, true, j));
                    }
                } else {
                    res.add(new Move(i, true, -1));
                }
            }
            if (blackSeeds[i] > 0) {
                if (specialSeeds[i] > 0) {
                    for (int j = 1; j <= redSeeds[i] + blackSeeds[i]; j++) {
                        res.add(new Move(i, false, j));
                    }
                } else {
                    res.add(new Move(i, false, -1));
                }
            }
            if (redSeeds[i] == 0 && blackSeeds[i] == 0 && specialSeeds[i] > 0) {
                res.add(new Move(i, true, 1));
                res.add(new Move(i, false, 1));
            }
        }

        return res;
    }

    public GameState applyMove(Move move, int playerNo, boolean print) {
        int lastPos;
        int tracker = 1;

        int[] newRedSeeds = redSeeds.clone();
        int[] newBlackSeeds = blackSeeds.clone();
        int[] newSpecialSeeds = specialSeeds.clone();

        int numRedSeeds = newRedSeeds[move.position];
        int numBlackSeeds = newBlackSeeds[move.position];
        int numSpecialSeeds = newSpecialSeeds[move.position];
        int pos = 0;
        int remainingSpecialSeeds = numSpecialSeeds;


        if (move.redFirst) {
            while (numRedSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (pos >= move.posSpecial + move.position - 1 && remainingSpecialSeeds > 0) {
                        specialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newRedSeeds[pos]++;
                    numRedSeeds--;
                    tracker++;
                }
            }
            while (numBlackSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (pos >= move.posSpecial + move.position - 1 && remainingSpecialSeeds > 0) {
                        specialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newBlackSeeds[pos]++;
                    numBlackSeeds--;
                    tracker++;
                }
            }
        } else {
            while (numBlackSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (pos >= move.posSpecial + move.position - 1 && remainingSpecialSeeds > 0) {
                        specialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newBlackSeeds[pos]++;
                    numBlackSeeds--;
                    tracker++;
                }
            }
            while (numRedSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (pos >= move.posSpecial + move.position - 1 && remainingSpecialSeeds > 0) {
                        specialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newRedSeeds[pos]++;
                    numRedSeeds--;
                    tracker++;
                }
            }
        }
        newRedSeeds[move.position] = 0;
        newBlackSeeds[move.position] = 0;
        newSpecialSeeds[move.position] = 0;
        lastPos = pos;

        if (print) {
            System.out.printf("Player %d plays from hole %d with %d seeds\n", playerNo, move.position, redSeeds[move.position] + blackSeeds[move.position] + specialSeeds[move.position]);
        }
        GameState res;
        res = capture(redSeeds, blackSeeds, specialSeeds, lastPos, move.position, playerNo, move.redFirst, print);

        return res;
    }

    public GameState capture(int[] redSeeds, int[] blackSeeds, int[] specialSeeds, int lastPos, int firstPos, int playerNo, boolean redFirst, boolean print) {
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
            int pos = (firstPos + i) % 12;
            int hole;
            if (redFirst) {
                hole = blackSeeds[pos] + specialSeeds[pos];
                if (hole == 2 || hole == 3) {
                    count += blackSeeds[pos] + specialSeeds[pos];
                    blackSeeds[pos] = 0;
                    specialSeeds[pos] = 0;
                    if (count > 0 && print) System.out.printf("Player %d captures %d black seeds from hole %d\n", playerNo, count, pos);
                    i--;
                } else fail = true;
            } else {
                hole = redSeeds[pos] + specialSeeds[pos];
                if (hole == 2 || hole == 3) {
                    count += redSeeds[pos] + specialSeeds[pos];
                    redSeeds[pos] = 0;
                    specialSeeds[pos] = 0;
                    if (count > 0 && print) System.out.printf("Player %d captures %d red seeds from hole %d\n", playerNo, count, pos);
                    i--;
                } else fail = true;
            }
        }
        GameState res;
        if (playerNo == 1) res = new GameState(redSeeds, blackSeeds, specialSeeds, score1 + count, score2, this.rootMove);
        else res = new GameState(redSeeds, blackSeeds, specialSeeds, score1, score2 + count, this.rootMove);
        return res;
    }

    public MinimaxResult minimax(GameState node, int depth, int playerNo, boolean maximisingPlayer, boolean first) {
        if (playerNoMoves(node)) return new MinimaxResult(evalNoMoves(node, playerNo, maximisingPlayer), node.rootMove);
        if (depth == 0 || gameOver(node)) return new MinimaxResult(evalNode(node, playerNo, maximisingPlayer), node.rootMove);
        List<GameState> newNodes = new ArrayList<>();
        List<Move> moves = node.legalMoves(playerNo);
        for (Move move : moves) {
            GameState newNode = applyMove(move, playerNo, false);
            if (first) newNode.setRootMove(move);
            newNodes.add(applyMove(move, playerNo, false));
        }
        List<MinimaxResult> moveResults = new ArrayList<>();
        for (GameState nextNode : newNodes) {
            moveResults.add(minimax(nextNode, depth - 1, nextPlayer(playerNo), !maximisingPlayer, false));
        }

        MinimaxResult res = new MinimaxResult(0, moves.get(0));
        if (maximisingPlayer) {
            int val = -10000;
            for (int i = 0; i < moveResults.size(); i++) {
                MinimaxResult newValue = moveResults.get(i);
                if (newValue.valeur > val) {
                    val = newValue.valeur;
                    res = newValue;
                }
            }
        } else {
            int val = 10000;
            for (int i = 0; i < moveResults.size(); i++) {
                MinimaxResult newValue = moveResults.get(i);
                if (newValue.valeur < val) {
                    val = newValue.valeur;
                    res = newValue;
                }
            }
        }
        return res;
    }

    public void setRootMove(Move rootMove) {
        this.rootMove = rootMove;
    }

    public static boolean gameOver(GameState node) {
        return (node.score1 > 37 || node.score2 > 37 || node.score1 == 37 && node.score2 == 37);
    }

    public int evalNode(GameState node, int playerNo, boolean maximisingPlayer) {
        if (playerNo == 1 && maximisingPlayer || playerNo == 2 && !maximisingPlayer) {
            if (node.score1 >= 38) return 100;
            else if (node.score2 >= 38) return -100;
            else return node.score1 - node.score2;
        } else {
            if (node.score2 >= 38) return 100;
            else if (node.score1 >= 38) return -100;
            else return node.score2 - node.score1;
        }
    }

    public static boolean playerNoMoves(GameState node) {
        int acc1 = 0;
        int acc2 = 0;
        for (int i = 0; i <= 5; i++) {
            acc1 += node.redSeeds[i] + node.blackSeeds[i] + node.specialSeeds[i];
            int j = i + 6;
            acc2 += node.redSeeds[j] + node.blackSeeds[j] + node.specialSeeds[j];
        }
        return (acc1 == 0 || acc2 == 0);
    }

    public int evalNoMoves(GameState node, int playerNo, boolean maximisingPlayer) {
        int score1 = node.score1;
        int score2 = node.score2;

        for (int i = 0; i <= 5; i++) {
            score1 += node.redSeeds[i] + node.blackSeeds[i] + node.specialSeeds[i];
            int j = i + 6;
            score2 += node.redSeeds[j] + node.blackSeeds[j] + node.specialSeeds[j];
        }

        if (playerNo == 1 && maximisingPlayer || playerNo == 2 && !maximisingPlayer) {
            if (score1 >= 38) return 100;
            else if (score2 >= 38) return -100;
            else return score1 - score2;
        } else {
            if (score2 >= 38) return 100;
            else if (score1 >= 38) return -100;
            else return score2 - score1;
        }
    }

    public static int nextPlayer(int playerNo) {
        if (playerNo == 1) return 2;
        else return 1;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "redSeeds=" + Arrays.toString(redSeeds) +
                ", blackSeeds=" + Arrays.toString(blackSeeds) +
                ", specialSeeds=" + Arrays.toString(specialSeeds) +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", rootMove=" + rootMove +
                '}';
    }
}