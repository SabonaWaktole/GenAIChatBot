package edu.astu.chatbot.integration;

import edu.astu.chatbot.utils.HttpProcessor;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.RequestBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeminiAIChatbotAdapter {
    private final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
    private final String apiKey = "AIzaSyCgDpxSDYyHzJoqkPdTe_chMCjvJ5GI29Q";

    private final HttpProcessor httpProcessor;

    public String processRequest(String userRequest) {

        // Create the JSON body using JSONObject and JSONArray
        JSONObject jsonBody = new JSONObject();
        JSONArray contentsArray = new JSONArray();

        // Create the inner structure of "parts"
        JSONObject partObject = new JSONObject();
        partObject.put("text", userRequest);

        // Create the content object and add the "parts" array
        JSONObject contentObject = new JSONObject();
        contentObject.put("parts", new JSONArray().put(partObject));

        // Add the content object to the contents array
        contentsArray.put(contentObject);

        // Add the "contents" array to the final JSON body
        jsonBody.put("contents", contentsArray);

        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(apiUrl)
                .addQueryParam("key", apiKey)
                .setBody(jsonBody.toString())
                .build();

        JSONObject resp = httpProcessor.jsonRequestProcessor(builder);

        if (resp.getString("StatusCode").equals("200")) {
            JSONObject resBody = new JSONObject(resp.getString("ResponseBody"));
            // Access the text
            return resBody.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } else {
            throw new RuntimeException("Failed to generate response.");
        }
    }
}
