package com.example.minimal;

import java.util.List;

public class Move {
    private List<Integer> cardsPlayed;
    private String source;  // Source can be "deck", "pile", or any other relevant identifier

    public Move(List<Integer> cardsPlayed, String source) {
        this.cardsPlayed = cardsPlayed;
        this.source = source;
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
}

