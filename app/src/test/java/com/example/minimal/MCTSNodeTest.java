package com.example.minimal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class MCTSNodeTest {

    @Test
    public void testUpdate_Win() {
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1);
        when(state.getResult(1)).thenReturn(true);
        when(state.getScores(state)).thenReturn(new int[]{10, 20});

        MCTSNode parent = null;
        Move move = null;
        MCTSNode node = new MCTSNode(state, parent, 1, move);
        node.visits = 5;
        node.wins = 2;
        node.score = 15;

        node.update(state);

        assertEquals(6, node.visits);
        assertEquals(3, node.wins);
    }


    @Test
    public void testUpdate_NoWin() {
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1);
        when(state.getResult(1)).thenReturn(false);
        when(state.getScores(state)).thenReturn(new int[]{30, 40});

        MCTSNode node = new MCTSNode(state, null, 1, null);
        node.visits = 5;
        node.wins = 2;
        node.score = 15;

        node.update(state);

        assertEquals(6, node.visits);
        assertEquals(2, node.wins);
    }


    @Test
    public void testAddChild() {
        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1);
        when(state.getResult(1)).thenReturn(true);
        when(state.getScores(state)).thenReturn(new int[]{10, 20});

        MCTSNode parent = null;


        MCTSNode parentNode = new MCTSNode(state, parent, 1, null);

        List<Integer> cardsPlayed = Arrays.asList(1, 2, 3);
        String source = "deck";
        Move move = new Move(cardsPlayed, source);

        MCTSNode childNode = parentNode.addChild(move, 1);

        assertTrue(parentNode.getChildren().contains(childNode));

        assertEquals(move, childNode.getMove());
        assertEquals(1, childNode.getPlayerJustMoved());
        assertEquals(parentNode, childNode.getParent());
        assertEquals(parentNode.getGameState(), childNode.getGameState());
    }


    @Test
    public void testIsFullyExpanded() {
        State st = mock(State.class);
        when(st.getPlayerToMove()).thenReturn(1);
        when(st.getResult(1)).thenReturn(true);
        when(st.getScores(st)).thenReturn(new int[]{10, 20});


        MCTSNode parentNode = new MCTSNode(st, null, 1, null);

        State state = mock(State.class);
        when(state.getPlayerToMove()).thenReturn(1);

        parentNode.setGameState(state);

        List<Move> legalMoves = Arrays.asList(
                new Move(Arrays.asList(1, 2), "deck"),
                new Move(Arrays.asList(3, 4), "pile"),
                new Move(Arrays.asList(5, 6), "hand")
        );
        for (Move move : legalMoves) {
            MCTSNode childNode = new MCTSNode(state, parentNode, 1, move); // Assuming playerJustMoved is 1
            childNode.setMove(move);
            parentNode.getChildren().add(childNode);
        }

        boolean fullyExpanded = parentNode.isFullyExpanded();

        assertTrue(fullyExpanded);
    }



}
