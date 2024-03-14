package com.example.minimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a node in the search tree for ISMCTS
 * Stores information such as visits, wins, parent and children of the node
 */
public class MCTSNode {

    private MCTSNode parent;
    /** The number of times this node has been visited during simulations. */
    private int visits;

    /** The number of simulated wins from this node. */
    private int wins;

    /** The total accumulated reward from simulations. */
    private double totalReward;

    /** The score associated with this node. */
    private double score;
    /** The list of child nodes for this node. */
    private List<MCTSNode> children;

    /** The player who just made a move to reach this state. */
    private Integer playerJustMoved;

    /** The move that led to this state. */
    private Move move;

    /** The game state associated with this node. */
    private State gameState;



    /**
     * Constructs a new MCTSNode with the given game state, parent node, player who just moved,
     * and the move that led to this state.
     *
     * @param gameState The game state associated with this node.
     * @param parent The parent node of this node.
     * @param playerJustMoved The player who just made a move to reach this state.
     * @param move The move that led to this state.
     */
    public MCTSNode(State gameState, MCTSNode parent, Integer playerJustMoved, Move move) {
        this.gameState = gameState;
        this.parent = parent;
        this.visits = 0;
        this.wins = 0;
        this.totalReward = 0.0;
        this.score=0;
        this.children = new ArrayList<>();
        this.playerJustMoved = playerJustMoved;
        this.move=move;
    }

    /**
     * Retrieves the number of visits to this node during simulations.
     * @return The number of visits to this node.
     */
    public int getVisits() {
        return visits;
    }

    /**
     * Retrieves the score associated with this node.
     * @return The score associated with this node.
     */
    double getScore() {
        return score;
    }

    /**
     * Retrieves the number of simulated wins from this node.
     * @return The number of wins from this node.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Retrieves the move that led to this state.
     * @return The move that led to this state.
     */
    public Move getMove() {
        return move;
    }

    /**
     * Retrieves the parent node of this node.
     * @return The parent node of this node.
     */
    public MCTSNode getParent() {
        return parent;
    }

    /**
     * Retrieves the list of child nodes for this node.
     * @return The list of child nodes for this node.
     */
    public List<MCTSNode> getChildren() {
        return children;
    }

    /**
     * Retrieves the game state associated with this node.
     * @return The game state associated with this node.
     */
    public State getGameState() {
        return gameState;
    }


    /**
     * Retrieves a list of untried moves for the given game state.
     * Untried moves are moves that have not yet been explored from the current node.
     *
     * @param state The game state for which untried moves need to be retrieved.
     * @return A list of untried moves for the given game state.
     */
    public List<Move> getUntriedMoves(State state) {

        if (children.isEmpty()) {
            // If the node has no children, all moves are untried
            return state.getAllMoves(state.getPlayerToMove());
        } else {
            // If the node has children, filter out moves that have already been tried
            List<Move> allMoves = state.getAllMoves(state.getPlayerToMove());
            List<Move> triedMoves = new ArrayList<>();

            for (MCTSNode child : children) {
                // Collect moves from child nodes
                triedMoves.add(child.getGameState().getLastMove());

            }

            // Remove tried moves from the list of all moves
            allMoves.removeAll(triedMoves);
            return allMoves;
        }
    }

    /**
     * Adds a child node to the current node with the specified move and player who just made the move.
     *
     * @param move The move that led to the creation of the child node.
     * @param playerJustMoved The player who just made the move.
     * @return The newly created child node.
     */
    public MCTSNode addChild(Move move, int playerJustMoved) {
        MCTSNode childNode = new MCTSNode(null, this, playerJustMoved, move);
        childNode.move = move;
        childNode.parent = this;
        childNode.gameState = this.gameState;

        // Additional initialization or updates based on the new child node

        children.add(childNode);
        return childNode;
    }

    /**
     * Updates the statistics of the current node based on the outcome of simulations from a given game state.
     * Increments the visit count, updates the score, and increments the win count if the current player wins.
     *
     * @param state The game state representing the outcome of simulations.
     */
    public void update(State state) {
        visits++;
            if(state.getResult(state.getPlayerToMove())){
                int[] sc = state.getScores(state);
                score = 0 + Math.sqrt(sc[state.getPlayerToMove()]);

                wins++;
            }
            else{
                int[] sc = state.getScores(state);
                score = 100 + Math.sqrt(sc[state.getPlayerToMove()]);
            }

    }

    /**
     * Checks if the current node is fully expanded, meaning all legal moves for the current player
     * have corresponding child nodes.
     *
     * @return True if all legal moves have corresponding child nodes, otherwise false.
     */
    public boolean isFullyExpanded() {
        // Get all legal moves for the current player
        List<Move> legalMoves = gameState.getAllMoves(gameState.getPlayerToMove());

        // Check if all legal moves have corresponding child nodes
        for (Move move : legalMoves) {
            boolean found = false;
            for (MCTSNode child : children) {
                if (child.getMove().equals(move)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false; // At least one legal move doesn't have a corresponding child node
            }
        }
        return true; // All legal moves have corresponding child nodes
    }


    /**
     * Selects a child node using the Upper Confidence Bound (UCB) algorithm, considering exploration parameter.
     *
     * @param legalMoves A list of legal moves for the current player.
     * @param exploration The exploration parameter for UCB.
     * @return The selected child node based on UCB score.
     */
    public MCTSNode UCBSelectChild(List<Move> legalMoves, double exploration) {
        if (children.isEmpty()) {
            return this; // Return itself if there are no children
        }

        // Filter the list of children by the list of legal moves and untried moves
        List<MCTSNode> legalChildren = new ArrayList<>();
        for (MCTSNode child : children) {
            if (legalMoves.contains(child.getMove()) && !child.isFullyExpanded()) {
                legalChildren.add(child);
            }
        }
        // Get the child with the highest UCB score
        MCTSNode selectedChild = null;
        double highestUCB = Double.NEGATIVE_INFINITY;
        for (MCTSNode child : legalChildren) {
            double UCB;
            if (child.getVisits() == 0) {
                // Assign a high value to unvisited nodes for exploration
                UCB = Double.MAX_VALUE;
            } else {
                // Use the standard UCB formula
                UCB = (double) child.getWins() / child.getVisits()
                        + exploration * Math.sqrt(Math.log(getVisits()) / child.getVisits());
            }

            if (UCB > highestUCB) {
                highestUCB = UCB;
                selectedChild = child;
            }
        }

        // Return the selected child
        return selectedChild;
    }

    /**
     * Creates a deep copy of the current node.
     *
     * @return A cloned copy of the current node.
     */
    public MCTSNode cloneNode() {
        MCTSNode clone = new MCTSNode(this.gameState, this.parent, this.playerJustMoved, this.move);
        clone.visits = this.visits;
        clone.wins = this.wins;
        clone.totalReward = this.totalReward;
        clone.children = new ArrayList<>(this.children);
        clone.score = this.score;
        return clone;
    }

}