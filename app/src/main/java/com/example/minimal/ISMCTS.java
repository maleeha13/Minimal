package com.example.minimal;


import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import java.util.List;
import java.util.Random;

public class ISMCTS {

    private State rootState; // The current state of the real game from which the algorithm tries to find the optimal move

    private MCTSNode currentNode; // The node currently



    public Move run(State rootState, int itermax) throws CloneNotSupportedException {
        MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);

        for (int i = 0; i < itermax; i++) {
            System.out.println("iter num " + i);
            System.out.println("cards are " + rootState.getDiscardedCards().size());
            State state = rootState.clone();
             state = state.CloneAndRandomize(rootState.getPlayerToMove());
            MCTSNode node = rootNode.cloneNode(); // Start each iteration from the root node


            // Ensure root node is expanded initially
            if (node.getChildren().isEmpty()) {
                expandNode(node, state);
            }

            // Select
            while (!rootState.getAllMoves(rootState.getPlayerToMove()).isEmpty() && !node.getUntriedMoves(rootState).isEmpty() && !state.isTerminal()) {
                System.out.println("SELECTION");
                node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.7);
                state.applyMove(node.getMove(), state.getPlayerToMove());

                // Expand the selected node if it hasn't been visited yet
                if (node.getVisits() == 0) {
                    expandNode(node, state);
                }
            }



            // Simulate
            while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !state.isTerminal()) {
                System.out.println("SIMULATION");

                List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                state.applyMove(randomMove, state.getPlayerToMove());
                boolean simulationResult = state.getResult(rootState.getPlayerToMove());

                node.update(simulationResult);


            }


            // Backpropagate
            while (node != null) {
                System.out.println("BACK PROP");

                node.update(state.getResult(node.getPlayerJustMoved()));
                node = node.getParent();
            }

        }


        if (!rootNode.getChildren().isEmpty()) {
            Move maxMove = Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits)).getMove();
            System.out.println("max move is " + maxMove);

            return maxMove;
        } else {
            // Handle the case when the list of children is empty
            // For example, return a default move or throw an exception
            // For now, let's return null
            return null;
        }
        // Return the move that was most visited
//        return Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits)).getMove();
    }


    private static void expandNode(MCTSNode node, State state) {
        List<Move> untriedMoves = node.getUntriedMoves(state);
        if (!untriedMoves.isEmpty()) {
            Move m = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
            int player = state.getPlayerToMove();
//            state.applyMove(m, player);
            node.addChild(m, player);
            return ;
        }
        return ; // If no untried moves, return the same node
    }
}