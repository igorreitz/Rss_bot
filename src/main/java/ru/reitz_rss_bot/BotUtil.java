package ru.reitz_rss_bot;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class BotUtil {

    static void start() {
        ApiContextInitializer.init();
        Bot bot = new Bot();

        String RSSUrl = "";
        //Загружаем данные из файла конфигурации.
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File("src/main/resources/config.properties")));
            RSSUrl = properties.getProperty("RSSUrl");
            bot.setBotName(properties.getProperty("botName"));
            bot.setBotToken(properties.getProperty("botToken"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            //Подключаемся к Telegram
            telegramBotsApi.registerBot(bot);

            //Первичное заполнение хранилища новостями
            RSSParser parser = new RSSParser();
            bot.feedList.addAll(parser.parseFeed(RSSUrl).getEntries());
            bot.feedList.sort(Comparator.comparing(SyndEntry::getPublishedDate));
            //Настраиваем и запускаем парсинг раз в MINUTES минут
            int MINUTES = 10; // Период запроса новостей из ленты
            Timer timer = new Timer();
            String finalRSSUrl = RSSUrl;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<SyndEntry> parsedEntryList =
                                parser.parseFeed(finalRSSUrl).getEntries();
                        parsedEntryList.sort(Comparator.comparing(SyndEntry::getPublishedDate));
                        parsedEntryList.forEach(entry -> {
                            if (!bot.feedList.contains(entry)) {
                                bot.indexOfNewEntry = bot.feedList.size();
                                bot.hasNewEntry = true;
                                bot.feedList.add(entry);
                            }
                        });
                    } catch (FeedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000 * 60 * MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
