package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.HibernateException;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Usuario;
import br.com.db1.util.exception.ErroSistema;

public class UsuarioDAO implements DAO<Usuario> {

	private EntityManager manager;

	@Inject
	public UsuarioDAO (EntityManager manager) {
		this.manager = manager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findAll() throws ErroSistema {
		try {
			return manager.createQuery("Select u from Usuario u where u.ativo = true").getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar usuarios", ex);
		}
	}

	public Usuario findById(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Usuario u where u.id = :pid and u.ativo = true");
			query.setParameter("pid", id);
			return (Usuario) query.getSingleResult();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar usuario por ID", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findByName(String name) throws ErroSistema {
		try {
			Query query = manager
					.createQuery("Select u from Usuario u where upper(u.nome) like :pnome and u.ativo = true");
			query.setParameter("pnome", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar usuario por nome", ex);
		}
	}

	public Usuario findByEmail(String email) throws ErroSistema {
		try {
			Query query = manager
					.createQuery("Select u from Usuario u where upper(u.email) like :pemail and u.ativo = true");
			query.setParameter("pemail", email.toUpperCase());
			return (Usuario) query.getSingleResult();
		} catch (NoResultException ex) {
			// throw new ErroSistema("Erro ao tentar buscar usuario por Email", ex);
			return null;
		}
	}

	@Transactional
	public boolean save(Usuario t) throws ErroSistema {
		try {
			if (t.getId() != null) {
				manager.merge(t);
			} else {
				manager.persist(t);
			}
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar salvar o usuario", ex);
		}
		return true;
	}

	@Transactional
	public boolean restate(Usuario t) throws ErroSistema {
		try {
			t.setAtivo(false);
			manager.merge(t);
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar desativar o usuario", ex);
		}
		return true;
	}

}
