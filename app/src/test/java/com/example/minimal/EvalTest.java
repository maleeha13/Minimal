package com.example.minimal;


import static android.view.View.INVISIBLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvalTest {



        static ArrayList<Integer> testCards = new ArrayList<>();
        public static ArrayList<Integer> makeTestList() {

            testCards.add(410);
            testCards.add(103);
            testCards.add(104);
            testCards.add(105);
            testCards.add(106);

            testCards.add(210);
            testCards.add(310);
            testCards.add(100);
            testCards.add(110);
            testCards.add(111);

            testCards.add(112);
            testCards.add(113);
            testCards.add(101);
            testCards.add(101);
            testCards.add(101);

            testCards.add(101);
            testCards.add(101);
            testCards.add(101);
            testCards.add(101);
            testCards.add(101);

            testCards.add(101);
            testCards.add(101);
            testCards.add(101);

            return testCards;
        }

        private List<Integer> getCardsWithSameRank(List<Integer> hand, int targetCard) {
            List<Integer> cardsWithSameRank = new ArrayList<>();
            for (Integer card : hand) {
                if (getRank(card) == getRank(targetCard) && !card.equals(targetCard)) {
                    cardsWithSameRank.add(card);
                }
            }
            return cardsWithSameRank;
        }

        private int getRank(int card) {

            return card % 100;
        }

        public double calculateProbability() {

            List<Integer> unseenCards = new ArrayList<>();
            unseenCards.add(201);
            unseenCards.add(400);
            unseenCards.add(300);
            unseenCards.add(302);
            unseenCards.add(405);
            unseenCards.add(209);
            unseenCards.add(304);
            unseenCards.add(213);
            unseenCards.add(311);
            unseenCards.add(203);
            unseenCards.add(210);
            unseenCards.add(110);
            unseenCards.add(313);
            unseenCards.add(104);
            unseenCards.add(103);
            unseenCards.add(408);
            unseenCards.add(306);
            unseenCards.add(105);
            unseenCards.add(310);
            unseenCards.add(207);


            List<Integer> discardedCards = new ArrayList<>();
            discardedCards.add(412);
            int topDiscard = discardedCards.isEmpty() ? 0 : discardedCards.get(discardedCards.size() - 1);

            // Count the number of cards smaller than the top discard in the deck
            int smallerCardsCount = 0;
            int topDiscardValue = topDiscard % 100;
            for (Integer card : unseenCards) {
                if (card % 100 < topDiscardValue) {
                    smallerCardsCount++;
                }
            }

            // Calculate the probability of picking a smaller card from the deck
            double probability = (double) smallerCardsCount / unseenCards.size();

            return probability;

        }

        @Test
        public void getAllMoves() {
            List<Move> allMoves = new ArrayList<>();

            // Get the current player's hand
            List<Integer> currentPlayerHand = new ArrayList<>();
            currentPlayerHand.add(101);
            currentPlayerHand.add(305);
            currentPlayerHand.add(407);
            currentPlayerHand.add(200);
            currentPlayerHand.add(111);

            Integer largestCardWithoutSameRank = null; // Variable to store the largest card without the same rank

            for (Integer card : currentPlayerHand) {
                // Check for other cards with the same rank
                boolean cardNotInAllMoves = allMoves.stream()
                        .flatMap(move -> move.getCardsPlayed().stream())
                        .noneMatch(card::equals);

                if (cardNotInAllMoves) {
                    List<Integer> cardsWithSameRank = getCardsWithSameRank(currentPlayerHand, card);

                    // If there are cards with the same rank, create a move for playing them together with the pile
                    if (!cardsWithSameRank.isEmpty()) {
                        cardsWithSameRank.add(card);  // Add the current card to the list
                        double probability = calculateProbability();

                        // Decide source based on probability
                        String source = probability < 0.6 ? "pile" : "deck";

                        Move movePile = new Move(cardsWithSameRank, source);
//                    Move moveDeck = new Move(cardsWithSameRank, "deck");

                        allMoves.add(movePile);
//                    allMoves.add(moveDeck);
                    } else {
                        // If the current card is higher than the current largest card without the same rank, update it
                        if (largestCardWithoutSameRank == null || card % 100 > largestCardWithoutSameRank % 100) {
                            largestCardWithoutSameRank = card ;

                        }
                    }
                }
            }

            // If there is a largest card without the same rank, add it to the moves
            if (largestCardWithoutSameRank != null) {
                double probability = calculateProbability();
                String source = probability < 0.6 ? "pile" : "deck";
                Move movePileSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), source);
//            Move moveDeckSingle = new Move(Collections.singletonList(largestCardWithoutSameRank), "deck");

                allMoves.add(movePileSingle);
//            allMoves.add(moveDeckSingle);
            }




        }

    @Test
    public void callMinimize(){
        ImageView[] mockImageViews = new ImageView[5];

        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
        }

        mockImageViews[0].setVisibility(View.VISIBLE);
        mockImageViews[1].setVisibility(View.VISIBLE);
        mockImageViews[2].setVisibility(View.VISIBLE);
        mockImageViews[3].setVisibility(View.VISIBLE);
        mockImageViews[4].setVisibility(View.VISIBLE);
        when(mockImageViews[0].getTag()).thenReturn(104);
        when(mockImageViews[1].getTag()).thenReturn(204);
        when(mockImageViews[2].getTag()).thenReturn(212);
        when(mockImageViews[3].getTag()).thenReturn(410);
        when(mockImageViews[4].getTag()).thenReturn(310);

        int largest = 0;
        Boolean pickFromStack = false;
        ImageView drop = null;
        List<ImageView> list = new ArrayList<>();

        Map<Integer, List<ImageView>> imageViewMap = new HashMap<>();

        Integer stackCardNumber = 110;

        List<ImageView> descList = new ArrayList<>();


        for (int i = 0; i < 5; i++) {
            ImageView img = mockImageViews[i];
            if(img.getVisibility() == View.VISIBLE){

                int cardNumber = (Integer)img.getTag();
                int remainder = cardNumber % 100;

                if (cardNumber % 100 == stackCardNumber % 100) {
                    pickFromStack = true;
                }

                if (imageViewMap.containsKey(remainder)) {
                    imageViewMap.get(remainder).add(img);
                } else {
                    List<ImageView> imageViewList = new ArrayList<>();
                    imageViewList.add(img);
                    imageViewMap.put(remainder, imageViewList);
                }
                descList.add(img);
            }
        }

        Collections.sort(descList, new Comparator<ImageView>() {
            @Override
            public int compare(ImageView img1, ImageView img2) {
                int tag1 = (int) img1.getTag() % 100;
                int tag2 = (int) img2.getTag() % 100;
                return tag2 - tag1;
            }
        });



        largest = (int) descList.get(0).getTag() % 100;

        int largestKey = 0;
        int largestValue = 0;

        for (Map.Entry<Integer, List<ImageView>> entry : imageViewMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue().size();

            if (value > largestValue && key!=stackCardNumber%100) {
                largestKey = key;
                largestValue = value;
            }
        }

        list.addAll(imageViewMap.get(largestKey));

        int totalValue = 0;
        for (ImageView imageView : list) {

            totalValue += (Integer) imageView.getTag() % 100;
        }

        String source;

        if(pickFromStack){
            source = "pile";
        }
        else{
            source="deck";
        }

        if (totalValue >= largest) {
            assertEquals(source, "deck");
            for (ImageView obj : list) {
                System.out.println("dropped : " + obj.getTag());

            }


            } else {
            List<ImageView> newList = new ArrayList<>();
            if (largest == stackCardNumber % 100 && pickFromStack) {
                drop = (descList.get(1));
            }

            else{
                drop = (descList.get(0));
            }
            newList.add(drop);

            for (ImageView obj : newList) {
                System.out.println("dropped : " + obj.getTag());

            }
            assertEquals(source, "pile");
            assertEquals(drop.getTag(), 212);

        }

    }


    @Test
    public void newMin() {
        ImageView[] mockImageViews = new ImageView[5];

        for (int i = 0; i < 5; i++) {
            mockImageViews[i] = mock(ImageView.class);
        }

        mockImageViews[0].setVisibility(View.VISIBLE);
        mockImageViews[1].setVisibility(View.VISIBLE);
        mockImageViews[2].setVisibility(View.VISIBLE);
        mockImageViews[3].setVisibility(View.VISIBLE);
        mockImageViews[4].setVisibility(View.VISIBLE);
        when(mockImageViews[0].getTag()).thenReturn(104);
        when(mockImageViews[1].getTag()).thenReturn(204);
        when(mockImageViews[2].getTag()).thenReturn(212);
        when(mockImageViews[3].getTag()).thenReturn(410);
        when(mockImageViews[4].getTag()).thenReturn(310);


        int largest = 0;
        Boolean pickFromStack = false;
        ImageView drop = null;
        List<ImageView> list = new ArrayList<>();
        Map<Integer, List<ImageView>> imageViewMap = new HashMap<>();

        Integer stackCardNumber = 110;

        List<ImageView> descList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ImageView img = mockImageViews[i];
            if(img.getVisibility() == View.VISIBLE){
                int cardNumber = (int) img.getTag();
                int remainder = cardNumber % 100;

                if (cardNumber % 100 == stackCardNumber % 100) {
                    pickFromStack = true;
                }

                if (imageViewMap.containsKey(remainder)) {
                    imageViewMap.get(remainder).add(img);
                } else {
                    List<ImageView> imageViewList = new ArrayList<>();
                    imageViewList.add(img);
                    imageViewMap.put(remainder, imageViewList);
                }
                descList.add(img);
            }

        }

        Collections.sort(descList, new Comparator<ImageView>() {
            @Override
            public int compare(ImageView img1, ImageView img2) {
                int tag1 = (int) img1.getTag() % 100;
                int tag2 = (int) img2.getTag() % 100;
                // Sort in descending order
                return tag2 - tag1;
            }
        });

        largest = (int) descList.get(0).getTag() % 100;

