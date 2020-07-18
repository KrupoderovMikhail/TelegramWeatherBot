package bot;

import model.Model;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final String USERNAME = "your_username";
    private static final String TOKEN = "your_token";

    /**
     * Метод для настройки сообщения и его отправки.
     * @param message
     * @param text
     */
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true); // Включить возможность разметки
        sendMessage.setChatId(message.getChatId().toString()); // определяем id чата, в который нужно отправить сообщение
        sendMessage.setReplyToMessageId(message.getMessageId()); // определяем id сообщения, на которое нужно ответить
        sendMessage.setText(text);
        try {
            setButtons(sendMessage); // добавить кнопки
            execute(sendMessage); // добавить сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для приема сообщений
     * @param update Содержит сообщение о пользовании
     */
    @Override
    public synchronized void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Привет!");
                    break;
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/settings":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                case "/weather":
                    sendMsg(message, "В каком городе вы хотите узнать прогноз погоды?");
                    break;
                case "Привет":
                    sendMsg(message, "Маме своей привет напиши кожаный ублюдок!");
                    break;
                default:
                    try {
                            sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден!");
                    }
            }
        }
    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup); // связать сообщения с клавиатурой
        replyKeyboardMarkup.setSelective(true); // показывать клавиатуру определенным пользователям
        replyKeyboardMarkup.setResizeKeyboard(true); // "подгонка" клавиатуры
        replyKeyboardMarkup.setOneTimeKeyboard(false); // не скрывать клавиатуру

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
//        List<KeyboardRow> keyboardRowList2 = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow(); // Строка клавиатуры (1-я)
//        KeyboardRow keyboardSecondRow = new KeyboardRow(); // Строка клавиатуры (2-я)

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));
        keyboardFirstRow.add(new KeyboardButton("/weather"));


        keyboardRowList.add(keyboardFirstRow); // добавить кнопку в список
        replyKeyboardMarkup.setKeyboard(keyboardRowList); // добавить лист в массив кнопок
//        replyKeyboardMarkup.setKeyboard(keyboardRowList2);
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации
     * @return USERNAME
     */
    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    /**
     * Метод возвращает токен бота для связи с сервером Telegram
     * @return TOKEN
     */
    @Override
    public String getBotToken() {
        return TOKEN;
    }
}