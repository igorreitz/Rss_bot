import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Если провайдер блокирует доступ к телеграму, можно установить Tor-браузер,
        //запустить его и раскомментировать строчки ниже:
        //System.getProperties().put( "proxySet", "true" );
        //System.getProperties().put( "socksProxyHost", "127.0.0.1" );
        //System.getProperties().put( "socksProxyPort", "9150" );


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);
            RSSParser parser = new RSSParser();
            //Bot.feedList.addAll(parser.parseFeed("https://awas1952.livejournal.com/data/rss").getEntries());
            Bot.feedList.addAll(parser.parseFeed("https://news.mail.ru/rss/main/91/").getEntries());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
