package com.fatec.scc.controller;

import java.util.List;

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
import com.fatec.scc.servico.ImovelServico;
import com.fatec.scc.servico.LocacaoServico;
import com.fatec.scc.model.Cliente;
import com.fatec.scc.model.Imovel;
import com.fatec.scc.model.Locacao;

@Controller
@RequestMapping(path = "/sig")
public class LocacaoController {
	Logger logger = LogManager.getLogger(LocacaoController.class);
	@Autowired
	LocacaoServico locacaoServico;
	@Autowired
	private ImovelServico imovelServico;
	@Autowired
	private ClienteServico clienteServico;

	@GetMapping("/locacoes")
	public ModelAndView retornaFormDeConsultaTodasLocacoes() {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		modelAndView.addObject("locacoes", locacaoServico.findAll());
		return modelAndView;
	}

	@GetMapping("/locacao")
	public ModelAndView retornaFormDeCadastroDe(Locacao locacao) {
		ModelAndView mv = new ModelAndView("cadastrarLocacao");
		try {
			Imovel imovel = null;
			Cliente cliente = null;
			imovel = imovelServico.findById(locacao.getId());
			cliente = clienteServico.findByCpf(locacao.getCpf());
			List<Locacao> locacoes = locacaoServico.findByIdCpf(locacao.getId(), locacao.getCpf());
			boolean locacaoEmAberto = false;
			for(Locacao umaLocacao : locacoes) {
				if (umaLocacao.getDataFim() == null) {
					locacaoEmAberto = true;
				}
			}

			if ((imovel != null && cliente != null && locacoes == null) || (imovel != null && cliente != null && locacaoEmAberto == false)) {
				locacao.setDataInicio(locacao.getDataInicio());
				locacaoServico.saveOrUpdate(locacao);
				mv.addObject("message", "Locação registrada");
			} else {
				logger.info("======================> não achou imóvel/cliente no db");
				mv.addObject("message", "Imóvel/Cliente não localizado ou locação em aberto");
			}
		} catch (Exception e) {
			logger.info("erro nao esperado no cadastro de locação ===> " + e.getMessage());
		}
		return mv;
	}

	@GetMapping("/locacoes/{cpf}") // diz ao metodo que ira responder a uma requisicao do tipo get
	public ModelAndView retornaFormParaEditarLocacao(@PathVariable("cpf") String cpf) {
		ModelAndView modelAndView = new ModelAndView("atualizarLocacao");
		modelAndView.addObject("locacao", locacaoServico.findByCpf(cpf)); // o repositorio e injetado no controller
		return modelAndView; // addObject adiciona objetos para view
	}

	@GetMapping("/locacao/{id}")
	public ModelAndView excluirNoFormDeConsultaLocacao(@PathVariable("id") Long id) {
		locacaoServico.deleteById(id);
		logger.info(">>>>>> 1. servico de exclusao chamado para o id => " + id);
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		modelAndView.addObject("locacoes", locacaoServico.findAll());
		return modelAndView;
	}

	@PostMapping("/locacoes")
	public ModelAndView save(@Valid Locacao locacao, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		if (result.hasErrors()) {
			modelAndView.setViewName("cadastrarLocacao");
		} else {
			modelAndView = locacaoServico.saveOrUpdate(locacao);
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
		Locacao umaLocacao = locacaoServico.findById(id);
		umaLocacao.setCpf(locacao.getCpf());
		umaLocacao.setDataInicio(locacao.getDataInicio());
		umaLocacao.setDataFim(locacao.getDataFim());
		umaLocacao.setCep(locacao.getCep());
		modelAndView = locacaoServico.saveOrUpdate(umaLocacao);

		return modelAndView;
	}

}