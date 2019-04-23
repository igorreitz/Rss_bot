package ru.reitz_rss_bot;

import com.sun.syndication.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    /**
     * Логгер
     */
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    /**
     * Имя бота
     */
    private String botName = "";
    /**
     * Токен бота
     */
    private String botToken = "";
    /**
     * Список с записями ленты новостей
     */
    List<SyndEntry> feedList = new ArrayList<>();
    /**
     * Индекс паказываемой новости
     */
    int index = 0;
    /**
     * Индекс первой новой новости
     */
    int indexOfNewEntry = 0;
    /**
     * Признак наличия новых записей в списке feedList
     */
    boolean hasNewEntry = false;

    void setBotName(String botName) {
        this.botName = botName;
    }

    void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    log.info("Message /start received");
                case "Далее":
                    log.info("Message next received");
                    sendMsg(message, getNextEntry());
                    break;
                case "Сначала":
                    log.info("Message get_first received");
                    sendMsg(message, getFirstEntry());
                    break;
                case "Сначала*":
                    log.info("Message get_first* received");
                    sendMsg(message, getFirstEntryWithAsterisk());
                    break;
                default:
                    log.info("Unknown command received");
                    sendMsg(message, "Неизвестная команда.");
                    break;
            }
        }
    }

    /**
     * Метод для приема сообщений.
     *
     * @param updates Содержит список сообщений от пользователя.
     */
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    /**
     * Метод для настройки сообщения и его отправки.
     *
     * @param s Строка, которую необходимо отправить в качестве сообщения.
     */
    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(s);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram connection error");
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    public String getBotUsername() {
        return botName;
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    public String getBotToken() {
        return botToken;
    }

    /**
     * Обработка кнопки Далее
     *
     * @return строку-ответ
     */
    private String getNextEntry() {
        if (index >= feedList.size())
            index = 0;

        String s = RSSParser.printEntry(feedList.get(index),this);
        index++;

        return s;
    }

    /**
     * Обработка кнопки Сначала
     *
     * @return строку-ответ
     */
    private String getFirstEntry() {
        index = 0;
        return getNextEntry();
    }

    /**
     * Обработка кнопки Сначала*
     *
     * @return строку-ответ
     */
    private String getFirstEntryWithAsterisk() {
        index = indexOfNewEntry;
        hasNewEntry = false;
        return getNextEntry();
    }

    /**
     * Метод для настройки кастомной клавиатуры в чате
     *
     * @param sendMessage
     */
    private synchronized void setButtons(SendMessage sendMessage) {
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
        keyboardFirstRow.add(new KeyboardButton("Далее"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        if (hasNewEntry) {
            keyboardSecondRow.add(new KeyboardButton("Сначала*"));
        } else {
            keyboardSecondRow.add(new KeyboardButton("Сначала"));
        }

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
