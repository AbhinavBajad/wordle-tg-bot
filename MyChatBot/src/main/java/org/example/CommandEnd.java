package org.example;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class CommandEnd {
    public String endCommand(Update update , String[] word , TelegramClient telegramClient , boolean[] isGamePresent) {
        System.out.println("Inside endCommand");
        if(!isGamePresent[0]) {
            return "No game is running!  ";
        }

        else {
            User user = update.getMessage().getFrom();
            Long chatId = update.getMessage().getChatId();
            Long senderId = user.getId();

            GetChatAdministrators getAdmins = new GetChatAdministrators(chatId.toString());
            try {
                List<ChatMember> administrators = telegramClient.execute(getAdmins);

                for (ChatMember member : administrators) {
                    if (member.getUser().getId().equals(senderId)) {
                        System.out.println("Is administrator");
                        isGamePresent[0] = false;
                        return "Game ended , the word was " + word[0]  + " !  ";

                    } else {
                        return "Only administrators can end the game!  ";
                    }
                }

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return "Invalid Command!  ";
    }
}