// Iterate over the image view map and add all values to the list
        int largestKey = 0;
        int largestValue = 0;

// Find the key with the largest value in the map
        for (Map.Entry<Integer, List<ImageView>> entry : imageViewMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue().size();

            if (value > largestValue) {
                largestKey = key;
                largestValue = value;
            }
        }


        list.addAll(imageViewMap.get(largestKey));



// Check if the total value in the list is greater than 'largest'
        int totalValue = 0;
        for (ImageView imageView : list) {

            totalValue += (Integer) imageView.getTag() % 100;
            System.out.println("tot " + (Integer) imageView.getTag());
        }



        String source;

        if(pickFromStack){
            source = "pile";
        }
        else{
            source="deck";
        }


        if (totalValue >= largest) {

            MinimizeAI minimize = new MinimizeAI();
            assertEquals(source, "pile");
            assertEquals(drop.getTag(), 212);
        } else {
            // Create a new list and add 'img' to it
            List<ImageView> newList = new ArrayList<>();
            if (largest == stackCardNumber % 100 && pickFromStack) {
                drop = (descList.get(1)); // Add the second ImageView in the list to the descList

            }

            else{
                drop = (descList.get(0));
            }


            assertEquals(drop.getTag(), 211);

            newList.add(drop);
            for (ImageView obj : newList) {
                System.out.println("dropped : " + obj.getTag());

            }


        }


    }
}
