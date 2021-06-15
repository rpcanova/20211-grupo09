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

import com.fatec.scc.servico.ImovelServico;
import com.fatec.scc.model.Imovel;

@Controller
@RequestMapping(path = "/sig")
public class ImovelController {
	Logger logger = LogManager.getLogger(ImovelController.class);
	@Autowired
	ImovelServico servico;
	
	@GetMapping("/imoveis")
	public ModelAndView retornaFormDeConsultaTodosImoveis() {
		ModelAndView modelAndView = new ModelAndView("consultarImovel");
		modelAndView.addObject("imoveis", servico.findAll());
		return modelAndView;
	}
	
	@GetMapping("/imovel")
	public ModelAndView retornaFormDeCadastroDe(Imovel imovel) {
		ModelAndView mv = new ModelAndView("cadastrarImovel");
		mv.addObject("imovel", imovel);
		return mv;
	}

	@GetMapping("/imoveis/{nome}") // diz ao metodo que ira responder a uma requisicao do tipo get
	public ModelAndView retornaFormParaEditarImovel(@PathVariable("nome") String nome) {
		ModelAndView modelAndView = new ModelAndView("atualizarImovel");
		modelAndView.addObject("imovel", servico.findByNome(nome)); // o repositorio e injetado no controller
		return modelAndView; // addObject adiciona objetos para view
	}

	@GetMapping("/imovel/{id}")
	public ModelAndView excluirNoFormDeConsultaImovel(@PathVariable("id") Long id) {
		servico.deleteById(id);
		logger.info(">>>>>> 1. servico de exclusao chamado para o id => " + id);
		ModelAndView modelAndView = new ModelAndView("consultarImovel");
		modelAndView.addObject("imoveis", servico.findAll());
		return modelAndView;
	}

	@PostMapping("/imoveis")
	public ModelAndView save(@Valid Imovel imovel, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarImovel");
		if (result.hasErrors()) {
			modelAndView.setViewName("cadastrarImovel");
		} else {
			modelAndView = servico.saveOrUpdate(imovel);
		}
		return modelAndView;
	}

	@PostMapping("/imoveis/{id}")
	public ModelAndView atualizaImovel(@PathVariable("id") Long id, @Valid Imovel imovel, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarImovel");
		if (result.hasErrors()) {
			imovel.setId(id);
			return new ModelAndView("atualizarImovel");
		}
		// programacao defensiva - deve-se verificar se o Cliente existe antes de
		// atualizar
		Imovel umImovel = servico.findById(id);
		umImovel.setNome(imovel.getNome());
		umImovel.setDescricao(imovel.getDescricao());
		umImovel.setValor(imovel.getValor());
		umImovel.setCep(imovel.getCep());
		modelAndView = servico.saveOrUpdate(umImovel);

		return modelAndView;
	}
}