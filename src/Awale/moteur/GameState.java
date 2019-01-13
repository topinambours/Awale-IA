package Awale.moteur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState {
    int[] redSeeds;
    int[] blackSeeds;
    int[] specialSeeds;
    int score1;
    int score2;

    public GameState() {
        redSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        blackSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        specialSeeds = new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        score1 = 0;
        score2 = 0;
    }

    GameState(int[] specialSeeds){

        redSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        blackSeeds = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};

        //redSeeds = new int[]{3,3,3,3,3,0,2,2,2,2,2,3};
        //blackSeeds = new int[]{3,3,3,3,3,0,2,2,2,2,2,3};

        this.specialSeeds = specialSeeds;
        score1 = 0;
        score2 = 0;
    }

    GameState(int[] redSeeds, int[] blackSeeds, int[] specialSeeds, int score1, int score2) {
        this.redSeeds = redSeeds;
        this.blackSeeds = blackSeeds;
        this.specialSeeds = specialSeeds;
        this.score1 = score1;
        this.score2 = score2;
    }

    List<Move> legalMoves(int playerNo) {
        ArrayList<Move> res = new ArrayList<>();
        int offset = 6 * (playerNo - 1);

        for (int i = offset; i < offset + 6; i++) {
            if (redSeeds[i] > 0) {
                if (specialSeeds[i] > 0) {
                    for (int j = redSeeds[i] + blackSeeds[i]; j >= 1; j--) {
                        res.add(new Move(i, true, j));
                    }
                } else {
                    res.add(new Move(i, true, 0));
                }
            }
            if (blackSeeds[i] > 0) {
                if (specialSeeds[i] > 0) {
                    for (int j = redSeeds[i] + blackSeeds[i]; j >= 1; j--) {
                        res.add(new Move(i, false, j));
                    }
                } else {
                    res.add(new Move(i, false, 0));
                }
            }
            if (redSeeds[i] == 0 && blackSeeds[i] == 0 && specialSeeds[i] > 0) {
                res.add(new Move(i, true, 1));
                res.add(new Move(i, false, 1));
            }
        }

        return res;
    }

    GameState applyMove(Move move, int playerNo, boolean print) {
        int tracker = 1;
        int skipped = 0;

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
                    if (tracker + skipped >= move.posSpecial && remainingSpecialSeeds > 0) {
                        newSpecialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newRedSeeds[pos]++;
                    numRedSeeds--;

                } else skipped++;
                tracker++;
            }
            while (numBlackSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (tracker + skipped >= move.posSpecial && remainingSpecialSeeds > 0) {
                        newSpecialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newBlackSeeds[pos]++;
                    numBlackSeeds--;

                } else skipped++;
                tracker++;
            }
        } else {
            while (numBlackSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (tracker + skipped >= move.posSpecial && remainingSpecialSeeds > 0) {
                        newSpecialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newBlackSeeds[pos]++;
                    numBlackSeeds--;

                } else skipped++;
                tracker++;
            }
            while (numRedSeeds > 0) {
                pos = (move.position + tracker) % 12;
                if (pos != move.position) {
                    if (tracker + skipped >= move.posSpecial && remainingSpecialSeeds > 0) {
                        newSpecialSeeds[pos]++;
                        remainingSpecialSeeds--;
                        tracker++;
                        continue;
                    }
                    newRedSeeds[pos]++;
                    numRedSeeds--;

                } else skipped++;
                tracker++;
            }
        }
        while (remainingSpecialSeeds > 0) {
            pos = (move.position + tracker) % 12;
            if (pos != move.position) {
                if (tracker >= move.posSpecial) {
                    newSpecialSeeds[pos]++;
                    remainingSpecialSeeds--;

                }
            }
            tracker++;
        }
        newRedSeeds[move.position] = 0;
        newBlackSeeds[move.position] = 0;
        newSpecialSeeds[move.position] = 0;

        if (print) {
            System.out.printf("Player %d plays move %s with %d seeds\n", playerNo, move.toString(), redSeeds[move.position] + blackSeeds[move.position] + specialSeeds[move.position]);
        }

        //CAPTURE BEGINS
        Color lastColor = getLastColor(move);

        boolean fail = false;
        int i = pos - move.position;
        if (i < 0) i += 12;
        int count = 0;
        while (!fail && (i >= 0)) {
            if (playerNo == 1 && Math.floorMod((move.position + i), 12) < 6) {
                break;
            } else if (playerNo == 2 && Math.floorMod((move.position + i), 12) > 5) {
                break;
            }
            int capturePos = Math.floorMod((move.position + i), 12);
            int hole;
            if (lastColor == Color.BLACK) {
                hole = newBlackSeeds[capturePos] + newSpecialSeeds[capturePos];
                if (hole == 2 || hole == 3) {
                    int newcap = 0;
                    newcap += newBlackSeeds[capturePos] + newSpecialSeeds[capturePos];
                    newBlackSeeds[capturePos] = 0;
                    newSpecialSeeds[capturePos] = 0;
                    if (newcap > 0 && print) System.out.printf("Player %d captures %d black seeds from hole %d\n", playerNo, newcap, capturePos + 1);
                    count += newcap;
                    i--;
                } else fail = true;
            } else if (lastColor == Color.RED){
                hole = newRedSeeds[capturePos] + newSpecialSeeds[capturePos];
                if (hole == 2 || hole == 3) {
                    int newcap = 0;
                    newcap += newRedSeeds[capturePos] + newSpecialSeeds[capturePos];
                    newRedSeeds[capturePos] = 0;
                    newSpecialSeeds[capturePos] = 0;
                    if (newcap > 0 && print) System.out.printf("Player %d captures %d red seeds from hole %d\n", playerNo, newcap, capturePos + 1);
                    count += newcap;
                    i--;
                } else fail = true;
            } else {
                int holeRed = newRedSeeds[capturePos] + newSpecialSeeds[capturePos];
                int holeBlack = newBlackSeeds[capturePos] + newSpecialSeeds[capturePos];
                int newcap = 0;
                boolean redCap = false;
                boolean blackCap = false;
                if (holeRed == 2 || holeRed == 3 || holeBlack == 2 || holeBlack == 3) {
                    newcap += newSpecialSeeds[capturePos];
                    newSpecialSeeds[capturePos] = 0;
                }
                if (holeRed == 2 || holeRed == 3) {
                    newcap += newRedSeeds[capturePos];
                    newRedSeeds[capturePos] = 0;
                    redCap = true;
                }
                if (holeBlack == 2 || holeBlack == 3) {
                    newcap += newBlackSeeds[capturePos];
                    newBlackSeeds[capturePos] = 0;
                    blackCap = true;
                }
                if (newcap > 0 && print) System.out.printf("Player %d captures %d red or black seeds from hole %d\n", playerNo, newcap, capturePos);
                count += newcap;
                i--;
                if (redCap && blackCap) {
                    lastColor = Color.SPECIAL;
                } else if (redCap) {
                    lastColor = Color.RED;
                } else if (blackCap) {
                    lastColor = Color.BLACK;
                } else {
                    fail = true;
                }
            }
        }
        GameState res;
        if (playerNo == 1) res = new GameState(newRedSeeds, newBlackSeeds, newSpecialSeeds, score1 + count, score2);
        else res = new GameState(newRedSeeds, newBlackSeeds, newSpecialSeeds, score1, score2 + count);

        return res;
    }

    int minimax(GameState node, Move bestMove, int depth, int playerNo, boolean maximisingPlayer, int alpha, int beta) {
        // CHECK IF TERMINAL OR LEAF NODE AND EVALUATE IF SO
        if (playerNoMoves(node)) return evalNoMoves(node, playerNo, maximisingPlayer);
        if (depth == 0 || gameOver(node)) return evalNode(node, playerNo, maximisingPlayer);

        // APPLY POSSIBLE MOVES TO CURRENT NODES TO GENERATE LIST OF CHILD NODES
        List<Move> moves = node.legalMoves(playerNo);

        Move garbage = new Move();

        for (Move move : moves) {
            History.save(node);
            node = node.applyMove(move, playerNo, false);
            int score = - minimax(node, garbage, depth - 1, nextPlayer(playerNo), !maximisingPlayer, -beta, -alpha);
            node = History.restore();

            if (score > alpha) {
                alpha = score;
                bestMove.set(move);
            }

            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }

    static boolean gameOver(GameState node) {
        return (node.score1 > 37 || node.score2 > 37 || node.score1 == 37 && node.score2 == 37);
    }

    int evalNode(GameState node, int playerNo, boolean maximisingPlayer) {
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

    static boolean playerNoMoves(GameState node) {
        int acc1 = 0;
        int acc2 = 0;
        for (int i = 0; i <= 5; i++) {
            acc1 += node.redSeeds[i] + node.blackSeeds[i] + node.specialSeeds[i];
            int j = i + 6;
            acc2 += node.redSeeds[j] + node.blackSeeds[j] + node.specialSeeds[j];
        }
        return (acc1 == 0 || acc2 == 0);
    }

    int evalNoMoves(GameState node, int playerNo, boolean maximisingPlayer) {
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

    void captureNoMoves() {
        for (int i = 0; i <= 5; i++) {
            score1 += redSeeds[i] + blackSeeds[i] + specialSeeds[i];
            redSeeds[i] = 0;
            blackSeeds[i] = 0;
            specialSeeds[i] = 0;
            int j = i + 6;
            score2 += redSeeds[j] + blackSeeds[j] + specialSeeds[j];
            redSeeds[j] = 0;
            blackSeeds[j] = 0;
            specialSeeds[j] = 0;
        }
    }

    static int nextPlayer(int playerNo) {
        if (playerNo == 1) return 2;
        else return 1;
    }

    Color getLastColor(Move move) {
        if (move.posSpecial > redSeeds[move.position] + blackSeeds[move.position]) return Color.SPECIAL;
        if (move.redFirst) {
            if (blackSeeds[move.position] > 0) return Color.BLACK;
            else return Color.RED;
        } else {
            if (redSeeds[move.position] > 0) return Color.RED;
            else return Color.BLACK;
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "redSeeds=" + Arrays.toString(redSeeds) +
                ", blackSeeds=" + Arrays.toString(blackSeeds) +
                ", specialSeeds=" + Arrays.toString(specialSeeds) +
                ", score1=" + score1 +
                ", score2=" + score2 +
                '}';
    }
}