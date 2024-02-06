package com.example.minimal;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import java.util.List;
import java.util.Random;

public class ISMCTS {

    public static Move ISMCTS(State rootState, int itermax, boolean verbose) throws CloneNotSupportedException {
        MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);

        for (int i = 0; i < itermax; i++) {
            MCTSNode node = rootNode;
            State state = rootState.CloneAndRandomize(rootState.getPlayerToMove());

            // Select
            while (!state.getAllMoves(rootState.getPlayerToMove()).isEmpty() && !node.getUntriedMoves().isEmpty()) {
                node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.7);
                state.applyMove(node.getMove(), state.getPlayerToMove());
            }

            // Expand
            List<Move> untriedMoves = node.getUntriedMoves();
            if (!untriedMoves.isEmpty()) {
                Move m = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
                int player = state.getPlayerToMove();
                state.applyMove(m, player);
                node = node.addChild(m, player);
            }

            // Simulate
            while (!state.getAllMoves(state.getPlayerToMove()).isEmpty()) {
                List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                state.applyMove(randomMove, state.getPlayerToMove());
            }

            // Backpropagate
            while (node != null) {
                node.update(state.getResult(node.getPlayerJustMoved()));
                node = node.getParent();
            }
        }

//        // Output some information about the tree - can be omitted
//        if (verbose) {
//            System.out.println(rootNode.treeToString(0));
//        } else {
//            System.out.println(rootNode.childrenToString());
//        }

        // Return the move that was most visited
        return Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits)).getMove();
    }
}


