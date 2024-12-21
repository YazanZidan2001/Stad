package com.example.stad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StadApplication {
	public static void main(String[] args) {
		SpringApplication.run(StadApplication.class, args);
	}
}
