package com.example.minimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSNode {
    private int visits;
    private int wins;
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


    // Add any other methods or getters/setters as needed

//    public MCTSNode select(MCTSNode rootNode, double explorationConstant) {
//        MCTSNode currentNode = rootNode;
//
//        // Traverse the tree until a leaf node is reached
//        while (!currentNode.getChildren().isEmpty()) {
//            // Select the child node with the highest UCT value
//            double bestUCTValue = Double.NEGATIVE_INFINITY;
//            MCTSNode selectedChild = null;
//
//            for (MCTSNode child : currentNode.getChildren()) {
//                double uctValue = calculateUCTValue(child, explorationConstant);
//
//                if (uctValue > bestUCTValue) {
//                    bestUCTValue = uctValue;
//                    selectedChild = child;
//                }
//            }
//
//            currentNode = selectedChild;
//        }
//
//        return currentNode;
//    }
//
//    public MCTSNode expand() {
//        List<Move> untriedMoves = getUntriedMoves();
//
//        if (!untriedMoves.isEmpty()) {
//            Move selectedMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
//            MCTSNode childNode = addChild(selectedMove, playerJustMoved);
//            return childNode;
//        }
//
//        return null;  // No untried moves, cannot expand further
//    }

//    public double simulate() {
//        State simulatedState = gameState.Clone();
//        int currentPlayer = playerJustMoved;
//
//        while (!simulatedState.isTerminal()) {
//            List<Move> legalMoves = simulatedState.getAllMoves(currentPlayer);
//            if (legalMoves.isEmpty()) {
//                break;  // No legal moves, end simulation
//            }
//
//            Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
//            simulatedState.applyMove(randomMove, currentPlayer);
//
//            currentPlayer = (currentPlayer % 4) + 1;  // Switch to the next player
//        }
//
//        return simulatedState.getScore(playerJustMoved);  // Return the result of the simulation
//    }
//    public void backpropagate(double result) {
//        MCTSNode currentNode = this;
//
//        while (currentNode != null) {
//            currentNode.visits++;
//            currentNode.addToReward(result);
//            currentNode = currentNode.parent;
//        }
//    }
    public List<Move> getUntriedMoves() {

        if (children.isEmpty()) {
            // If the node has no children, all moves are untried
//            System.out.println("it is empty????");
            return gameState.getAllMoves(gameState.getPlayerToMove());  // Implement this method in your State class
        } else {
//            System.out.println("notttttt????");

            // If the node has children, filter out moves that have already been tried
            List<Move> allMoves = gameState.getAllMoves(gameState.getPlayerToMove());  // Implement this method in your State class
            List<Move> triedMoves = new ArrayList<>();

            for (MCTSNode child : children) {
                // Collect moves from child nodes

                triedMoves.add(child.getGameState().getLastMove());
//                System.out.println("the last move was "+ child.getGameState().getLastMove());
            }

            // Remove tried moves from the list of all moves
            allMoves.removeAll(triedMoves);
            System.out.println("moves "+ allMoves);

            return allMoves;
        }
    }

    public MCTSNode addChild(Move move, int playerJustMoved) {
        MCTSNode childNode = new MCTSNode(null, this, playerJustMoved, move);  // Replace 'null' with the appropriate initial game state
        childNode.move = move;
        childNode.parent = this;
        childNode.gameState = this.gameState;

        // Additional initialization or updates based on the new child node

        childNode.setParentPlayerJustMoved(playerJustMoved);
        children.add(childNode);
//        System.out.println("adding .... " + children);
        return childNode;
    }

    public void update(boolean terminalState) {
        visits++;

        if (playerJustMoved != 0) {
            // Assuming your State class has a method GetResult(int player) to get the result for a player
           if(gameState.getResult(playerJustMoved)){
               wins++;
           }
        }
    }

    private void setParentPlayerJustMoved(int playerJustMoved) {
        // Set the playerJustMoved value for the child node
        // Additional logic as needed
    }

    public MCTSNode UCBSelectChild(List<Move> legalMoves, double exploration) {

//        System.out.println("entering ucb");
        // Filter the list of children by the list of legal moves
        List<MCTSNode> legalChildren = new ArrayList<>();
        for (MCTSNode child : children) {
            System.out.println(" lms is " + legalMoves);
            System.out.println("child is " + child.getMove());
            if (legalMoves.contains(child.getMove())) {
                legalChildren.add(child);
//                System.out.println(" adding legal children");
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
                        + exploration * Math.sqrt(Math.log(child.getVisits()) / child.getVisits());
            }
//            System.out.println("UCB is " + UCB);

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
        // You might need to add additional cloning logic based on your specific implementation

        return clone;
    }

}
