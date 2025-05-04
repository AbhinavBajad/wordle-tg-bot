package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.data.WordLoader;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatBotProcessor {

    public String processMsg(String prevMsg , Update update , String[] word , boolean[] isGamePresent){
        System.out.println("Inside processMsg");
        String botAnswer="";

        if(update.hasMessage() && update.getMessage().hasText()){
            String currTxt = validateAndReturn(update.getMessage().getText().toLowerCase());
            System.out.println("currTxt: " + currTxt);
            if(currTxt == null || currTxt.isEmpty()) return prevMsg;
            // got a valid word , update it in the prevMsg
            else if(currTxt.equals(word[0])) {
                botAnswer = "You guessed the word " + word[0] + " correclty!";
                isGamePresent[0] = false;
            }
            else botAnswer = prevMsg + "\n" + updateMsg( currTxt , word);
        }
        return botAnswer;
    }

    public String validateAndReturn(String txtMsg ){
        System.out.println("inside validateAndReturn");
         if(txtMsg.length() != 5) return "";
         if (!txtMsg.matches("[a-zA-Z]+")) return "";

         ObjectMapper objectMapper = new ObjectMapper();
         List<String > validWords = WordLoader.loadWords("src/main/java/org/data/allWordsSorted.json");
         int idx = Collections.binarySearch(validWords, txtMsg);
         System.out.println("idx :" + idx);
         return (idx>=0)? txtMsg : "";
    }

    public String updateMsg( String currTxt , String[] word){
        System.out.println("Inside updateMsg");
        // RED EMOJI STRING
        String newMsg = "🟥🟥🟥🟥🟥 "+ currTxt.toUpperCase();
        Set<Character> s = new HashSet<Character>();

        // Matched letters
        for(int i=0;i<word[0].length();i++){
            s.add(word[0].charAt(i));
        }

        for(int i=0; i<word[0].length(); i++){
            // BLUE
            if(word[0].charAt(i) == currTxt.charAt(i)){
                String emoji = "\uD83D\uDFE6"; // blue emoji
                newMsg = replaceEmojiAt(newMsg , i , emoji);
            }
            // YELLOW
           else if(s.contains(currTxt.charAt(i))){
               String emoji = "\uD83D\uDFE8";
               newMsg = replaceEmojiAt(newMsg ,i , emoji);
           }
           // RED
           else {
               String emoji = "\uD83D\uDFE5";
               newMsg = replaceEmojiAt(newMsg , i , emoji);
           }
        }
        System.out.println("Returning from updateMsg");
        return newMsg;
    }
    public static String replaceEmojiAt(String original, int emojiIndex, String newEmoji) {
        int[] codePoints = original.codePoints().toArray(); // split into emoji/codepoints
        if (emojiIndex >= 0 && emojiIndex < codePoints.length) {
            codePoints[emojiIndex] = newEmoji.codePointAt(0); // replace code point
        }
        return new String(codePoints, 0, codePoints.length); // build back string
    }

}
