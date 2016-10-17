package com.hzy.mvc;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.common.base.Supplier;

public class Simulator implements Supplier<Simulator> {

    private static final int SIMULATOR_DEFAULT_PORT = 8085;
    private static final String SIMULATOR_CONTEXT = "/cdm-simulator";
    public static final int UT_PORT = 12800;

    private static final Logger LOGGER = Logger.getLogger(Simulator.class);

    private Server server;
    private String contextURL;

    public void start(String webappPath) {
        start(SIMULATOR_DEFAULT_PORT, SIMULATOR_CONTEXT, webappPath);
    }

    public void start(int port, String webappPath) {
        start(port, SIMULATOR_CONTEXT, webappPath);
    }

    public void start(int port, String contextPath, String webappPath) {
        startWithExtraClasspath(port, contextPath, webappPath, null);
    }

    public void startWithExtraClasspath(int port, String webappPath, String extraClasspath) {
        startWithExtraClasspath(port, SIMULATOR_CONTEXT, webappPath, extraClasspath);
    }

    public void startWithExtraClasspath(int port, String contextPath, String webappPath, String extraClasspath) {
        try {
            if (server != null) {
                server.stop();
            }
            server = new Server(port);
            WebAppContext webContext = new WebAppContext(webappPath, contextPath);
            if (extraClasspath != null) {
                webContext.setExtraClasspath(extraClasspath);
            }
            server.setHandler(webContext);
            server.setStopAtShutdown(true);
            server.start();
            this.contextURL = "http://127.0.0.1:" + port + contextPath;
        } catch (Exception e) {
            LOGGER.error("Start Jetty failed. Port: " + port, e);
            throw new IllegalStateException(e);
        }
    }

    public String getContextURL() {
        return contextURL;
    }

    public void stop() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            LOGGER.error("Stop Jetty failed.", e);
        }
    }


    public Simulator get() {
        return this;
    }
}
