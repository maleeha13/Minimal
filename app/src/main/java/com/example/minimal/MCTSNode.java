package com.example.minimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a node in the search tree for ISMCTS
 */
public class MCTSNode {
    private int visits;
    private int wins;
    private double score;
    private double totalReward;
    private MCTSNode parent;
    private List<MCTSNode> children;
    private State gameState;  // Replace with your actual State class or type
    private Move move;  // Replace with your actual State class or type
    private int playerJustMoved;  // Replace with your actual State class or type

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

    public void updateState(State newState) {
        this.gameState = newState;
        // Perform any other necessary updates based on the new state
    }
    public int getVisits() {
        return visits;
    }
    double getScore() {
        return score;
    }
    public int getWins() {
        return wins;
    }
    public Move getMove() {
        return move;
    }

    public int getPlayerJustMoved(){
        return playerJustMoved;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public MCTSNode getParent() {
        return parent;
    }

    public List<MCTSNode> getChildren() {
        return children;
    }

    public State getGameState() {
        return gameState;
    }

    public void incrementVisits() {
        visits++;
    }

    public void addToReward(double reward) {
        totalReward += reward;
    }

    public List<Move> getUntriedMoves(State state) {

        if (children.isEmpty()) {
            // If the node has no children, all moves are untried
//            System.out.println("it is empty????");
            return state.getAllMoves(state.getPlayerToMove());  // Implement this method in your State class
        } else {
//            System.out.println("notttttt????");
//            System.out.println("player innnnn untr is "+ state.getPlayerToMove());

            // If the node has children, filter out moves that have already been tried
            List<Move> allMoves = state.getAllMoves(state.getPlayerToMove());  // Implement this method in your State class
            List<Move> triedMoves = new ArrayList<>();

            for (MCTSNode child : children) {
                // Collect moves from child nodes

                triedMoves.add(child.getGameState().getLastMove());

            }

            // Remove tried moves from the list of all moves
            allMoves.removeAll(triedMoves);
//            System.out.println("moves "+ allMoves);

            return allMoves;
        }
    }

    public MCTSNode addChild(Move move, int playerJustMoved) {
        MCTSNode childNode = new MCTSNode(null, this, playerJustMoved, move);
        childNode.move = move;
        childNode.parent = this;
        childNode.gameState = this.gameState;

        // Additional initialization or updates based on the new child node

        childNode.setParentPlayerJustMoved(playerJustMoved);
        children.add(childNode);
        return childNode;
    }

    public void update(State state) {
        visits++;


            // Assuming your State class has a method GetResult(int player) to get the result for a player
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

    private void setParentPlayerJustMoved(int playerJustMoved) {
        // Set the playerJustMoved value for the child node
        // Additional logic as needed
    }
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


    public MCTSNode cloneNode() {
        MCTSNode clone = new MCTSNode(this.gameState, this.parent, this.playerJustMoved, this.move);
        clone.visits = this.visits;
        clone.wins = this.wins;
        clone.totalReward = this.totalReward;
        clone.children = new ArrayList<>(this.children);
        clone.score = this.score;
        // You might need to add additional cloning logic bas
        // ed on your specific implementation

        return clone;
    }

}