package game;

import java.util.*;

public class CPMCTS extends ComputerPlayer{

    /**
     * Constructor.
     *
     * @param notAllowedHue This hue is not allowed for the color of the player checkers.
     * @param noImageID     This imageID is not allowed for the image destination of the player checkers.
     * @param name          The name of the player.
     */
    public CPMCTS(float notAllowedHue, int noImageID, String name) {
        super(notAllowedHue, noImageID, name);
    }

    public static class Node {
        public Player[][] state;
        public Node parent;
        public List<Node> children = new ArrayList<>();
        public List<int[]> untriedActions;
        public int[] action;
        public double Q = 0; // score
        public int N = 0; // nbr  de visite

        public Node(Player[][] state, Node parent, List<int[]> actions, int[] action) {
            this.state = state;
            this.parent = parent;
            this.untriedActions = new ArrayList<>(actions);
            this.action = action;
        }

        public boolean isFullyExpanded() {
            return untriedActions.isEmpty();
        }

        public boolean isTerminal(Player player, Player opponent) {
            // TODO: implémenter la condition de fin de partie
            // on joue à l'infiniiii
            return getActions(state, player, opponent).isEmpty() &&
                    getActions(state, opponent, player).isEmpty();
        }

    }

    private static final int BUDGET = 1000;
    private static final double C = Math.sqrt(2);

    public static int[] uctSearch(Player[][] s0, Player currentPlayer, Player opponent) {
        Node root = new Node(copyBoard(s0), null, getActions(s0, currentPlayer, opponent), null);

        for (int i = 0; i < BUDGET; i++) {
            Node v = treePolicy(root, currentPlayer, opponent);
            double delta = defaultPolicy(v.state, currentPlayer);
            backup(v, delta);
        }

        return bestChild(root, 0).action;
    }

    private static Player[][] copyBoard(Player[][] s0) {
        int size = s0.length;
        Player[][] copy = new Player[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(s0[i], 0, copy[i], 0, size);
        }
        return copy;
    }

    private static List<int[]> getActions(Player[][] state, Player player, Player opponent) {

        // TODO: retourner la liste des coups possibles
        return new ArrayList<>();
    }

    private static Node treePolicy(Node v, Player player, Player opponent) {
        while (!v.isTerminal(player, opponent)) {
            if (!v.isFullyExpanded()) return expand(v, player, opponent);
            else v = bestChild(v, C);
        }
        return v;
    }

    private static Node expand(Node v, Player player, Player opponent) {
        int[] a = v.untriedActions.remove(0);
        Player[][] nextState = applyAction(v.state, a);
        Node child = new Node(nextState, v, getActions(nextState, player, opponent), a);
        v.children.add(child);
        return child;
    }

    private static Player[][] applyAction(Player[][] state, int[] a) {
        // TODO: appliquer un coup à un plateau
        return new Player[0][0];
    }

    @Override
    public void GetNextMove(Player[][] boardByPlayers, Integer[] rowColumnIndexes, Player realOpponent) {
        int[] move = uctSearch(boardByPlayers, this, realOpponent);
        rowColumnIndexes[0] = move[0];
        rowColumnIndexes[1] = move[1];
    }

    private static Node bestChild(Node v, double c) {
        // TODO: implémenter sélection du meilleur enfant avec UCT
        return null;
    }


    private static double defaultPolicy(Player[][] state, Player player) {
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