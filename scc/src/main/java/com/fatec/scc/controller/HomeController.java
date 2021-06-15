package com.fatec.scc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	public ModelAndView menu() {
		return new ModelAndView("paginaMenu");
	}

	@GetMapping("/login")
	public ModelAndView autenticacao() {
		return new ModelAndView("paginaLogin");
	}

	@GetMapping("/proprietarios/menu")
	public ModelAndView menuProprietario() {
		return new ModelAndView("menuProprietario");
	}

	@GetMapping("/locatariosCompradores/menu")
	public ModelAndView menuLocatarioComprados() {
		return new ModelAndView("menuLocatarioComprador");
	}

	@GetMapping("/contratos/menu")
	public ModelAndView contratosMenu() {
		return new ModelAndView("contratosMenu");
	}

	@GetMapping("/corretor/menu")
	public ModelAndView corretoresMenu() {
		return new ModelAndView("corretoresMenu");
	}

	@GetMapping("/imovel/menu")
	public ModelAndView imoveisMenu() {
		return new ModelAndView("imoveisMenu");
	}
	
	@GetMapping("/cliente/cadastrar")
	public ModelAndView cadastrarCliente() {
		return new ModelAndView("cadastrarCliente");
	}
}
