package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
private String botUserName;
    @Value("${bot.token}")
private String botToken;

    @Autowired
    ResourceLoader resourceLoader;



    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }


    public void sendPhoto(String chatId, String filename, String caption) throws TelegramApiException {
        try {
            Resource photoResource = resourceLoader.getResource("classpath:" + filename);
            InputStream inputStream = photoResource.getInputStream();

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(inputStream,"photo.jpg")); // Set the photo using the input stream
            sendPhoto.setCaption(caption);

            execute(sendPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        sendMessage.setReplyMarkup(setButtons(sendMessage));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        String chatId = message.getChat().getId().toString();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("photo_pun_good.jpg");


        long userId = message.getFrom().getId();
        if (text.equals("/start")) {
            sendMsg(chatId, "Привет, ты грустишь?");
        } else if (text.equals("ДА") || text.equals("да") || text.equals("Да")) {
            try {
                sendPhoto(chatId, "photo_pun.jpg","Не грусти!! Ты лучше всех)))");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (text.equals("Нет") || text.equals("нет") || text.equals("НЕТ")) {
            try {
                sendPhoto(chatId, "photo_pun_good.jpg","Вот и славненько");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            sendText(userId, "Ничего не понятно, но очень интересно.");

        }
        System.out.println(message.getText());
    }

    public ReplyKeyboardMarkup setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("ДА"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("НЕТ"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
