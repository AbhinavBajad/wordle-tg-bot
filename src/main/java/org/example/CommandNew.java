package org.example;
import java.util.Collections;
import java.util.Random;

import static org.data.commonWords.words;

public class CommandNew {
    public static String newCommand(WordleInstance game){
        System.out.println("Inside newCommand");
        if(!game.isPresent){
            game.LastMsg = "";
            game.gameWord = getWord();
        }
        return (!game.isPresent) ? "New game started, Guess the 5 letter word!  " :
                "Game is already in progress!  ";
    }

    public static String getWord(){
        Collections.shuffle(words);

        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }
}
