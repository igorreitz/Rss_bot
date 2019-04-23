package ru.reitz_rss_bot;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.IOException;
import java.util.*;

public class Main {
    //Блок с настройками приложения. TODO настройки вынести в отдельный файл
    static String botName = "reitz_rss_bot";
    static String botToken = "ВСТАВЬ СЮДА ТОКЕН";
    private static String RSSUrl = "https://news.mail.ru/rss/main/91/";

    /**
     * Точка входа
     *
     * @param args
     */
    public static void main(String[] args) {
        //Если провайдер блокирует доступ к телеграму, можно установить Tor-браузер,
        //запустить его и раскомментировать строчки ниже:
        //System.getProperties().put( "proxySet", "true" );
        //System.getProperties().put( "socksProxyHost", "127.0.0.1" );
        //System.getProperties().put( "socksProxyPort", "9150" );


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            //Подключаемся к Telegram
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

            //Первичное заполнение хранилища новостями
            RSSParser parser = new RSSParser();
            bot.feedList.addAll(parser.parseFeed(RSSUrl).getEntries());
            bot.feedList.sort(Comparator.comparing(SyndEntry::getPublishedDate));
            //Настраиваем и запускаем парсинг раз в MINUTES минут
            int MINUTES = 10; // Период запроса новостей из ленты
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<SyndEntry> parsedEntryList =
                                parser.parseFeed(RSSUrl).getEntries();
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
