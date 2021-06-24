package com.mbj.server;

import com.mbj.util.CLI;

public class CacheUnitDriver {

    public static void main(String[] args) {
        CLI cli = new CLI(System.in, System.out);
        Server server = new Server();
        cli.addPropertyChangeListener(server);
        new Thread(cli).start();
    }
}
