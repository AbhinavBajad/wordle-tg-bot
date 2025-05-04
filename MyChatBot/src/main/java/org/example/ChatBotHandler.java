package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class ChatBotHandler implements LongPollingSingleThreadUpdateConsumer {
    String botToken = "8137523531:AAFP4qiy_lqV5fouTtkyKPZLz1iTjQ0cKfM";
    private final TelegramClient telegramClient = new OkHttpTelegramClient(botToken);
    String lastMsg = "";
    boolean[] isGamePresent = {false};
    String[] word = {""};

    public void consume(Update update){
        System.out.println("inside consume");
        String prevMsg = lastMsg;
        System.out.println("Chat details: "+update.getMessage().getChat());
        System.out.println("User details: "+update.getMessage().getFrom());
        if(update.getMessage().getChat().getUserName() == null ||
                !update.getMessage().getChat().getUserName().equals("happy_jais_homes")) return;
        if(update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Inside if condition");
            long chatID = update.getMessage().getChatId();
            ChatBotProcessor chatBotProcessor = new ChatBotProcessor();
            String botAnswer = "";
            String currMsg = update.getMessage().getText();

            if (currMsg.equals(Commands.NEW) || currMsg.equals(Commands.NEWW)) {
                botAnswer = CommandNew.newCommand(isGamePresent , word);
                System.out.println("The word is: "+ word[0]);
                isGamePresent[0] = true;
            }
            else if (currMsg.equals(Commands.END) || currMsg.equals(Commands.ENDD)) {
                botAnswer= new CommandEnd().endCommand(update, word, telegramClient, isGamePresent);
                if(!isGamePresent[0]) {
                    prevMsg = "";
                    lastMsg = "";
                    word[0] = "";
                }
            }
            else if(currMsg.charAt(0) == '/' && isGamePresent[0]) botAnswer = Commands.INVALID;

            else if(isGamePresent[0]){
                currMsg = chatBotProcessor.processMsg(prevMsg, update, word ,isGamePresent);
                botAnswer = (currMsg.equals(prevMsg)) ? "Not a valid word" : currMsg;
                lastMsg = currMsg;
                if(!isGamePresent[0]){
                    //end the game
                    prevMsg = "";
                    lastMsg = "";
                    word[0] = "";
                }
            }

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
