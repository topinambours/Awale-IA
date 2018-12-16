package Advanced.moteur;

class Move{
    public final int position;
    public final boolean redFirst;
    public final int posSpecial;

    public Move(int position, boolean redFirst, int posSpecial) {
        this.position = position;
        this.redFirst = redFirst;
        this.posSpecial = posSpecial;
    }

    public Move(String str) {

        String[] part = str.split("(?<=\\D)(?=\\d)");
        this.posSpecial = Integer.parseInt(part[1]);//special

        String[] part2 = part[0].split("(?=\\D)(?<=\\d)");
        this.position = Integer.parseInt(part2[0]);//play
        this.redFirst =  part2[1] == "R";//color
    }

    public Move() {
        position = -1;
        redFirst = false;
        posSpecial = -1;
    }

    @Override
    public String toString() {
        return "Move{" +
                "position=" + position +
                ", redFirst=" + redFirst +
                ", posSpecial=" + posSpecial +
                '}';
    }
}