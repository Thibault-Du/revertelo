package game;

import java.util.*;

public class CPMCTS {

    static final int SIZE = 3;

    public static class Node {
        public char[][] state;
        public Node parent;
        public List<Node> children = new ArrayList<>();
        public List<int[]> untriedActions;
        public int[] action;
        public double Q = 0; // score
        public int N = 0; // nbr  de visite

        public Node(char[][] state, Node parent, List<int[]> actions, int[] action) {
            this.state = state;
            this.parent = parent;
            this.untriedActions = new ArrayList<>(actions);
            this.action = action;
        }

        public boolean isFullyExpanded() {
            return untriedActions.isEmpty();
        }

        public boolean isTerminal() {
            // TODO: implémenter la condition de fin de partie
            return false; // on joue à l'infiniiii
        }

    }

    private static final int BUDGET = 1000;
    private static final double C = Math.sqrt(2);

    public static int[] uctSearch(char[][] s0, char player) {
        Node root = new Node(copyBoard(s0), null, getActions(s0), null);

        for (int i = 0; i < BUDGET; i++) {
            Node v = treePolicy(root);
            double delta = defaultPolicy(v.state, player);
            backup(v, delta);
        }

        return bestChild(root, 0).action;
    }

    private static char[][] copyBoard(char[][] s0) {
        // TODO: implémenter une copie du plateau
        return new char[0][0];
    }

    private static List<int[]> getActions(char[][] s0) {
        // TODO: retourner la liste des coups possibles
        return new ArrayList<>();
    }

    private static Node treePolicy(Node v) {
        while (!v.isTerminal()) {
            if (!v.isFullyExpanded()) return expand(v);
            else v = bestChild(v, C);
        }
        return v;
    }

    private static Node expand(Node v) {
        int[] a = v.untriedActions.remove(0);
        char[][] nextState = applyAction(v.state, a);
        Node child = new Node(nextState, v, getActions(nextState), a);
        v.children.add(child);
        return child;
    }

    private static char[][] applyAction(char[][] state, int[] a) {
        // TODO: appliquer un coup à un plateau
        return new char[0][0];
    }

    private static Node bestChild(Node v, double c) {
        // TODO: implémenter sélection du meilleur enfant avec UCT
        return null;
    }


    private static double defaultPolicy(char[][] state, char player) {
        // TODO: implémenter une simulation aleaatoire du jeu
        return 0;
    }

    private static void backup(Node v, double delta) {
        while (v != null) {
            v.N++;
            v.Q += delta;
            v = v.parent;
        }
    }

}