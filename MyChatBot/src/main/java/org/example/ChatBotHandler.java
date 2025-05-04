package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;

public class ChatBotHandler implements LongPollingSingleThreadUpdateConsumer {
    String botToken = "8137523531:AAFP4qiy_lqV5fouTtkyKPZLz1iTjQ0cKfM";
    private final TelegramClient telegramClient = new OkHttpTelegramClient(botToken);
    HashMap<Long , WordleInstance> map = new HashMap<>();

    public void consume(Update update){

        System.out.println("inside consume");
        System.out.println("Chat details: "+update.getMessage().getChat());
        System.out.println("User details: "+update.getMessage().getFrom());

        if(update.hasMessage() && update.getMessage().hasText()) {
            long chatID = update.getMessage().getChatId();
            WordleInstance game = map.get(chatID);

            if(game == null){
                game = new WordleInstance();
                map.put(chatID , game);
            }
            ChatBotProcessor chatBotProcessor = new ChatBotProcessor();
            String botAnswer = "";
            String currMsg = update.getMessage().getText();

            if (currMsg.equals(Commands.NEW) || currMsg.equals(Commands.NEWW)) {
                botAnswer = CommandNew.newCommand(game);
                System.out.println("The word is: "+ game.gameWord);
                game.isPresent = true;
            }
            else if (currMsg.equals(Commands.END) || currMsg.equals(Commands.ENDD)) {
                botAnswer= new CommandEnd().endCommand(update, telegramClient, game);
                if(!game.isPresent) {
                    game.LastMsg = "";
                    game.gameWord = "";
                }
            }
            else if(currMsg.charAt(0) == '/' && game.isPresent) botAnswer = Commands.INVALID;

            else if(game.isPresent){
                String prevMsg = game.LastMsg;
                currMsg = chatBotProcessor.processMsg(prevMsg, update, game);
                botAnswer = (currMsg.equals(prevMsg)) ? "Not a valid word" : currMsg;
                game.LastMsg = currMsg;
                if(!game.isPresent){
                    //end the game
                    game.LastMsg = "";
                    game.gameWord = "";
                }
            }
            // Storing the current state of the game
            map.put(chatID , game);

            System.out.println("botAnswer :" + botAnswer);
            if(!botAnswer.isEmpty()) {
                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chatID)
                        .replyToMessageId(update.getMessage().getMessageId())
                        .text(botAnswer)
                        .build();

                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
