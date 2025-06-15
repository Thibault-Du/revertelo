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

    public class Node {
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
            // on joue à l'infiniiii
            return getActions(state, player, opponent).isEmpty() &&
                    getActions(state, opponent, player).isEmpty();
        }

    }

    private static final int BUDGET = 1000;
    private static final double C = 2;

    public int[] uctSearch(Player[][] s0, Player currentPlayer, Player opponent) {
        Node root = new Node(copyBoard(s0), null, getActions(s0, currentPlayer, opponent), null);
        if (root.untriedActions.isEmpty()) return null;
        for (int i = 0; i < BUDGET; i++) {
            Node v = treePolicy(root, currentPlayer, opponent);
            double delta = defaultPolicy(v.state, currentPlayer, opponent);
            backup(v, delta);
        }

        analyseArbre(root);
        return bestChild(root, 0).action;
    }

    private Player[][] copyBoard(Player[][] s0) {
        int size = s0.length;
        Player[][] copy = new Player[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(s0[i], 0, copy[i], 0, size);
        }
        return copy;
    }

    private List<int[]> getActions(Player[][] state, Player player, Player opponent) {
        List<int[]> possibleMoves = new ArrayList<int[]>();
        for (int rowIndex = 0; rowIndex < state.length; rowIndex++)
            for (int columnIndex = 0; columnIndex < state[0].length; columnIndex++)
                if (CanSetPlayer(state, rowIndex, columnIndex, player, opponent))
                    possibleMoves.add(new int[] { rowIndex, columnIndex });
        return possibleMoves;
    }

    private boolean CanSetPlayer(Player[][] boardPlayer, int rowIndex, int columnIndex, Player player, Player opponent)
    {
        if (boardPlayer[rowIndex][columnIndex] == null)
            if (player == null)
                return true;
            else
                for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++)
                    for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++)
                        if ((rowIndexChange != 0) || (columnIndexChange != 0))
                            if (CheckDirection(boardPlayer, rowIndex, columnIndex, rowIndexChange, columnIndexChange, player, opponent))
                                return true;
        return false;
    }

    private boolean CheckDirection(Player[][] boardPlayer, int rowIndex, int columnIndex, int rowIndexChange, int columnIndexChange, Player player, Player opponent)
    {
        boolean areOppositePlayerFound = false;
        rowIndex += rowIndexChange;
        columnIndex += columnIndexChange;
        while ((rowIndex >= 0) && (rowIndex < boardPlayer.length) && (columnIndex >= 0) && (columnIndex < boardPlayer[0].length))
        {
            if (areOppositePlayerFound)
            {
                if (boardPlayer[rowIndex][columnIndex] == player)
                    return true;
                else if (boardPlayer[rowIndex][columnIndex] == null)
                    return false;
            }
            else
            {
                if (boardPlayer[rowIndex][columnIndex] == opponent)
                    areOppositePlayerFound = true;
                else
                    return false;
            }

            rowIndex += rowIndexChange;
            columnIndex += columnIndexChange;
        }

        return false;
    }

    private Node treePolicy(Node v, Player player, Player opponent) {
        Player current = player;
        Player opp = opponent;

        while (!v.isTerminal(player, opponent)) {
            if (!v.isFullyExpanded()) {
                return expand(v, current, opp);
            } else {
                Node next = bestChild(v, C);
                if (next == null) return v;
                v = next;
                Player temp = current;
                current = opp;
                opp = temp;
            }
        }
        return v;
    }

    private Node expand(Node v, Player player, Player opponent) {
        int[] a = v.untriedActions.remove(0);
        Player[][] nextState = applyAction(v.state, a, player);
        Node child = new Node(nextState, v, getActions(nextState, player, opponent), a);
        v.children.add(child);
        return child;
    }

    private Player[][] applyAction(Player[][] state, int[] a, Player player) {
        Player[][] nextState = copyBoard(state);
        nextState[a[0]][a[1]] = player;
        return nextState;
    }

    @Override
    public void GetNextMove(Player[][] boardByPlayers, Integer[] rowColumnIndexes, Player realOpponent) {
        int[] move = uctSearch(boardByPlayers, this, realOpponent);
        if (move == null) {
            return;
        }
        rowColumnIndexes[0] = move[0];
        rowColumnIndexes[1] = move[1];
    }

    private Node bestChild(Node v, double c) {
        Node best = null;
        double maxValue = -Double.MAX_VALUE;

        for (Node child : v.children) {
            double value = ( child.Q / child.N ) + c * Math.sqrt(2 * Math.log(v.N) / child.N);
            if (value > maxValue) {
                maxValue = value;
                best = child;
            }
        }
        return best;
    }


    private static final int NB_SIMU = 20;

    private double defaultPolicy(Player[][] state, Player player, Player opponent) {
        double totalResult = 0.0;

        for (int i = 0; i < NB_SIMU; i++) {
            totalResult += simulateRandomGame(state, player, opponent);
        }

        return totalResult;
    }

    private double simulateRandomGame(Player[][] state, Player player, Player opponent) {
        Player[][] simulation = copyBoard(state);
        Random rand = new Random();
        Player current = player;

        while (!getActions(simulation, current, current == player ? opponent : player).isEmpty()) {
            Player opp = (current == player) ? opponent : player;
            List<int[]> actions = getActions(simulation, current, opp);
            if (!actions.isEmpty()) {
                int[] move = actions.get(rand.nextInt(actions.size()));
                simulation = applyAction(simulation, move, current);
            }
            current = opp;
        }

        int playerScore = 0;
        int opponentScore = 0;
        for (int i = 0; i < simulation.length; i++) {
            for (int j = 0; j < simulation[0].length; j++) {
                if (simulation[i][j] == player) playerScore++;
                else if (simulation[i][j] == opponent) opponentScore++;
            }
        }

        if (playerScore > opponentScore) return 1;
        else if (playerScore < opponentScore) return -1;
        else return 0;
    }

    private void backup(Node v, double delta) {
        while (v != null) {
            v.N++;
            v.Q += delta;
            v = v.parent;
        }
    }

    public void analyseArbre(Node root) {
        int totalNodes = 0;
        int maxDepth = 0;
        int nbLeaf = 0;
        Map<Integer, Integer> nodesParLevel = new HashMap<>();

        Queue<Map.Entry<Node, Integer>> queue = new LinkedList<>();
        queue.add(new AbstractMap.SimpleEntry<>(root, 0));

        while (!queue.isEmpty()) {
            Map.Entry<Node, Integer> current = queue.poll();
            Node node = current.getKey();
            int depth = current.getValue();

            totalNodes++;
            maxDepth = Math.max(maxDepth, depth);
            nodesParLevel.put(depth, nodesParLevel.getOrDefault(depth, 0) + 1);

            if (!node.children.isEmpty()) {
                for (Node child : node.children) {
                    queue.add(new AbstractMap.SimpleEntry<>(child, depth + 1));
                }
            } else {
                nbLeaf++;
            }
        }

        System.out.println("----- MCTS Tree Analysis -----");
        System.out.println("Profondeur arbre : " + maxDepth);
        System.out.println("Nombre de niveau : " + nodesParLevel.size());
        System.out.println("Nombre de feuille : " + nbLeaf);
        System.out.println("Noeuds par level:");
        for (int level : nodesParLevel.keySet()) {
            System.out.println("  Level " + level + ": " + nodesParLevel.get(level));
        }
        System.out.println("--------------------------------");
    }


}