package Awale.moteur;

import java.util.ArrayDeque;

class History {
    private static ArrayDeque<GameState> history = new ArrayDeque<>();

    public static void save(GameState node) {
        history.push(node);
    }

    public static GameState restore() {
        return history.pop();
    }

    private History(){}
}