package com.fatec.scc.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImovelRepository extends CrudRepository<Imovel, Long>{
	public Imovel findByNome(@Param("nome") String nome);
}