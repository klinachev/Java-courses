module info.kgeorgiy.ja.klinachev.crawler {
    requires transitive info.kgeorgiy.java.advanced.crawler;

    exports info.kgeorgiy.ja.klinachev.crawler;

    opens info.kgeorgiy.ja.klinachev.crawler to junit;
}