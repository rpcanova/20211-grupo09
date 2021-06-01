package com.fatec.scc.servico;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.scc.model.Imovel;
import com.fatec.scc.model.ImovelRepository;
import com.fatec.scc.model.Endereco;

@Service
public class ImovelServicoI implements ImovelServico {
	Logger logger = LogManager.getLogger(ImovelServicoI.class);
	
	@Autowired
	private ImovelRepository repository;

	public Iterable<Imovel> findAll() {
		return repository.findAll();
	}

	public Imovel findByNome(String nome) {
		return repository.findByNome(nome);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
		logger.info(">>>>>> 2. comando exclusao executado para o id => " + id);
	}

	public Imovel findById(Long id) {
		return repository.findById(id).get();
	}

	public ModelAndView saveOrUpdate(Imovel imovel) {
		ModelAndView modelAndView = new ModelAndView("consultarImovel");
		try {
			String endereco = obtemEndereco(imovel.getCep());
			if (endereco != "") {
				imovel.setEndereco(endereco);
				repository.save(imovel);
				logger.info(">>>>>> 4. comando save executado ");
				modelAndView.addObject("imoveis", repository.findAll());
			}
		} catch (Exception e) {
			modelAndView.setViewName("cadastrarImovel");
			if (e.getMessage().contains("could not execute statement")) {
				modelAndView.addObject("message", "Dados invalidos - imóvel já cadastrado.");
				logger.info(">>>>>> 5. imóvel ja cadastrado ==> " + e.getMessage());
			} else {
				modelAndView.addObject("message", "Erro não esperado - contate o administrador");
				logger.error(">>>>>> 5. erro nao esperado ==> " + e.getMessage());
			}
		}
		return modelAndView;
	}

	public String obtemEndereco(String cep) {
		RestTemplate template = new RestTemplate();
		String url = "https://viacep.com.br/ws/{cep}/json/";
		Endereco endereco = template.getForObject(url, Endereco.class, cep);
		logger.info(">>>>>> 3. obtem endereco ==> " + endereco.toString());
		return endereco.getLogradouro();
	}
}