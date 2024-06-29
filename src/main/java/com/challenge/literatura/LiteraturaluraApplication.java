package com.challenge.literatura;

import com.challenge.literatura.principal.Principal;
import com.challenge.literatura.repository.AutorRepository;
import com.challenge.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository,autorRepository);
		principal.muestraElMenu();
	}
}
