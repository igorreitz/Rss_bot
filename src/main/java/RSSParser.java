import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URL;


import com.sun.syndication.feed.synd.SyndContent;

import com.sun.syndication.feed.synd.SyndEntry;

import com.sun.syndication.feed.synd.SyndFeed;

import com.sun.syndication.io.FeedException;

import com.sun.syndication.io.SyndFeedInput;

import com.sun.syndication.io.XmlReader;


public class RSSParser {

    SyndFeed parseFeed(String url)
            throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

    public static String printEntry(SyndEntry entry) {
        String resultString = entry.getTitle() + " - " + entry.getAuthor() + "\n" + entry.getLink();

/*        for (Object contobj : entry.getContents()) {
            SyndContent content = (SyndContent) contobj;
            resultString = resultString + content.getType() + "\n" + content.getValue() + "\n";
        }*/

        SyndContent content = entry.getDescription();

       // if (content != null)
           //resultString = resultString + content.getValue() + "\n";

        //resultString = resultString + entry.getPublishedDate() + "\n";


        return resultString;
    }
}