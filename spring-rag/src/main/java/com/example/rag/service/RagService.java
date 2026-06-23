package com.example.rag.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchDocument;
import com.azure.search.documents.models.SearchResult;
import com.azure.search.documents.util.SearchPagedIterable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RagService {

    private final SearchClient searchClient;
    private final OpenAIClient openAIClient;

    @Value("${azure.openai.deployment}")
    private String deployment;

    public RagService(SearchClient searchClient,
                      OpenAIClient openAIClient) {

        this.searchClient = searchClient;
        this.openAIClient = openAIClient;
    }

    public String ask(String question) {

        try {

            StringBuilder context = new StringBuilder();

            SearchPagedIterable results =
                    searchClient.search(question);

            for (SearchResult result : results) {

                SearchDocument document =
                        result.getDocument(SearchDocument.class);

                Object content = document.get("content");

                if (content != null) {

                    context.append(content.toString())
                            .append("\n\n");
                }

                if (context.length() > 5000) {
                    break;
                }
            }

            List<ChatRequestMessage> messages =
                    new ArrayList<>();

            messages.add(
                    new ChatRequestSystemMessage(
                            "You are a helpful assistant. " +
                                    "Answer only using the provided context.\n\n" +
                                    "Context:\n" +
                                    context
                    )
            );

            messages.add(
                    new ChatRequestUserMessage(question)
            );

            ChatCompletionsOptions options =
                    new ChatCompletionsOptions(messages);

            options.setTemperature(0.2);
            options.setMaxTokens(800);

            ChatCompletions response =
                    openAIClient.getChatCompletions(
                            deployment,
                            options
                    );

            return response.getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

        } catch (Exception ex) {

            ex.printStackTrace();

            return "Error: " + ex.getMessage();
        }
    }
}