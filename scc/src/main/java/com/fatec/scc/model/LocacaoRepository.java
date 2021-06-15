package com.fatec.scc.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LocacaoRepository extends CrudRepository<Locacao, Long>{
	public Locacao findByCpf(@Param("cpf") String cpf);
	public Locacao findByCep(@Param("cep") String cep);
}