package com.example.minimal;

import org.junit.Test;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MCTSNodeTest {

    @Test
    public void testUpdate_Win() {
        // Mock a State object representing a win for the current player
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1); // Assuming current player is player 1
        when(state.getResult(1)).thenReturn(true); // Mock a win for player 1
        when(state.getScores(state)).thenReturn(new int[]{10, 20}); // Mock scores

        // Create a parent MCTSNode (if needed)
        MCTSNode parent = null;

        // Create a Move object (if needed)
        Move move = null;

        // Create an initial MCTSNode with a State object
        MCTSNode node = new MCTSNode(state, parent, 1, move);
        node.visits = 5; // Initial visits
        node.wins = 2;   // Initial wins
        node.score = 15; // Initial score

        // Call the method under test
        node.update(state);

        // Verify the updates
        assertEquals(6, node.visits); // Visits should increase by 1
        assertEquals(3, node.wins);   // Wins should increase by 1
    }


    @Test
    public void testUpdate_NoWin() {
        // Mock a State object representing a loss for the current player
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1); // Assuming current player is player 1
        when(state.getResult(1)).thenReturn(false); // Mock no win for player 1
        when(state.getScores(state)).thenReturn(new int[]{30, 40}); // Mock scores

        // Create an initial MCTSNode with some initial values
        MCTSNode node = new MCTSNode(state, null, 1, null);
        node.visits = 5; // Initial visits
        node.wins = 2;   // Initial wins
        node.score = 15; // Initial score

        // Call the method under test
        node.update(state);

        // Verify the updates
        assertEquals(6, node.visits); // Visits should increase by 1
        assertEquals(2, node.wins);   // Wins should remain the same
    }


    @Test
    public void testAddChild() {
        // Create a parent MCTSNode
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1); // Assuming current player is player 1
        when(state.getResult(1)).thenReturn(true); // Mock a win for player 1
        when(state.getScores(state)).thenReturn(new int[]{10, 20}); // Mock scores

        // Create a parent MCTSNode (if needed)
        MCTSNode parent = null;

        // Create a Move object (if needed)

        MCTSNode parentNode = new MCTSNode(state, parent, 1, null);

        // Create a Move object
        List<Integer> cardsPlayed = Arrays.asList(1, 2, 3); // Example cards played
        String source = "deck"; // Example source
        Move move = new Move(cardsPlayed, source);

        // Add a child node using the addChild method
        MCTSNode childNode = parentNode.addChild(move, 1); // Assuming playerJustMoved is 1

        // Verify that the child node is correctly added to the parent node's list of children
        assertTrue(parentNode.getChildren().contains(childNode));

        // Verify that the child node is initialized with the provided move and player information
        assertEquals(move, childNode.getMove());
        assertEquals(1, childNode.getPlayerJustMoved());
        assertEquals(parentNode, childNode.getParent());
        assertEquals(parentNode.getGameState(), childNode.getGameState()); // Assuming gameState is copied from the parent
    }


    @Test
    public void testIsFullyExpanded() {
        // Create a parent MCTSNode
        State st = mock(State.class);
        when(st.getPlayerToMove()).thenReturn(1); // Assuming current player is player 1
        when(st.getResult(1)).thenReturn(true); // Mock a win for player 1
        when(st.getScores(st)).thenReturn(new int[]{10, 20}); // Mock scores

        // Create a Move object (if needed)

        MCTSNode parentNode = new MCTSNode(st, null, 1, null);

        // Mock a State object
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1); // Assuming player 1 is the current player

        // Set the parent node's game state
        parentNode.setGameState(state);

        // Add some child nodes
        List<Move> legalMoves = Arrays.asList(
                new Move(Arrays.asList(1, 2), "deck"), // Move 1
                new Move(Arrays.asList(3, 4), "pile"), // Move 2
                new Move(Arrays.asList(5, 6), "hand")   // Move 3
        );
        for (Move move : legalMoves) {
            MCTSNode childNode = new MCTSNode(state, parentNode, 1, move); // Assuming playerJustMoved is 1
            childNode.setMove(move);
            parentNode.getChildren().add(childNode);
        }

        // Call the method under test
        boolean fullyExpanded = parentNode.isFullyExpanded();

        // Verify that the result is true since all legal moves have corresponding child nodes
        assertTrue(fullyExpanded);
    }

//    @Test
//    public void testUCBSelectChild() {
//        // Mock a list of legal moves
//        List<Move> legalMoves = Arrays.asList(
//                new Move(Arrays.asList(1, 2), "deck"),
//                new Move(Arrays.asList(3, 4), "pile"),
//                new Move(Arrays.asList(5, 6), "hand")
//        );
//
//        // Create child nodes
//        State state = mock(State.class);
//        MCTSNode child1 = new MCTSNode(state, null, 1, new Move(Arrays.asList(1, 2), "deck"));
//        MCTSNode child2 = new MCTSNode(state, null, 1, new Move(Arrays.asList(3, 4), "pile"));
//        MCTSNode child3 = new MCTSNode(state, null, 1, new Move(Arrays.asList(5, 6), "hand"));
//
//        // Set the visits and wins for the child nodes
//        child1.setVisits(10);
//        child2.setVisits(20);
//        child3.setVisits(30);
//        child1.setWins(3);
//        child2.setWins(4);
//        child3.setWins(5);
//
//        // Mock the behavior of state.getAllMoves(1) to return legalMoves
//        when(state.getAllMoves(1)).thenReturn(legalMoves);
//
//        // Create the parent node
//        MCTSNode parentNode = new MCTSNode(state, null, 1, null);
//        List<MCTSNode> children = Arrays.asList(child1, child2, child3);
//        parentNode.setChildren(children);
//
//        // Create an instance of the class containing the method to test
//
//        MCTSNode selectedChild = parentNode.UCBSelectChild(legalMoves, 1.0);
//
//        // Call the method under test
//        assertEquals(child3, selectedChild);
//    }




}
