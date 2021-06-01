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

import com.fatec.scc.servico.LocacaoServico;
import com.fatec.scc.model.Locacao;

@Controller
@RequestMapping(path = "/sig")
public class LocacaoController {
	Logger logger = LogManager.getLogger(LocacaoController.class);
	@Autowired
	LocacaoServico servico;

	@GetMapping("/locacoes")
	public ModelAndView retornaFormDeConsultaTodasLocacoes() {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		modelAndView.addObject("locacoes", servico.findAll());
		return modelAndView;
	}

	@GetMapping("/locacao")
	public ModelAndView retornaFormDeCadastroDe(Locacao locacao) {
		ModelAndView mv = new ModelAndView("cadastrarLocacao");
		mv.addObject("locacao", locacao);
		return mv;
	}

	@GetMapping("/locacoes/{cpf}") // diz ao metodo que ira responder a uma requisicao do tipo get
	public ModelAndView retornaFormParaEditarLocacao(@PathVariable("cpf") String cpf) {
		ModelAndView modelAndView = new ModelAndView("atualizarLocacao");
		modelAndView.addObject("locacao", servico.findByCpf(cpf)); // o repositorio e injetado no controller
		return modelAndView; // addObject adiciona objetos para view
	}

	@GetMapping("/locacao/{id}")
	public ModelAndView excluirNoFormDeConsultaLocacao(@PathVariable("id") Long id) {
		servico.deleteById(id);
		logger.info(">>>>>> 1. servico de exclusao chamado para o id => " + id);
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		modelAndView.addObject("locacoes", servico.findAll());
		return modelAndView;
	}

	@PostMapping("/locacoes")
	public ModelAndView save(@Valid Locacao locacao, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		if (result.hasErrors()) {
			modelAndView.setViewName("cadastrarLocacao");
		} else {
			modelAndView = servico.saveOrUpdate(locacao);
		}
		return modelAndView;
	}

	@PostMapping("/locacoes/{id}")
	public ModelAndView atualizaLocacao(@PathVariable("id") Long id, @Valid Locacao locacao, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		if (result.hasErrors()) {
			locacao.setId(id);
			return new ModelAndView("atualizarLocacao");
		}
		// programacao defensiva - deve-se verificar se o Cliente existe antes de
		// atualizar
		Locacao umaLocacao = servico.findById(id);
		umaLocacao.setCpf(locacao.getCpf());
		umaLocacao.setDataInicio(locacao.getDataInicio());
		umaLocacao.setDataFim(locacao.getDataFim());
		umaLocacao.setCep(locacao.getCep());
		modelAndView = servico.saveOrUpdate(umaLocacao);

		return modelAndView;
	}

}