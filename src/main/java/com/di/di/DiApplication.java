package com.di.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.di.di.domain.DiContainer;

@SpringBootApplication
public class DiApplication {

	public static void main(String[] args) throws Exception {

		DiContainer.autoRegister(DiApplication.class);

		SpringApplication.run(DiApplication.class, args);
	}

}
