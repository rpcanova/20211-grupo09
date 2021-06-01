package com.fatec.scc.controller;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fatec.scc.servico.ClienteServico;
import com.fatec.scc.model.Cliente;

@Controller
@RequestMapping(path = "/sig")
public class ClienteController {
	Logger logger = LogManager.getLogger(ClienteController.class);
	@Autowired
	ClienteServico servico;

	@GetMapping("/clientes")
	public ModelAndView retornaFormDeConsultaTodosClientes() {
		ModelAndView modelAndView = new ModelAndView("consultarCliente");
		modelAndView.addObject("clientes", servico.findAll());
		return modelAndView;
	}

	@GetMapping("/cliente")
	public ModelAndView retornaFormDeCadastroDe(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cadastrarCliente");
		mv.addObject("cliente", cliente);
		return mv;
	}

	@GetMapping("/clientes/{cpf}") // diz ao metodo que ira responder a uma requisicao do tipo get
	public ModelAndView retornaFormParaEditarCliente(@PathVariable("cpf") String cpf) {
		ModelAndView modelAndView = new ModelAndView("atualizarCliente");
		modelAndView.addObject("cliente", servico.findByCpf(cpf)); // o repositorio e injetado no controller
		return modelAndView; // addObject adiciona objetos para view
	}

	@GetMapping("/cliente/{id}")
	public ModelAndView excluirNoFormDeConsultaCliente(@PathVariable("id") Long id) {
		servico.deleteById(id);
		logger.info(">>>>>> 1. servico de exclusao chamado para o id => " + id);
		ModelAndView modelAndView = new ModelAndView("consultarCliente");
		modelAndView.addObject("clientes", servico.findAll());
		return modelAndView;
	}

	@PostMapping("/clientes")
	public ModelAndView save(@Valid Cliente cliente, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarCliente");
		if (result.hasErrors()) {
			modelAndView.setViewName("cadastrarCliente");
		} else {
			modelAndView = servico.saveOrUpdate(cliente);
		}
		return modelAndView;
	}

	@PostMapping("/clientes/{id}")
	public ModelAndView atualizaCliente(@PathVariable("id") Long id, @Valid Cliente cliente, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarCliente");
		if (result.hasErrors()) {
			cliente.setId(id);
			return new ModelAndView("atualizarCliente");
		}
		// programacao defensiva - deve-se verificar se o Cliente existe antes de
		// atualizar
		Cliente umCliente = servico.findById(id);
		umCliente.setCpf(cliente.getCpf());
		umCliente.setNome(cliente.getNome());
		umCliente.setEmail(cliente.getEmail());
		umCliente.setCep(cliente.getCep());
		modelAndView = servico.saveOrUpdate(umCliente);

		return modelAndView;
	}
}