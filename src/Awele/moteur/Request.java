package Awele.moteur;

class Request{
    public final int play;
    public final String color;
    public final int special;

    public Request(int play, String color, int special) {
        this.play = play;
        this.color = color;
        this.special = special;
    }

    public Request(String str) {

        String[] part = str.split("(?<=\\D)(?=\\d)");
        this.special = Integer.parseInt(part[1]);//special

        String[] part2 = part[0].split("(?=\\D)(?<=\\d)");
        this.play = Integer.parseInt(part2[0]);//play
        this.color =  part2[1];//color
    }

    public Request() {
        play = -1;
        color = "NaN";
        special = -1;
    }

    @Override
    public String toString() {
        return "Request{" +
                "play=" + play +
                ", color='" + color + '\'' +
                ", special=" + special +
                '}';
    }

}