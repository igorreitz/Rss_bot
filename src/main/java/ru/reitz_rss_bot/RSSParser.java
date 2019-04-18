package ru.reitz_rss_bot;

import java.io.IOException;
import java.net.URL;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


class RSSParser {
    /**
     * Метод для парсинга RSS ленты
     *
     * @param url ссылка на ленту
     * @return объект SyndFeed (лента новостей)
     * @throws IllegalArgumentException
     * @throws FeedException
     * @throws IOException
     */
    SyndFeed parseFeed(String url) throws IllegalArgumentException, FeedException, IOException {
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

    /**
     * Метод для отображения записи ленты в нужном формате
     *
     * @param entry запись ленты
     * @return запись в виде строки
     */
    static String printEntry(SyndEntry entry) {
        return Bot.index + 1 + "/" + Bot.feedList.size() + " " + entry.getPublishedDate() + "\n"
                + entry.getAuthor() + "\n" + entry.getLink();
    }
}