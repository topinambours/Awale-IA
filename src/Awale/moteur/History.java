package Awale.moteur;

import java.util.ArrayDeque;

/**
 * Variable globale qui sert d'historique pour le minimax
 * permet d'assurer une complexité en espace linéaire par rapport à la profondeur du minimax
 */
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