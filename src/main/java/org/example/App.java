package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableJpaRepositories("org.example.repository")
public class App
{

    public static void main( String[] args ) throws TelegramApiException {

        SpringApplication.run(App.class, args);
    }
}
