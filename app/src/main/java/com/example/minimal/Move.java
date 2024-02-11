package com.example.minimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move implements Cloneable{
    private List<Integer> cardsPlayed;
    private String source;  // Source can be "deck", "pile", or any other relevant identifier

    public Move(List<Integer> cardsPlayed, String source) {
        this.cardsPlayed = cardsPlayed;
        this.source = source;
    }

    @Override
    public Move clone() {
        try {
            return (Move) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported for Move class", e);
        }
    }

    // Add getters and setters as needed

    public List<Integer> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Integer> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // You can also override toString() method for better readability
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Played card(s) ");
        for (Integer card : cardsPlayed) {
            sb.append(card).append(" ");
        }
        sb.append("from ").append(source);
        return sb.toString();
    }



    // Inside the Move class
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Move otherMove = (Move) obj;
        return Objects.equals(cardsPlayed, otherMove.cardsPlayed) &&
                Objects.equals(source, otherMove.source);
    }

    // Optional: Override hashCode for consistency
    @Override
    public int hashCode() {
        return Objects.hash(cardsPlayed, source);
    }

}

