package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateException;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Imagem;
import br.com.db1.model.Livro;
import br.com.db1.util.exception.ErroSistema;

public class LivroDAO implements DAO<Livro> {

	private EntityManager manager;

	@Inject
	public LivroDAO (EntityManager manager) {
		this.manager = manager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Livro> findAll() throws ErroSistema {
		try {
			return manager.createQuery("Select u from Livro u where u.ativo = true").getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar livros", ex);
		}
	}

	public Livro findById(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Livro u where u.id = :pId and u.ativo = true");
			query.setParameter("pId", id);
			return (Livro) query.getSingleResult();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar livro por ID", ex);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Livro> findByName(String name) throws ErroSistema {// titulo do livro
		try {
			Query query = manager.createQuery("Select u from Livro u where u.titulo like :pTitulo and u.ativo = true");
			query.setParameter("pTitulo", "%" + name + "%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar livro pelo titulo", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Livro> findByAuthor(String author) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Livro u where u.autor like :pAutor and u.ativo = true");
			query.setParameter("pAutor", "%" + author + "%");
			return query.getResultList();
		} catch (Exception ex) {
			throw new ErroSistema("Erro ao tentar buscar livro pelo autor", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Livro> findByCategory(String category) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Livro u where u.categoria like :pCategoria and u.ativo = true");
			query.setParameter("pCategoria", "%" + category + "%");
			return query.getResultList();
		} catch (Exception ex) {
			throw new ErroSistema("Erro ao tentar buscar livro pela categoria", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Imagem> findByAllImagem(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Imagem u inner join u.livro l where l.id = :pid");
			query.setParameter("pid", id);
			return query.getResultList();
		} catch (Exception ex) {
			throw new ErroSistema("Erro ao tentar buscar livro pela categoria", ex);
		}
	}

	@Transactional
	public boolean save(Livro t) throws ErroSistema {
		try {
			if (t.getId() != null) {
				manager.merge(t);
			} else {
				manager.persist(t);
			}
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar salvar o livro", ex);
		}
		return true;
	}

	@Transactional
	public boolean restate(Livro t) throws ErroSistema {
		try {
			t.setAtivo(false);
			manager.merge(t);
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar desativar o livro", ex);
		}
		return true;
	}

}
