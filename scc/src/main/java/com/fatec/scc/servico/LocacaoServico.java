package com.fatec.scc.servico;

import org.springframework.web.servlet.ModelAndView;
import com.fatec.scc.model.Locacao;

public interface LocacaoServico {

	public Iterable<Locacao> findAll();

	public Locacao findByCpf(String cpf);

	public void deleteById(Long id);

	public Locacao findById(Long id);

	public ModelAndView saveOrUpdate(Locacao locacao);

	public String obtemEndereco(String cep);
}