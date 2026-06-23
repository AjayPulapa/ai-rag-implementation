package com.example.rag.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {

    @Bean
    public SearchClient searchClient(
            @Value("${azure.search.endpoint}") String endpoint,
            @Value("${azure.search.key}") String key,
            @Value("${azure.search.index}") String index) {

        return new SearchClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .indexName(index)
                .buildClient();
    }
}
