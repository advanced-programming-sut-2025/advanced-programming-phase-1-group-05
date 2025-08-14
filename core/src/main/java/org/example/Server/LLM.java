package org.example.Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.example.Common.DataTransferObjects.ChatMessage;

import java.io.IOException;
import java.util.Collections;


public class LLM {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = System.getenv("API_KEY");

    public static String generateResponse(String prompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "mistralai/mistral-7b-instruct:free");
        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a helpful assistant.");
        messages.add(systemMessage);

        JsonObject userMessageObj = new JsonObject();
        userMessageObj.addProperty("role", "user");
        userMessageObj.addProperty("content", prompt);
        messages.add(userMessageObj);

        requestBody.add("messages", messages);

        RequestBody body = RequestBody.create(
            MediaType.get("application/json"),
            requestBody.toString()
        );

        Request request = new Request.Builder()
            .url(API_URL)
            .header("Authorization", "Bearer " + API_KEY)
            .post(body)
            .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray choices = jsonObject.getAsJsonArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JsonObject firstChoice = choices.get(0).getAsJsonObject();
                JsonObject message = firstChoice.getAsJsonObject("message");
                if (message != null) {
                    return message.get("content").getAsString();
                }
            }
            return "teehee";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
