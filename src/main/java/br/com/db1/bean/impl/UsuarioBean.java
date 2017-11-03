package br.com.db1.bean.impl;

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
import br.com.db1.dao.impl.UsuarioDAO;
import br.com.db1.model.Usuario;
import br.com.db1.util.exception.ErroSistema;

@RequestScoped
@Named
public class UsuarioBean extends BEAN<Usuario, UsuarioDAO> {

	private EntityManager manager;

	private UsuarioDAO usuarioDao;

	private String emailLogin = "";
	private String senhaLogin = "";
	private Boolean erroValidacao = false;

	@Inject
	public UsuarioBean(UsuarioDAO usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	@PostConstruct
	public void init() {
		buscar("", "");
		novo();
	}

	@Override
	public UsuarioDAO getDAO() {
		if (usuarioDao == null) {
			usuarioDao = new UsuarioDAO(manager);
		}
		return usuarioDao;
	}

	@Override
	public Usuario criaNovaEntidade() {
		return new Usuario();
	}

	@Override
	public String caminhoNovaEntidade() {
		return "cadastroUsuario";
	}

	@Override
	public String caminhoSalvarEntidade() {
		return "telaLogin";
	}

	@Override
	public String caminhoEditarEntidade(Usuario usuario) {
		return "";
	}

	@Override
	public String caminhoDesativarEntidade() {
		return "";
	}

	public String login() throws ErroSistema {

		FacesContext fc = FacesContext.getCurrentInstance();
		setEntidade(getDAO().findByEmail(emailLogin));

		if (getEntidade() != null && getEntidade().getSenha().equals(this.senhaLogin)) {
			ExternalContext ec = fc.getExternalContext();
			HttpSession session = (HttpSession) ec.getSession(false);
			session.setAttribute("usuario", this.getEntidade());

			if (getEntidade().getAdministrador()) {
				return "/LoginRH/telaInicialRH";
			} else {
				return "/LoginColaborador/telaInicialColaborador";
			}

		} else {
			FacesMessage fm = new FacesMessage("USUÁRIO E/OU SENHA INVÁLIDOS!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			setErroValidacao(Boolean.TRUE);
			return "/telaLogin";
		}
	}

	public String logout() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		session.removeAttribute("usuario");

		return "/telaLogin";
	}

	@Override
	public void buscar(String value, String type) {
		try {
			if (value.isEmpty()) {
				setEntidades(getDAO().findAll());
			} else if ("name".equals(type)) {
				setEntidades(getDAO().findByName(value));
			}
			if (getEntidades() == null || getEntidades().isEmpty()) {
				adicionarMensagem("Não tem nada cadastrado!", FacesMessage.SEVERITY_WARN);
			}
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public String getEmailLogin() {
		return emailLogin;
	}

	public void setEmailLogin(String emailLogin) {
		this.emailLogin = emailLogin;
	}

	public String getSenhaLogin() {
		return senhaLogin;
	}

	public void setSenhaLogin(String senhaLogin) {
		this.senhaLogin = senhaLogin;
	}

	public Boolean getErroValidacao() {
		return erroValidacao;
	}

	public void setErroValidacao(Boolean erroValidacao) {
		this.erroValidacao = erroValidacao;
	}
}
