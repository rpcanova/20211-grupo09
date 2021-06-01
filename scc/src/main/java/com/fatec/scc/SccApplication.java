package com.fatec.scc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fatec.scc.model.Usuario;
import com.fatec.scc.model.UsuarioRepository;

@SpringBootApplication
public class SccApplication {
	Logger logger = LogManager.getLogger(SccApplication.class);
	@Autowired
	UsuarioRepository repository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SccApplication.class, args);
	}

	@Autowired
	public void inicializa() {
		Usuario user = new Usuario();
		user.setNome("User");
		user.setLogin("user");
		user.setSenha(passwordEncoder.encode("123"));
		repository.save(user);
		Usuario usuario = repository.findByLogin("user");
		logger.info(">>>>>> inicializacao da aplicacao => " + usuario.toString());
	}
}