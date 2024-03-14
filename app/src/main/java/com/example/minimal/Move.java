package com.example.minimal;

import java.util.List;
import java.util.Objects;

/**
 * The Move class represents a move made in a game, including the cards played and the source of the move.
 */
public class Move implements Cloneable{

    /** The list of cards played in the move. */
    private List<Integer> cardsPlayed;

    /** The source of the move */
    private String source;


    /**
     * Constructs a Move object with the specified cards played and source.
     *
     * @param cardsPlayed The list of cards played in the move.
     * @param source The source of the move.
     */
    public Move(List<Integer> cardsPlayed, String source) {
        this.cardsPlayed = cardsPlayed;
        this.source = source;
    }

    /**
     * Creates a deep copy of the Move object.
     * @return A cloned copy of the Move object.
     */
    @Override
    public Move clone() {
        try {
            return (Move) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported for Move class", e);
        }
    }

    /**
     * Retrieves the list of cards played in the move.
     * @return The list of cards played in the move.
     */
    public List<Integer> getCardsPlayed() {
        return cardsPlayed;
    }

    /**
     * Retrieves the source of the move.
     * @return The source of the move.
     */
    public String getSource() {
        return source;
    }

    /**
     * Generates a string representation of the move for better readability.
     * @return A string representation of the move.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Played card(s) ");
        for (Integer card : cardsPlayed) {
            sb.append(card).append(" ");
        }
        sb.append("from ").append(source);
        return sb.toString();
    }


    /**
     * Checks if this Move object is equal to another object.
     * @param obj The object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Move otherMove = (Move) obj;
        return Objects.equals(cardsPlayed, otherMove.cardsPlayed) &&
                Objects.equals(source, otherMove.source);
    }

    /**
     * Generates a hash code for the Move object for consistency.
     * @return The hash code of the Move object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardsPlayed, source);
    }

}

