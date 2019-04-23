package ru.reitz_rss_bot;

import java.io.IOException;
import java.net.URL;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class RSSParser {

    /**
     * Логгер
     */
    private static final Logger log = LoggerFactory.getLogger(RSSParser.class);

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
        log.info("Parsing of {}", url);
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

    /**
     * Метод для отображения записи ленты в нужном формате
     *
     * @param entry запись ленты
     * @return запись в виде строки
     */
    static String printEntry(SyndEntry entry, Bot bot) {
        String result = bot.index + 1 + "/" + bot.feedList.size() + " " + entry.getPublishedDate() + "\n"
                + entry.getAuthor() + "\n" + entry.getLink();
        log.info("News string generated: {}", entry.getPublishedDate() + " " + entry.getAuthor() + " " + entry.getLink());
        return result;
    }
}