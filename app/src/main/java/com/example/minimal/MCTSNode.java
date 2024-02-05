package com.example.minimal;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    private int visits;
    private double totalReward;
    private MCTSNode parent;
    private List<MCTSNode> children;
    private State gameState;  // Replace with your actual State class or type

    public MCTSNode(State gameState, MCTSNode parent) {
        this.gameState = gameState;
        this.parent = parent;
        this.visits = 0;
        this.totalReward = 0.0;
        this.children = new ArrayList<>();
    }

    public void updateState(State newState) {
        this.gameState = newState;
        // Perform any other necessary updates based on the new state
    }
    public int getVisits() {
        return visits;
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

    public void addChild(MCTSNode child) {
        children.add(child);
    }

    // Add any other methods or getters/setters as needed

    public MCTSNode select(MCTSNode rootNode, double explorationConstant) {
        MCTSNode currentNode = rootNode;

        // Traverse the tree until a leaf node is reached
        while (!currentNode.getChildren().isEmpty()) {
            // Select the child node with the highest UCT value
            double bestUCTValue = Double.NEGATIVE_INFINITY;
            MCTSNode selectedChild = null;

            for (MCTSNode child : currentNode.getChildren()) {
                double uctValue = calculateUCTValue(child, explorationConstant);

                if (uctValue > bestUCTValue) {
                    bestUCTValue = uctValue;
                    selectedChild = child;
                }
            }

            currentNode = selectedChild;
        }

        return currentNode;
    }

//    public MCTSNode expand() {
//        // Get the untried moves from the current game state
//        List<Move> untriedMoves = getUntriedMoves();
//
//        // If there are untried moves, pick one and create a new child node
//        if (!untriedMoves.isEmpty()) {
//            Move selectedMove = untriedMoves.get(0);  // You might want to implement a strategy for selecting moves
////            State newState = gameState.applyMoveAndGetResult(selectedMove);  // Implement this method in your State class
//
//            // Create a new child node with the selected move and the resulting state
//            MCTSNode newChildNode = new MCTSNode(newState, this);
//            this.addChild(newChildNode);
//
//            return newChildNode;
//        }
//
//        // If all moves have been tried, return null or handle the case accordingly
//        return null;
//    }


    public List<Move> getUntriedMoves() {
        if (children.isEmpty()) {
            // If the node has no children, all moves are untried
            return gameState.getAllMoves(1);  // Implement this method in your State class
        } else {
            // If the node has children, filter out moves that have already been tried
            List<Move> allMoves = gameState.getAllMoves(1);  // Implement this method in your State class
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


    // Example method to calculate the UCT value for a child node
    public double calculateUCTValue(MCTSNode child, double explorationConstant) {
        if (child.getVisits() == 0) {
            return Double.MAX_VALUE;  // High UCT for unvisited nodes
        }

        double exploitation = child.getTotalReward() / child.getVisits();
        double exploration = explorationConstant * Math.sqrt(Math.log(child.getParent().getVisits()) / child.getVisits());

        return exploitation + exploration;
    }

}
