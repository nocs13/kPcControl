package com.server.app;

import java.io.*;
import java.nio.file.Paths;
import java.net.*;


import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.*;

import com.server.app.WebHandler;

/**
 * kPcControl server.
 *
 */
public class App
{
  public static void main( String[] args )
  {
    int port = 8433;

    Settings settings = new Settings();

    String path = Paths.get(".").toAbsolutePath().normalize().toString();

    System.out.println("Current directory: " + path + ".\n");

    try {
      InetSocketAddress address = new InetSocketAddress(port);
      HttpsServer server = HttpsServer.create(address, 0);
      SSLContext ssl = SSLContext.getInstance("TLS");

      char[] password = "degenerat".toCharArray();
      KeyStore ks = KeyStore.getInstance("JKS");

      //FileInputStream fis = new FileInputStream("/pckey.jks");
      //FileInputStream fis = getClass().getResourceAsStream("/pckey.jks");
      FileInputStream fis = (FileInputStream) App.class.getClassLoader().getResourceAsStream("/pckey.jks");
      ks.load(fis, password);

      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, password);

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(ks);

      ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

      server.setHttpsConfigurator(new HttpsConfigurator(ssl) {
          public void configure(HttpsParameters par) {
            try {
              SSLContext sc = SSLContext.getDefault();
              SSLEngine  se = sc.createSSLEngine();

              par.setNeedClientAuth(false);
              par.setCipherSuites(se.getEnabledCipherSuites());
              par.setProtocols(se.getEnabledProtocols());

              SSLParameters defaultSSLParameters = sc.getDefaultSSLParameters();
              par.setSSLParameters(defaultSSLParameters);
            } catch (Exception e) {
              System.out.println("Failed to create HTTPS port");
              e.printStackTrace();
            }
          }
        });

      server.createContext("/test", new WebHandler());
      server.setExecutor(null); // creates a default executor

      System.out.println("Starting server...");
      server.start();

    } catch (Exception exception) {
      System.out.println("Failed to create HTTPS server on port " + port + " of localhost");
      exception.printStackTrace();
    }
  }
}
