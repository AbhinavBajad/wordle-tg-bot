package org.example;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class CommandEnd {
    public String endCommand(Update update , TelegramClient telegramClient ,WordleInstance game) {
        System.out.println("Inside endCommand");
        if(!game.isPresent) {
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
                        game.isPresent = false;
                        return "Game ended , the word was " + game.gameWord  + " !  ";

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
