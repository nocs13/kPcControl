package com.server.app;

import java.io.*;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.*;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;

public class WebHandler implements HttpHandler
{
  @Override
  public void handle(HttpExchange t) throws IOException
  {
    String response = "This is the response";
    HttpsExchange httpsExchange = (HttpsExchange) t;
    t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    t.sendResponseHeaders(200, response.length());
    OutputStream os = t.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
