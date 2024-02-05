package com.example.minimal;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    private int visits;
    private int wins;
    private double totalReward;
    private MCTSNode parent;
    private List<MCTSNode> children;
    private State gameState;  // Replace with your actual State class or type
    private Move move;  // Replace with your actual State class or type
    private int playerJustMoved;  // Replace with your actual State class or type

    public MCTSNode(State gameState, MCTSNode parent, Integer playerJustMoved) {
        this.gameState = gameState;
        this.parent = parent;
        this.visits = 0;
        this.wins = 0;
        this.totalReward = 0.0;
        this.children = new ArrayList<>();
        this.playerJustMoved = playerJustMoved;
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
            System.out.println("it is empty????");
            return gameState.getAllMoves(1);  // Implement this method in your State class
        } else {
            System.out.println("notttttt????");

            // If the node has children, filter out moves that have already been tried
            List<Move> allMoves = gameState.getAllMoves(1);  // Implement this method in your State class
            List<Move> triedMoves = new ArrayList<>();

            for (MCTSNode child : children) {
                // Collect moves from child nodes

                triedMoves.add(child.getGameState().getLastMove());
                System.out.println("the last move was "+ child.getGameState().getLastMove());
            }

            // Remove tried moves from the list of all moves
            allMoves.removeAll(triedMoves);
            System.out.println("moves "+ allMoves);

            return allMoves;
        }
    }

    public MCTSNode addChild(Move move, int playerJustMoved) {
        MCTSNode childNode = new MCTSNode(null, this, playerJustMoved);  // Replace 'null' with the appropriate initial game state
        childNode.move = move;
        childNode.parent = this;
        childNode.gameState = this.gameState;

        // Additional initialization or updates based on the new child node

        childNode.setParentPlayerJustMoved(playerJustMoved);
        children.add(childNode);
        System.out.println("adding .... " + children);
        return childNode;
    }

    public void update(boolean terminalState) {
        visits++;

        if (playerJustMoved != 0) {
            // Assuming your State class has a method GetResult(int player) to get the result for a player
//            wins += gameState.getResult(playerJustMoved);
        }
    }

    private void setParentPlayerJustMoved(int playerJustMoved) {
        // Set the playerJustMoved value for the child node
        // Additional logic as needed
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
