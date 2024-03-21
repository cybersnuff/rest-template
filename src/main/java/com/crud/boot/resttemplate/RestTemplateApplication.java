package com.crud.boot.resttemplate;

import com.crud.boot.resttemplate.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


public class RestTemplateApplication {
    static final String URL = "http://94.198.50.185:7081/api/users";
    static StringBuilder result = new StringBuilder();
    static RestTemplate restTemplate = new RestTemplate();
    static User saveUser = new User(3L, "James", "Brown", (byte) 29);
    static User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 29);
    static String deleteUrl = URL + "/3";
    static HttpHeaders headers = new HttpHeaders();

    public static String save(HttpHeaders headers, User userSave) {
        HttpEntity<User> save = new HttpEntity<>(userSave, headers);
        ResponseEntity<String> responseEntitySave = restTemplate.exchange(URL, HttpMethod.POST, save, String.class);
        return responseEntitySave.getBody();
    }

    public static String update(HttpHeaders headers, User userUpdate) {
        HttpEntity<User> update = new HttpEntity<>(userUpdate, headers);
        ResponseEntity<String> responseEntityUpdate = restTemplate.exchange(URL, HttpMethod.PUT, update, String.class);
        return responseEntityUpdate.getBody();
    }

    public static String delete(HttpHeaders headers, User userUpdate) {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntityDelete = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);
        return responseEntityDelete.getBody();

    }

    public static String getAllUsers() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        return String.join(";", Objects.requireNonNull(responseEntity.getHeaders().get("set-cookie")));
    }

    public RestTemplateApplication() {
        String sessionId = getAllUsers();
        headers.set("cookie", sessionId);
    }

    public static void main(String[] args) {
        RestTemplateApplication restTemplateApplication = new RestTemplateApplication();
        result
                .append(save(headers, saveUser))
                .append(update(headers, updatedUser))
                .append(delete(headers, updatedUser));
        System.out.println("Итоговый код \"" + result + "\" - состоит из " + result.length() + " символов");
    }
}





