package com.fabiankevin.springboot_kubernetes_deployment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

    @Value("${app.user.name:John Doe}")
    private String name;

    @GetMapping("/greet")
    public String greet(){
        return "Hello "+name;
    }
}
