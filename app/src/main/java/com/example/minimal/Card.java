package com.example.minimal;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class Card {
    static ArrayList<Integer> cards;
    public static ArrayList<Integer> getCards() {
        return cards;
    }
    public static ArrayList<Integer> makeCardList() {

        cards = new ArrayList<>();
        cards.add(102);
        cards.add(103);
        cards.add(104);
        cards.add(105);
        cards.add(106);
        cards.add(107);
        cards.add(108);
        cards.add(100);
        cards.add(110);
        cards.add(111);
        cards.add(112);
        cards.add(113);
        cards.add(101);

        cards.add(202);
        cards.add(203);
        cards.add(204);
        cards.add(205);
        cards.add(206);
        cards.add(207);
        cards.add(208);
        cards.add(200);
        cards.add(210);
        cards.add(211);
        cards.add(212);
        cards.add(213);
        cards.add(201);

        cards.add(302);
        cards.add(303);
        cards.add(304);
        cards.add(305);
        cards.add(306);
        cards.add(307);
        cards.add(308);
        cards.add(300);
        cards.add(310);
        cards.add(311);
        cards.add(312);
        cards.add(313);
        cards.add(301);

        cards.add(402);
        cards.add(403);
        cards.add(404);
        cards.add(405);
        cards.add(406);
        cards.add(407);
        cards.add(408);
        cards.add(400);
        cards.add(410);
        cards.add(411);
        cards.add(412);
        cards.add(413);
        cards.add(401);

        return cards;
    }

    public static void assignImages(int card, ImageView image) {
        Log.d("tetststts", String.valueOf(card));

        image.setTag(card);
        switch (card) {
            case 102:
                image.setImageResource(R.drawable.clubs_2);
                break;
            case 103:
                image.setImageResource(R.drawable.clubs_3);
                break;
            case 104:
                image.setImageResource(R.drawable.clubs_4);
                break;
            case 105:
                image.setImageResource(R.drawable.clubs_5);
                break;
            case 106:
                image.setImageResource(R.drawable.clubs_6);
                break;
            case 107:
                image.setImageResource(R.drawable.clubs_7);
                break;
            case 108:
                image.setImageResource(R.drawable.clubs_8);
                break;
            case 100:
                image.setImageResource(R.drawable.clubs_9);
                break;
            case 110:
                image.setImageResource(R.drawable.clubs_10);
                break;
            case 111:
                image.setImageResource(R.drawable.jack_of_clubs);
                break;
            case 112:
                image.setImageResource(R.drawable.queen_of_clubs);
                break;
            case 113:
                image.setImageResource(R.drawable.king_of_clubs);
                break;
            case 101:
                image.setImageResource(R.drawable.ace_of_clubs);
                break;
            case 202:
                image.setImageResource(R.drawable.diamonds_2);
                break;
            case 203:
                image.setImageResource(R.drawable.diamonds_3);
                break;
            case 204:
                image.setImageResource(R.drawable.diamonds_4);
                break;
            case 205:
                image.setImageResource(R.drawable.diamonds_5);
                break;
            case 206:
                image.setImageResource(R.drawable.diamonds_6);
                break;
            case 207:
                image.setImageResource(R.drawable.diamonds_7);
                break;
            case 208:
                image.setImageResource(R.drawable.diamonds_8);
                break;
            case 200:
                image.setImageResource(R.drawable.diamonds_9);
                break;
            case 210:
                image.setImageResource(R.drawable.diamonds_10);
                break;
            case 211:
                image.setImageResource(R.drawable.jack_of_diamonds);
                break;
            case 212:
                image.setImageResource(R.drawable.queen_of_diamonds);
                break;
            case 213:
                image.setImageResource(R.drawable.king_of_diamonds);
                break;
            case 201:
                image.setImageResource(R.drawable.ace_of_diamonds);
                break;

            case 302:
                image.setImageResource(R.drawable.hearts_2);
                break;
            case 303:
                image.setImageResource(R.drawable.hearts_3);
                break;
            case 304:
                image.setImageResource(R.drawable.hearts_4);
                break;
            case 305:
                image.setImageResource(R.drawable.hearts_5);
                break;
            case 306:
                image.setImageResource(R.drawable.hearts_6);
                break;
            case 307:
                image.setImageResource(R.drawable.hearts_7);
                break;
            case 308:
                image.setImageResource(R.drawable.hearts_8);
                break;

            case 300:
                image.setImageResource(R.drawable.hearts_9);
                break;
            case 310:
                image.setImageResource(R.drawable.hearts_10);
                break;
            case 311:
                image.setImageResource(R.drawable.jack_of_hearts);
                break;
            case 312:
                image.setImageResource(R.drawable.queen_of_hearts);
                break;
            case 313:
                image.setImageResource(R.drawable.king_of_hearts);
                break;
            case 301:
                image.setImageResource(R.drawable.ace_of_hearts);
                break;

            case 402:
                image.setImageResource(R.drawable.spades_2);
                break;
            case 403:
                image.setImageResource(R.drawable.spades_3);
                break;
            case 404:
                image.setImageResource(R.drawable.spades_4);
                break;
            case 405:
                image.setImageResource(R.drawable.spades_5);
                break;
            case 406:
                image.setImageResource(R.drawable.spades_6);
                break;
            case 407:
                image.setImageResource(R.drawable.spades_7);
                break;
            case 408:
                image.setImageResource(R.drawable.spades_8);
                break;
            case 400:
                image.setImageResource(R.drawable.spades_9);
                break;
            case 410:
                image.setImageResource(R.drawable.spades_10);
                break;
            case 411:
                image.setImageResource(R.drawable.jack_of_spades);
                break;
            case 412:
                image.setImageResource(R.drawable.queen_of_spades);
                break;
            case 413:
                image.setImageResource(R.drawable.king_of_spades);
                break;
            case 401:
                image.setImageResource(R.drawable.ace_of_spades);
                break;
        }
    }
}