package ru.reitz_rss_bot;

public class Main {

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

        BotUtil.start();
    }
}
