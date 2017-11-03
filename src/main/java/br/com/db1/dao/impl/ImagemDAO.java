package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateException;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Imagem;
import br.com.db1.util.exception.ErroSistema;

public class ImagemDAO implements DAO<Imagem>{

	private EntityManager manager;
	
	@Inject
	public ImagemDAO (EntityManager manager) {
		this.manager = manager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Imagem> findAll() throws ErroSistema {
		try {
			return manager.createQuery("Select u from Imagem u").getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar a imagem do livro", ex);
		}
	}

	public Imagem findById(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Imagem u where u.id = :pId");
			query.setParameter("pId", id);
			return (Imagem) query.getSingleResult();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar imagem por ID", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Imagem> findByName(String name) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Imagem u where upper(u.nome) like :pName");
			query.setParameter("pName", "%"+ name.toUpperCase() +"%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar imagem por nome", ex);
		}
	}

	@Transactional
	public boolean save(Imagem t) throws ErroSistema {
		try {
			if (t.getId() != null) {
				manager.merge(t);
			} else {
				manager.persist(t);
			}
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar salvar a imagem", ex);
		}
		return true;
	}

	@Transactional
	public boolean restate(Imagem t) throws ErroSistema {//neste aqui vai ter o delete mesmo da imagem
		Imagem imagem = findById(t.getId());
		try {
			if(imagem != null) {
				manager.remove(imagem);
			}
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar deletar a imagem", ex);
		}
		return true;
	}

}
