package Advanced.moteur;



class MinimaxResult {
    public int valeur;
    public Move position;

    public MinimaxResult(int valeur, Move position) {
        this.valeur = valeur;
        this.position = position;
    }

    @Override
    public String toString() {
        return "MinimaxResult{" +
                "valeur=" + valeur +
                ", position=" + position +
                '}';
    }
}
