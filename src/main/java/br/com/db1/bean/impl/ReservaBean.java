package br.com.db1.bean.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import br.com.db1.bean.BEAN;
import br.com.db1.dao.impl.LivroDAO;
import br.com.db1.dao.impl.ReservaDAO;
import br.com.db1.enumeration.StatusLivro;
import br.com.db1.enumeration.StatusReserva;
import br.com.db1.model.Livro;
import br.com.db1.model.Reserva;
import br.com.db1.model.Usuario;
import br.com.db1.util.exception.ErroSistema;

@RequestScoped
@Named
public class ReservaBean extends BEAN<Reserva, ReservaDAO> {

	private EntityManager manager;

	private ReservaDAO reservaDao;

	private LivroDAO livroDao;

	private List<Reserva> minhasReservas;

	@Inject
	public ReservaBean(ReservaDAO reservaDao, LivroDAO livroDao) {
		this.reservaDao = reservaDao;
		this.livroDao = livroDao;
	}

	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		Usuario usr = ((Usuario) session.getAttribute("usuario"));
		buscar("", "");
		try {
			minhasReservas = getDAO().findByIdUsuario(usr.getId());
		} catch (ErroSistema e) {
			e.printStackTrace();
		}
		novo();
	}

	@Override
	public ReservaDAO getDAO() {
		if (reservaDao == null) {
			reservaDao = new ReservaDAO(manager);
		}
		return reservaDao;
	}
	
	public String cancelarReserva(Reserva reserva) throws ErroSistema {
		this.setEntidade(reserva);
		this.desativar();
		List<Reserva> reservasDoLivro = reservaDao.findByName(reserva.getLivro().getTitulo());
		if (reservasDoLivro.isEmpty()) {
			reserva.getLivro().setStatus(StatusLivro.DISPONIVEL);
			livroDao.save(reserva.getLivro());
		}
		return caminhoDesativarEntidade();
	}

	@Override
	public Reserva criaNovaEntidade() {
		return new Reserva();
	}

	@Override
	public String caminhoNovaEntidade() {
		return "";
	}

	@Override
	public String caminhoSalvarEntidade() {
		return "acervoColaborador";
	}

	@Override
	public String caminhoEditarEntidade(Reserva reserva) {
		return "";
	}

	@Override
	public String caminhoDesativarEntidade() {
		return "";
	}

	@Override
	public void buscar(String value, String type) {
		try {
			if (value.isEmpty()) {
				setEntidades(getDAO().findAll());
			} else if ("titulo".equals(type)) {
				setEntidades(getDAO().findByName(value));
			} else if ("usuarioId".equals(type)) {
				setEntidades(getDAO().findByIdUsuario(Long.parseLong(value)));
			} else if ("historico".equals(type)) {
				setEntidades(getDAO().findByIdUsuarioHistorico(Long.parseLong(value)));
			}
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void reservarLivro(Livro livro) throws ErroSistema {
		this.novo();
		List<Reserva> reservasDoLivro = reservaDao.findByName(livro.getTitulo());
		getEntidade().setLivro(livro);
		getEntidade().setDataReserva(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		getEntidade().setPrazoRetirada(7);
		if (reservasDoLivro.isEmpty()) {
			livro.setStatus(StatusLivro.EMPRESTADO);
			livroDao.save(livro);
			getEntidade().setStatusReserva(StatusReserva.AGUARDANDO_RETIRADA);
			adicionarMensagem("Reservado com sucesso! Voce tem 7 dias para retira-lo.", FacesMessage.SEVERITY_INFO);
		} else {
			getEntidade().setStatusReserva(StatusReserva.LISTA_ESPERA);
			adicionarMensagem("Reservado com sucesso! Voce esta na lista de espera.", FacesMessage.SEVERITY_INFO);
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		getEntidade().setUsuario((Usuario) session.getAttribute("usuario"));
		this.salvar();
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public ReservaDAO getReservaDao() {
		return reservaDao;
	}

	public void setReservaDao(ReservaDAO reservaDao) {
		this.reservaDao = reservaDao;
	}

	public LivroDAO getLivroDao() {
		return livroDao;
	}

	public void setLivroDao(LivroDAO livroDao) {
		this.livroDao = livroDao;
	}

	public List<Reserva> getMinhasReservas() {
		return minhasReservas;
	}

	public void setMinhasReservas(List<Reserva> minhasReservas) {
		this.minhasReservas = minhasReservas;
	}
	
}
