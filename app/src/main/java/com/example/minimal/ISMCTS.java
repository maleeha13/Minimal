package com.example.minimal;


import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ISMCTS {

    private State rootState; // The current state of the real game from which the algorithm tries to find the optimal move

    private MCTSNode currentNode; // The node currently



    public Move run(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game) throws CloneNotSupportedException {
        MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);
        expandNode(rootNode, rootState);
        for (int i = 0; i < itermax; i++) {
            System.out.println("iter num " + i);
//            System.out.println("cards are " + rootState.getDiscardedCards().size());
            State state = rootState.clone();
             state = state.CloneAndRandomize(rootState.getPlayerToMove());
            MCTSNode node = rootNode.cloneNode(); // Start each iteration from the root node


//            // Ensure root node is expanded initially
//            if (rootNode.getChildren().isEmpty()) {
//                expandNode(rootNode, state);
//            }

            node=rootNode.cloneNode();
            // Select
            while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !node.getUntriedMoves(state).isEmpty() && !state.isTerminal()) {
//                System.out.println("SELECTION");
                System.out.println("player to move b4 ucb is " + state.getPlayerToMove());
                node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.7);
                state.applyMove(node.getMove(), state.getPlayerToMove());
                System.out.println("applying ,,,, "  + node.getMove());

//                // Expand the selected node if it hasn't been visited yet
//                if (node.getVisits() == 0) {
//                    expandNode(node, state);
//                }
            }



            // Simulate
            while (!state.getAllMoves(state.getPlayerToMove()).isEmpty() && !state.isTerminal()) {
//                System.out.println("SIMULATION");

                List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                state.applyMove(randomMove, state.getPlayerToMove());
                boolean simulationResult = state.getResult(state.getPlayerToMove());

                node.update(simulationResult);


            }


            // Backpropagate
            while (node != null) {
//                System.out.println("BACK PROP");

                node.update(state.getResult(node.getPlayerJustMoved()));
                node = node.getParent();
            }

        }

//        List<MCTSNode> nodes = rootNode.getChildren();
//        for (MCTSNode node : nodes) {
//            System.out.println("moves are " + node.getMove());
//
//        }

        System.out.println("children are " );

        if (!rootNode.getChildren().isEmpty()) {
            Move maxMove = Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits)).getMove();
            System.out.println("max move is " + maxMove);
            applyBest(maxMove, hand, dropButton,  game);
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


    public Move runAlg(State rootState, int itermax, List<ImageView> hand, Button dropButton, Game game) throws CloneNotSupportedException {
        MCTSNode rootNode = new MCTSNode(rootState, null, 0, null);

        // Expand the rootNode initially
        expandNode(rootNode, rootState);

        for (int i = 0; i < itermax; i++) {
            MCTSNode node = rootNode.cloneNode(); // Clone the rootNode at the beginning of each iteration
            State state = rootState.clone();
            state = state.CloneAndRandomize(rootState.getPlayerToMove());

            // Selection and UCB
            while (!node.getUntriedMoves(state).isEmpty() && !state.isTerminal()) {
                if (node.getChildren().isEmpty()) {
                    expandNode(node, state);
                    System.out.println("are u adding " + node.getChildren().size());
                } else {
                    node = node.UCBSelectChild(state.getAllMoves(state.getPlayerToMove()), 0.7);
                    state.applyMove(node.getMove(), state.getPlayerToMove());
                }
            }

            // Simulation
            while (!state.isTerminal()) {
                List<Move> legalMoves = state.getAllMoves(state.getPlayerToMove());
                Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
                state.applyMove(randomMove, state.getPlayerToMove());
            }

            // Backpropagation
            while (node != null) {
                node.update(state.getResult(node.getPlayerJustMoved()));
                node = node.getParent();
            }
        }

        // Select the best move based on the visits
        System.out.println("does root node have child " + rootNode.getChildren().size());
        MCTSNode bestChild = Collections.max(rootNode.getChildren(), Comparator.comparingInt(MCTSNode::getVisits));
        System.out.println("best move is " + bestChild.getMove());
        applyBest(bestChild.getMove(), hand, dropButton, game);
        return bestChild.getMove();
    }


    private void expandNode(MCTSNode node, State state) {
        List<Move> untriedMoves = new ArrayList<>(node.getUntriedMoves(state)); // Create a copy of the untried moves list
        if (!untriedMoves.isEmpty()) {
            // Choose a random untried move
            Move randomMove = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
            System.out.println("size of untried move is before" + untriedMoves.size());

            // Apply the chosen move to create a new state
            State newState = state.clone(); // Assuming State has a clone method
            System.out.println("applying a move???>>>D?fCSD>");
            newState.applyMove(randomMove, newState.getPlayerToMove());
            // Add the new node as a child to the current node
            node.addChild(randomMove, newState.getPlayerToMove());

            // Remove the chosen move from the list of untried moves
            untriedMoves.remove(randomMove);
            System.out.println("size of untried move is after" + untriedMoves.size());
        }


}




//    private static void expandNode(MCTSNode node, State state) {
//        List<Move> untriedMoves = node.getUntriedMoves(state);
//        if (!untriedMoves.isEmpty()) {
//            Move m = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
//            int player = state.getPlayerToMove();
////            state.applyMove(m, player);
//            System.out.println("child added  .... " + m);
//            node.addChild(m, player);
//            return ;
//        }
//        return ; // If no untried moves, return the same node
//    }


    public void applyBest(Move maxMove, List<ImageView> hand, Button dropButton, Game game){

        String source = maxMove.getSource();
        List<Integer> cards = maxMove.getCardsPlayed();

        for (ImageView imageView : hand) {
            int tagValue = (Integer) imageView.getTag(); // Assuming the tag is an Integer

            // Check if the tag value of the current ImageView is in the cards list
            if (cards.contains(tagValue)) {
                // Perform a click action on the ImageView
                imageView.performClick();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform the second click after a 2-second delay

                dropButton.performClick();
            }

        }, 500) ; // 2000 milliseconds = 2 seconds



        if(Objects.equals(source, "deck")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Check if the game is paused before performing the third click
                    game.iv_deck.performClick();

                }
            }, 1000);

        }


        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Check if the game is paused before performing the third click
                    game.droppedCard.performClick();

                }
            }, 1000);

        }

    }
}