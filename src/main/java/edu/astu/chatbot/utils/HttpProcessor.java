package edu.astu.chatbot.utils;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.java.Log;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.RequestBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.asynchttpclient.Dsl.asyncHttpClient;

@Log
@Component
public class HttpProcessor {
    private AsyncHttpClientConfig asyncHttpClientConfig;

    public HttpProcessor() {
        io.netty.handler.ssl.SslContext sc = null;
        try {
            sc = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        asyncHttpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .setCompressionEnforced(true)
                .setMaxConnections(100)
                .setPooledConnectionIdleTimeout(Duration.ofDays(20000))
                .setRequestTimeout(Duration.ofDays(20000))
                .setMaxConnectionsPerHost(5000)
                .setSslContext(sc)
                .build();
    }

    public String processProperRequest(RequestBuilder builder) {
        AtomicReference<String> responseBody = new AtomicReference<>(new String());
        try (AsyncHttpClient client = asyncHttpClient(asyncHttpClientConfig)) {
            client
                    .executeRequest(builder)
                    .toCompletableFuture()
                    .thenApply(response -> {
                        Logger.getLogger(this.getClass().getName()).log(Level.FINE, response.getResponseBody());
                        JSONObject jsObject = new JSONObject();
                        try {
                            if (response.getStatusCode() != 200) {
                                responseBody.set(response.getStatusText());
                                Logger.getLogger(this.getClass().getName()).log(Level.INFO, String.format("Received [HttpStatus = %d, Text = %s, URL = %s] processing halted!", response.getStatusCode(), response.getStatusText(), response.getRemoteAddress()));
                            } else {
                                responseBody.set(response.getResponseBody());
                                Logger.getLogger(this.getClass().getName()).log(Level.INFO, String.format("Received [HttpStatus = %d, Body = %s, URL = %s] processing halted!", response.getStatusCode(), response.getResponseBody(), response.getRemoteAddress()));
                            }
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("JSON Processing failed, [Message= %s, URL= %s]", e.getMessage(), ""));
                        }
                        return jsObject;
                    }).thenAccept(u -> Logger.getLogger(this.getClass().getName()).log(Level.FINE, u.toString())).join();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("Micro-Service Sending Failed,[Type= %s,Message= %s]", "", e.getMessage()));
        }
        return responseBody.get();
    }

    public JSONObject jsonRequestProcessor(RequestBuilder builder) {
        AtomicReference<JSONObject> responseBody = new AtomicReference<>(new JSONObject());
        try (AsyncHttpClient client = asyncHttpClient(asyncHttpClientConfig)) {
            client
                    .executeRequest(builder)
                    .toCompletableFuture()
                    .thenApply(response -> {
                        Logger.getLogger(this.getClass().getName()).log(Level.FINE, response.getResponseBody());
                        JSONObject jsObject = new JSONObject();
                        try {
                            responseBody.get().put("StatusCode", String.valueOf(response.getStatusCode()));
                            responseBody.get().put("StatusText", response.getStatusText());
                            responseBody.get().put("ResponseBody", response.getResponseBody());
                            Logger.getLogger(this.getClass().getName()).log(Level.INFO, String.format("Received [HttpStatus = %d, Text = %s, Body = %s] processing halted!", response.getStatusCode(), response.getStatusText(), response.getResponseBody()));
                        } catch (Exception e) {
                            responseBody.get().put("StatusCode", "999");
                            responseBody.get().put("StatusText", e.getMessage());
                            responseBody.get().put("ResponseBody", responseBody.get());
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("JSON Processing failed, [Message= %s, URL= %s]", e.getMessage(), ""));
                        }
                        return jsObject;
                    }).thenAccept(u -> Logger.getLogger(this.getClass().getName()).log(Level.FINE, u.toString())).join();
        } catch (Exception e) {
            responseBody.get().put("StatusCode", "999");
            responseBody.get().put("StatusText", String.format("Micro-Service Sending Failed,[Type= %s,Message= %s]", "", e.getMessage()));
            responseBody.get().put("ResponseBody", responseBody.get());
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("Micro-Service Sending Failed,[Type= %s,Message= %s]", "", e.getMessage()));
        }
        return responseBody.get();
    }
}
