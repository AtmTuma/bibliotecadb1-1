package br.com.db1.bean.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import br.com.db1.dao.impl.EmprestimoDAO;
import br.com.db1.dao.impl.LivroDAO;
import br.com.db1.dao.impl.ReservaDAO;
import br.com.db1.enumeration.StatusLivro;
import br.com.db1.model.Emprestimo;
import br.com.db1.model.Reserva;
import br.com.db1.model.Usuario;
import br.com.db1.util.exception.ErroSistema;

@RequestScoped
@Named
public class EmprestimoBean extends BEAN<Emprestimo, EmprestimoDAO> {

	private EntityManager manager;

	private EmprestimoDAO emprestimoDao;

	private ReservaDAO reservaDao;

	private LivroDAO livroDao;

	private List<Emprestimo> emprestimoAtual;
	
	private List<Emprestimo> historicoEmprestimos;

	private String campoPesquisa = "";

	@Inject
	public EmprestimoBean(EmprestimoDAO emprestimoDao, ReservaDAO reservaDao, LivroDAO livroDao) {
		this.emprestimoDao = emprestimoDao;
		this.reservaDao = reservaDao;
		this.livroDao = livroDao;
	}

	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		if (!usuarioLogado.getAdministrador()) {
			try {
				emprestimoAtual = emprestimoDao.findByNameUsuario(usuarioLogado.getNome());
			} catch (ErroSistema e) {
				e.printStackTrace();
			}
		}
		buscar("", "");
		novo();
	}

	public void filtrar() throws ErroSistema {
		List<Emprestimo> emprestimosFiltrados = new ArrayList<Emprestimo>();
		emprestimosFiltrados.addAll(getDAO().findByName(campoPesquisa));
		setEntidades(emprestimosFiltrados);
	}

	@Override
	public void buscar(String value, String type) {
		try {
			if (value.isEmpty()) {
				setEntidades(getDAO().findAll());
			} else if ("livro".equals(type)) {
				setEntidades(getDAO().findByName(value));
			} else if ("categoria".equals(type)) {
				setEntidades(getDAO().findByIdUsuario(Long.parseLong(value)));
			}
			if (getEntidades() == null || getEntidades().isEmpty()) {
				
			}
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public String autorizaEmprestimo(Reserva reserva) throws ErroSistema {
		this.novo();
		getEntidade().setDataEmprestimo(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
		getEntidade().setDataDevolucao(LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(15L));
		getEntidade().setUsuario(reserva.getUsuario());
		getEntidade().setLivro(reserva.getLivro());
		reservaDao.restate(reserva);

		adicionarMensagem("Emprestado com sucesso! Voce tem até dia " + this.getEntidade().getDataDevolucao()
				+ " para devolve-lo.", FacesMessage.SEVERITY_INFO);

		return this.salvar();
	}

	public String confirmaDevolucaoLivro(Emprestimo emprestimo) throws ErroSistema {
		setEntidade(emprestimo);
		List<Reserva> reservasDoLivro = reservaDao.findByName(emprestimo.getLivro().getTitulo());
		if (reservasDoLivro.isEmpty()) {
			emprestimo.getLivro().setStatus(StatusLivro.DISPONIVEL);
			livroDao.save(emprestimo.getLivro());
		}
		adicionarMensagem("Devolução confirmada", FacesMessage.SEVERITY_INFO);
		return this.desativar();
	}

	public String renovarEmprestimo(Emprestimo emprestimo) {
		setEntidade(emprestimo);
		getEntidade().setDataEmprestimo(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
		getEntidade().setDataDevolucao(LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(15L));
		getEntidade().setUsuario(emprestimo.getUsuario());
		getEntidade().setLivro(emprestimo.getLivro());
		getEntidade().setRenovado(true);

		adicionarMensagem(
				"Renovado com sucesso! Voce tem até dia " + this.getEntidade().getDataDevolucao() + " para devolve-lo.",
				FacesMessage.SEVERITY_INFO);

		return this.salvar();
	}

	@Override
	public EmprestimoDAO getDAO() {
		if (emprestimoDao == null) {
			emprestimoDao = new EmprestimoDAO(manager);
		}
		return emprestimoDao;
	}

	@Override
	public Emprestimo criaNovaEntidade() {
		return new Emprestimo();
	}

	@Override
	public String caminhoNovaEntidade() {
		return "";
	}

	@Override
	public String caminhoSalvarEntidade() {
		return "reservas";
	}

	@Override
	public String caminhoEditarEntidade(Emprestimo emprestimo) {
		setEntidade(emprestimo);
		return "";
	}

	@Override
	public String caminhoDesativarEntidade() {
		return "emprestimos";
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public EmprestimoDAO getEmprestimoDao() {
		return emprestimoDao;
	}

	public void setEmprestimoDao(EmprestimoDAO emprestimoDao) {
		this.emprestimoDao = emprestimoDao;
	}

	public ReservaDAO getReservaDao() {
		return reservaDao;
	}

	public void setReservaDao(ReservaDAO reservaDao) {
		this.reservaDao = reservaDao;
	}

	public List<Emprestimo> getEmprestimoAtual() {
		return emprestimoAtual;
	}

	public void setEmprestimoAtual(List<Emprestimo> emprestimoAtual) {
		this.emprestimoAtual = emprestimoAtual;
	}

	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public LivroDAO getLivroDao() {
		return livroDao;
	}

	public void setLivroDao(LivroDAO livroDao) {
		this.livroDao = livroDao;
	}

	public List<Emprestimo> getHistoricoEmprestimos() {
		return historicoEmprestimos;
	}

	public void setHistoricoEmprestimos(List<Emprestimo> historicoEmprestimos) {
		this.historicoEmprestimos = historicoEmprestimos;
	}

}
