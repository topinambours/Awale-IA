package Awale.moteur;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState {
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

        //redSeeds = new int[]{1,8,5,0,0,4,0,0,2,2,7,7};
        //blackSeeds = new int[]{0,9,6,0,1,6,0,2,1,1,5,5};

        //blackSeeds = new int[]{3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1};
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
                    res.add(new Move(i, true, 0));
                }
            }
            if (blackSeeds[i] > 0) {
                if (specialSeeds[i] > 0) {
                    for (int j = 1; j <= redSeeds[i] + blackSeeds[i]; j++) {
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

    public GameState applyMove(Move move, int playerNo, boolean print, Move rootMove) {
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
        int lastPos = pos;

        if (print) {
            System.out.printf("Player %d plays move %s with %d seeds\n", playerNo, move.toString(), redSeeds[move.position] + blackSeeds[move.position] + specialSeeds[move.position]);
        }

        //CAPTURE BEGINS
        Color lastColor = getLastColor(move);

        boolean fail = false;
        int i = lastPos - move.position;
        if (i < 0) i += 12;
        int count = 0;
        while (!fail && (i >= 0)) {
            if (print) System.out.println("tour de boucle");
            if (playerNo == 1 && Math.floorMod((move.position + i), 12) < 6) {
                fail = true;
                break;
            } else if (playerNo == 2 && Math.floorMod((move.position + i), 12) > 5) {
                fail = true;
                break;
            }
            int pos1 = Math.floorMod((move.position + i), 12);
            int hole;
            if (lastColor == Color.BLACK) {
                if (print) System.out.println("we checked black");
                hole = newBlackSeeds[pos1] + newSpecialSeeds[pos1];
                if (hole == 2 || hole == 3) {
                    int newcap = 0;
                    newcap += newBlackSeeds[pos1] + newSpecialSeeds[pos1];
                    newBlackSeeds[pos1] = 0;
                    newSpecialSeeds[pos1] = 0;
                    if (newcap > 0 && print) System.out.printf("Player %d captures %d black seeds from hole %d\n", playerNo, newcap, pos1 + 1);
                    count += newcap;
                    i--;
                } else fail = true;
            } else if (lastColor == Color.RED){
                if (print) System.out.println("we checked red");
                hole = newRedSeeds[pos1] + newSpecialSeeds[pos1];
                if (hole == 2 || hole == 3) {
                    int newcap = 0;
                    newcap += newRedSeeds[pos1] + newSpecialSeeds[pos1];
                    newRedSeeds[pos1] = 0;
                    newSpecialSeeds[pos1] = 0;
                    if (newcap > 0 && print) System.out.printf("Player %d captures %d red seeds from hole %d\n", playerNo, newcap, pos1 + 1);
                    count += newcap;
                    i--;
                } else fail = true;
            } else {
                if (print) System.out.printf("redseeds : " + newRedSeeds[pos1] + "\n");
                if (print) System.out.printf("blackseeds : " + newBlackSeeds[pos1] + "\n");
                if (print) System.out.printf("specialseeds : " + newSpecialSeeds[pos1] + "\n");
                int holeRed = newRedSeeds[pos1] + newSpecialSeeds[pos1];
                if (print) System.out.printf("holered : " + holeRed + "\n");
                int holeBlack = newBlackSeeds[pos1] + newSpecialSeeds[pos1];
                if (print) System.out.printf("holeblack : " + holeBlack + "\n");
                int newcap = 0;
                boolean redCap = false;
                boolean blackCap = false;
                if (holeRed == 2 || holeRed == 3 || holeBlack == 2 || holeBlack == 3) {
                    if (print) System.out.println("we checked special");
                    newcap += newSpecialSeeds[pos1];
                    newSpecialSeeds[pos1] = 0;
                }
                if (holeRed == 2 || holeRed == 3) {
                    newcap += newRedSeeds[pos1];
                    newRedSeeds[pos1] = 0;
                    redCap = true;
                    if (print) System.out.println("redcap is true");
                }
                if (holeBlack == 2 || holeBlack == 3) {
                    newcap += newBlackSeeds[pos1];
                    newBlackSeeds[pos1] = 0;
                    blackCap = true;
                    if (print) System.out.println("blackcap is true");
                }
                if (newcap > 0 && print) System.out.printf("Player %d captures %d red or black seeds from hole %d\n", playerNo, newcap, pos1);
                count += newcap;
                i--;
                if (print) System.out.printf("i : " + i + "\n");
                if (redCap && blackCap) {
                    lastColor = Color.SPECIAL;
                    if (print) System.out.println("set special");
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
        if (playerNo == 1) res = new GameState(newRedSeeds, newBlackSeeds, newSpecialSeeds, score1 + count, score2, rootMove);
        else res = new GameState(newRedSeeds, newBlackSeeds, newSpecialSeeds, score1, score2 + count, rootMove);

        return res;
    }

    public MinimaxResult minimax(GameState node, int depth, int playerNo, boolean maximisingPlayer, boolean first, int alpha, int beta) {
        if (playerNoMoves(node)) return new MinimaxResult(evalNoMoves(node, playerNo, maximisingPlayer), node.rootMove);
        if (depth == 0 || gameOver(node)) return new MinimaxResult(evalNode(node, playerNo, maximisingPlayer), node.rootMove);
        List<GameState> newNodes = new ArrayList<>();
        List<Move> moves = node.legalMoves(playerNo);
        for (Move move : moves) {
            GameState newNode; //= applyMove(move, playerNo, false);
            if (first) newNode = applyMove(move, playerNo, false, move);
            else newNode = applyMove(move, playerNo, false, node.rootMove);
            newNodes.add(newNode);
        }
        /*List<MinimaxResult> moveResults = new ArrayList<>();
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
        }*/
        MinimaxResult res = new MinimaxResult(0, moves.get(0));
        ArrayList<Integer> values = new ArrayList<>();
        if (maximisingPlayer) {
            int val = -10000;
            for (GameState nextNode : newNodes) {
                MinimaxResult newResult = minimax(nextNode, depth - 1, nextPlayer(playerNo), false, false, alpha, beta);
                if (first) {
                    values.add(newResult.valeur);
                }
                if (newResult.valeur > val) {
                    val = newResult.valeur;
                    res = newResult;
                }
                alpha = Math.max(alpha, val);
                if (val >= beta) break;
            }
        } else {
            int val = 10000;
            for (GameState nextNode : newNodes) {
                MinimaxResult newResult = minimax(nextNode, depth - 1, nextPlayer(playerNo), true, false, alpha, beta);
                if (first) values.add(newResult.valeur);
                if (newResult.valeur < val) {
                    val = newResult.valeur;
                    res = newResult;
                }
                beta = Math.min(beta, val);
                if (alpha >= val) break;
            }
        }
        if (first) System.out.println(Arrays.toString(values.toArray()));
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

    public void captureNoMoves() {
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

    public static int nextPlayer(int playerNo) {
        if (playerNo == 1) return 2;
        else return 1;
    }

    public Color getLastColor(Move move) {
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
                ", rootMove=" + rootMove +
                '}';
    }
}