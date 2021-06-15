package com.fatec.scc.servico;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.scc.model.Imovel;

@Service
public interface ImovelServico{
	public Iterable<Imovel> findAll();
	
	public Imovel findByNome(String nome);

	public void deleteById(Long id);

	public Imovel findById(Long id);

	public ModelAndView saveOrUpdate(Imovel imovel);

	public String obtemEndereco(String cep);
}