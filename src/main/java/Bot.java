import com.sun.syndication.feed.synd.SyndEntry;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static List<SyndEntry> feedList = new ArrayList<>();

    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        //System.out.println(update.getMessage().getFrom().getFirstName() + ": " + update.getMessage().getText());
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                case "Далее":
                    sendMsg(message, getFirstEntry());
                    break;
                case "Сначала":
                    sendMsg(message, "Сначала");
                    break;
                case "Сначала*":
                    sendMsg(message, "Сначала!");
                    break;
                default:
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
     * @param s Строка, которую необходимот отправить в качестве сообщения.
     */

    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(s);
        setButtons(sendMessage, !s.equals("Далее"));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    public String getBotUsername() {
        return "reitz_rss_bot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    public String getBotToken() {
        return "";
    }

    public String getFirstEntry() {
        String s = RSSParser.printEntry(feedList.get(feedList.size() - 1));
        feedList.remove(feedList.size() - 1);
        return s;
    }

    public synchronized void setButtons(SendMessage sendMessage, boolean hasNewEntry) {
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
