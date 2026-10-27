package com.hltech.productservice.client;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.hltech.productservice.response.CategoryResponse;

@Component
public class CategoryClient {

    private static final String BASE_URL = "http://categoryservice";

    private final RestTemplate restTemplate;

    public CategoryClient(RestTemplate categoryRestTemplate) {
        this.restTemplate = categoryRestTemplate;
    }

    public List<CategoryResponse> getAllCategories() {
        try {
            CategoryResponse[] response = restTemplate.getForObject(
                BASE_URL + "/api/categories",
                CategoryResponse[].class
            );

            return response == null
                ? List.of()
                : Arrays.asList(response);

        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Unable to fetch categories from Category Service",
                ex
            );
        }
    }

    public void validateCategory(UUID categoryId) {
        try {
            restTemplate.getForEntity(
                BASE_URL + "/api/categories/{id}",
                Void.class,
                categoryId
            );

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Category ID does not exist"
            );

        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Unable to validate category",
                ex
            );
        }
    }
}