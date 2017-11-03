package br.com.db1.dao;

import java.util.List;

import br.com.db1.util.exception.ErroSistema;

public interface DAO<T> {
	
	public List<T> findAll() throws ErroSistema;

	public T findById(Long id) throws ErroSistema;

	public List<T> findByName(String name) throws ErroSistema;

	public boolean save(T t) throws ErroSistema;

	public boolean restate(T t) throws ErroSistema;

}