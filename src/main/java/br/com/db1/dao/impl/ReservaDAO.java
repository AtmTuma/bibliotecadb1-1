package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateException;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Reserva;
import br.com.db1.util.exception.ErroSistema;

public class ReservaDAO implements DAO<Reserva> {

	private EntityManager manager;

	@Inject
	public ReservaDAO (EntityManager manager) {
		this.manager = manager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Reserva> findAll() throws ErroSistema {
		try {
			return manager.createQuery("Select u from Reserva u where u.ativo = true").getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar reservas", ex);
		}
	}

	public Reserva findById(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Reserva u where u.id = :pid and u.ativo = true");
			query.setParameter("pid", id);
			return (Reserva) query.getSingleResult();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar reserva por id", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Reserva> findByName(String name) throws ErroSistema {// reservas pelo titulo do Livro
		try {
			Query query = manager.createQuery("Select u from Reserva u inner join u.livro l where upper(l.titulo) like :pnome and u.ativo = true");
			query.setParameter("pnome", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar reserva pelo titulo do livro", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Reserva> findByIdUsuario(Long id) throws ErroSistema {//reservas ativas do usuario
		try {
			Query query = manager.createQuery("Select u from Reserva u inner join u.usuario s where s.id = :pid and u.ativo = true");
			query.setParameter("pid", id);
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar reserva pelo Id do usuario", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Reserva> findByIdUsuarioHistorico(Long id) throws ErroSistema {// historico do usuario
		try {
			Query query = manager.createQuery("Select u from Reserva u inner join u.usuario s where s.id = :pid and u.ativo = false");
			query.setParameter("pid", id);
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar o historico de reservas pelo Id do usuario", ex);
		}
	}

	@Transactional
	public boolean save(Reserva t) throws ErroSistema {
		try {
			if (t.getId() != null) {
				manager.merge(t);
			} else {
				manager.persist(t);
			}
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar salvar o emprestimo", ex);
		}
		return true;
	}

	@Transactional
	public boolean restate(Reserva t) throws ErroSistema {
		try {
			t.setAtivo(false);
			manager.merge(t);
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar desativar a reserva", ex);
		}
		return true;
	}

}
