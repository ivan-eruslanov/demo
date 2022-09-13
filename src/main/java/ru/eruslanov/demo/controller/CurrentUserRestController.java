package ru.eruslanov.demo.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.eruslanov.demo.model.User;

import java.util.List;

@RestController
public class CurrentUserRestController {
    private final RestTemplate restTemplate;

    public CurrentUserRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getHackingCode() {

        String url = "http://94.198.50.185:7081/api/users";
        String fCode = "";

        /* First */
        ResponseEntity<List <User>> response =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});

        /* Second */
        String cookies = response.getHeaders().getValuesAsList("Set-Cookie").get(0);
        String jsessionid = cookies.substring(cookies.indexOf("JSESSIONID="), cookies.indexOf(";"));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Cookie", jsessionid);

        /* Third */
        User newUser = new User(3L, "James", "Brown", (byte) 38);
        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> response1 = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        fCode += response1.getBody();

        /* Four */
        User user = new User(3L, "Thomas", "Shelby", (byte) 38);
        HttpEntity<User> entity1 = new HttpEntity<>(user, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.PUT, entity1, String.class);
        fCode += response2.getBody();

        /* Five */
        HttpEntity<User> entity2 = new HttpEntity<>(headers);
        ResponseEntity<String> response3 = restTemplate.exchange(url + "/3", HttpMethod.DELETE, entity2, String.class);
        fCode += response3.getBody();
        System.out.println("CODE - " + fCode);
        return fCode;
    }
}
