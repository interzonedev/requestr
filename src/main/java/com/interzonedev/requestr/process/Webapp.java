package com.interzonedev.requestr.process;

import com.interzonedev.herokusupport.client.DefaultHerokuSupportClient;
import com.interzonedev.herokusupport.client.HerokuSupportClient;
import com.interzonedev.herokusupport.webserver.WebServerParams;
import com.interzonedev.herokusupport.webserver.WebServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Webapp {

    private static final Logger log = LoggerFactory.getLogger(Webapp.class);

    public static void main(String[] args) throws Exception {

        log.debug("main: Launching Jetty server");

        HerokuSupportClient herokuSupportClient = new DefaultHerokuSupportClient();

        WebServerParams webServerParams = new WebServerParams(5000);
        herokuSupportClient.startWebServer(WebServerType.JETTY, webServerParams, null, null, null);

    }

}
