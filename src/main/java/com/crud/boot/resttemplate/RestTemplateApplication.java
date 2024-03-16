package com.crud.boot.resttemplate;

import com.crud.boot.resttemplate.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class RestTemplateApplication {
    static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {

        StringBuilder result = new StringBuilder();
        RestTemplate restTemplate = new RestTemplate();
        User saveUser = new User(3L, "James", "Brown", (byte) 29);
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 29);
        String deleteUrl = URL + "/3";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        System.out.println("Список всех пользователей: " + responseEntity.getBody());

        List<String> setCookieHeaders = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);

        //вытащил свой session id
        String sessionId = null;
        if (setCookieHeaders != null) {
            for (String setCookieHeader : setCookieHeaders) {
                if (setCookieHeader.contains("SESSION")) {
                    sessionId = setCookieHeader.split(";")[0]; // Выбираем только значение до первой точки с запятой
                    break;
                }
            }
        }
        System.out.println("Сессия: " + sessionId);

        String session = sessionId;
        System.out.println(session);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId); // Установка заголовка Cookie с session id
        headers.setContentType(MediaType.APPLICATION_JSON);

        // попытка создания нового юзера
        HttpEntity<User> save = new HttpEntity<>(saveUser, headers);
        ResponseEntity<String> responseEntitySave = restTemplate.exchange(URL, HttpMethod.POST, save, String.class);
        result.append(responseEntitySave.getBody());

        //попытка апдейта
        HttpEntity<User> update = new HttpEntity<>(updatedUser, headers);
        ResponseEntity<String> responseEntityUpdate = restTemplate.exchange(URL, HttpMethod.PUT, update, String.class);
        result.append(responseEntityUpdate.getBody());

        //попытка удаления
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntityDelete = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);
        result.append(responseEntityDelete.getBody());

        System.out.println("Итоговый код \"" + result + "\" - состоит из " + result.length() + " символов");
    }
}