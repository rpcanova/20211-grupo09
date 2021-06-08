package com.fatec.scc.servico;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.scc.model.Locacao;

@Service
public interface LocacaoServico {
	public Iterable<Locacao> findAll();

	public Locacao findByCpf(String cpf);

	public void deleteById(Long id);

	public Locacao findById(Long id);

	public ModelAndView saveOrUpdate(Locacao locacao);

	public String obtemEndereco(String cep);

	public List<Locacao> findByIdCpf(Long id, String cpf);
}