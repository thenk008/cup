package org.wlld.web;

import org.wlld.web.WebIo.ReadConfig;
import org.wlld.web.WebIo.Web;
import org.wlld.web.server.HttpServer;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        ReadConfig.getConfig().readConfig();
        Web web = new Web();
        web.start();
        HttpServer server = new HttpServer();
        server.connect();
    }
}
