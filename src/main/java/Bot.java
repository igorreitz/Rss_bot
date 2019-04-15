import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Level;

public class Bot extends TelegramLongPollingBot {

    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getFrom().getFirstName()+": "+update.getMessage().getText());
        Message message = update.getMessage();
        if(message!= null && message.hasText()){
            if(message.getText().equals("/help")){
                sendMsg(message,"Привет");
            }
            else{
                sendMsg(message,"Я тест. робот");
            }
        }
    }

    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    /**
     * Метод для настройки сообщения и его отправки.

     * @param s Строка, которую необходимот отправить в качестве сообщения.
     */

    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    public String getBotUsername() {
        return "reitz_rss_bot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     * @return token для бота
     */
    public String getBotToken() {
        return "";
    }
}
