package info.kgeorgiy.ja.klinachev.crawler;

import info.kgeorgiy.java.advanced.crawler.AdvancedCrawlerTest;
import info.kgeorgiy.java.advanced.crawler.EasyCrawlerTest;
import info.kgeorgiy.java.advanced.crawler.HardCrawlerTest;
import info.kgeorgiy.java.advanced.crawler.Tester;

public class CustomTest extends Tester {


    public static void main(final String... args) {
        new Tester()
                .add("easy", EasyCrawlerTest.class)
                .add("hard", HardCrawlerTest.class)
                .add("advanced", AdvancedCrawlerTest.class)
                .run(args);
    }
}
