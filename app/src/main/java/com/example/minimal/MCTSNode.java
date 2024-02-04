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

    // Example method to calculate the UCT value for a child node
    public double calculateUCTValue(MCTSNode child, double explorationConstant) {
        if (child.getVisits() == 0) {
            return Double.MAX_VALUE;  // High UCT for unvisited nodes
        }

        double exploitation = child.getTotalReward() / child.getVisits();
        double exploration = explorationConstant * Math.sqrt(Math.log(visits) / child.getVisits());

        return exploitation + exploration;
    }
}
