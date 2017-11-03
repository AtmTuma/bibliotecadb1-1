package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateException;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Emprestimo;
import br.com.db1.model.Reserva;
import br.com.db1.util.exception.ErroSistema;

public class EmprestimoDAO implements DAO<Emprestimo> {

	private EntityManager manager;
	
	@Inject
	public EmprestimoDAO (EntityManager manager) {
		this.manager = manager;
	}

	@SuppressWarnings("unchecked")
	public List<Emprestimo> findAll() throws ErroSistema {
		try {
			return manager.createQuery("Select u from Emprestimo u where u.ativo = true").getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar emprestimos", ex);
		}
	}

	public Emprestimo findById(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Emprestimo u where u.id = :pid and u.ativo = true");
			query.setParameter("pid", id);
			return (Emprestimo) query.getSingleResult();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar emprestimo por id", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Emprestimo> findByName(String name) throws ErroSistema {// Titulo livro
		try {
			Query query = manager.createQuery("Select u from Emprestimo u inner join u.livro l where upper(l.titulo) like :pnome and u.ativo = true");
			query.setParameter("pnome", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar pelo titulo do livro", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Emprestimo> findByNameUsuario(String name) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Emprestimo u inner join u.usuario s where upper(s.nome) like :pnome and u.ativo = true");
			query.setParameter("pnome", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar pelo nome do usuario", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Emprestimo> findByIdUsuario(Long id) throws ErroSistema {
		try {
			Query query = manager.createQuery("Select u from Emprestimo u inner join u.usuario s where s.id = :pid and u.ativo = true");
			query.setParameter("pid", id);
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar pelo id do usuario", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Emprestimo> findByIdUsuarioHistorico(Long id) throws ErroSistema {// historico do usuario
		try {
			Query query = manager.createQuery("Select u from Emprestimo u inner join u.usuario s where s.id = :pid and u.ativo = false");
			query.setParameter("pid", id);
			return query.getResultList();
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar buscar o historico de emprestimos pelo Id do usuario", ex);
		}
	}

	@Transactional
	public boolean save(Emprestimo t) throws ErroSistema {
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
	public boolean restate(Emprestimo t) throws ErroSistema {
		try {
			t.setAtivo(false);
			manager.merge(t);
		} catch (HibernateException ex) {
			throw new ErroSistema("Erro ao tentar desativar o emprestimo", ex);
		}
		return true;
	}

}
