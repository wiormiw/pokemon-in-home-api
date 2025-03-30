package com.wiormiw.pokemon_in_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PokemonInHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokemonInHomeApplication.class, args);
	}

}
