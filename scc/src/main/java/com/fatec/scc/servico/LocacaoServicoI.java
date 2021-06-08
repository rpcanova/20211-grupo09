package com.fatec.scc.servico;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.scc.model.Locacao;
import com.fatec.scc.model.LocacaoRepository;
import com.fatec.scc.model.Endereco;

@Service
public class LocacaoServicoI implements LocacaoServico {
	Logger logger = LogManager.getLogger(LocacaoServicoI.class);

	@Autowired
	private LocacaoRepository repository;

	public Iterable<Locacao> findAll() {
		return repository.findAll();
	}

	public Locacao findByCpf(String cpf) {
		return repository.findByCpf(cpf);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
		logger.info(">>>>>> 2. comando exclusao executado para o id => " + id);
	}

	public Locacao findById(Long id) {
		return repository.findById(id).get();
	}

	public ModelAndView saveOrUpdate(Locacao locacao) {
		ModelAndView modelAndView = new ModelAndView("consultarLocacao");
		try {
			String endereco = obtemEndereco(locacao.getCep());
			if (endereco != "") {
				locacao.setEndereco(endereco);
				repository.save(locacao);
				logger.info(">>>>>> 4. comando save executado ");
				modelAndView.addObject("locacoes", repository.findAll());
			}
		} catch (Exception e) {
			modelAndView.setViewName("cadastrarLocacao");
			if (e.getMessage().contains("could not execute statement")) {
				modelAndView.addObject("message", "Dados invalidos - locação já cadastrada.");
				logger.info(">>>>>> 5. locacao ja cadastrada ==> " + e.getMessage());
			} else {
				modelAndView.addObject("message", "Erro não esperado - contate o administrador");
				logger.error(">>>>>> 5. erro nao esperado ==> " + e.getMessage());
			}
		}
		return modelAndView;
	}
	
	@Override
	public String obtemEndereco(String cep) {
		RestTemplate template = new RestTemplate();
		String url = "https://viacep.com.br/ws/{cep}/json/";
		Endereco endereco = template.getForObject(url, Endereco.class, cep);
		logger.info(">>>>>> 3. obtem endereco ==> " + endereco.toString());
		return endereco.getLogradouro();
	}

	@Override
	public List<Locacao> findByIdCpf(Long id, String cpf) {
		// TODO Auto-generated method stub
		return null;
	}
}