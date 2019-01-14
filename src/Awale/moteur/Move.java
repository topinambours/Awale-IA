package Awale.moteur;

public class Move{
    int position;
    boolean redFirst;
    int posSpecial;

    Move(int position, boolean redFirst, int posSpecial) {
        this.position = position;
        this.redFirst = redFirst;
        this.posSpecial = posSpecial;
    }

    Move(String str) {
        String[] part = str.split("(?<=\\D)(?=\\d)");
        this.posSpecial = Integer.parseInt(part[1]);//special

        String[] part2 = part[0].split("(?=\\D)(?<=\\d)");
        this.position = Integer.parseInt(part2[0]) - 1;//play
        this.redFirst =  part2[1].equals("R");//color
    }

    Move() {
        position = -1;
        redFirst = false;
        posSpecial = 0;
    }

    void set(Move move) {
        this.position = move.position;
        this.posSpecial = move.posSpecial;
        this.redFirst = move.redFirst;
    }

    private String letter() {
        if (redFirst) return "R";
        else return "B";
    }

    @Override
    public String toString() {
        return "" + (position + 1) + letter() + posSpecial;
    }
}