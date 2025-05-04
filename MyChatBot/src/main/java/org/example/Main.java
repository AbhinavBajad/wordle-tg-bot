package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String botToken = "8137523531:AAFP4qiy_lqV5fouTtkyKPZLz1iTjQ0cKfM";
        try(TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication()){
            botApplication.registerBot(botToken , new ChatBotHandler());
            System.out.println("ChatBot Started Successfully!");
            Thread.currentThread().join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}