package br.com.db1.bean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.db1.dao.DAO;
import br.com.db1.util.exception.ErroSistema;

public abstract class BEAN<E, D extends DAO<E>> {

	private E entidade;
	private List<E> entidades;

	public abstract D getDAO();

	public abstract E criaNovaEntidade();
	
	public abstract String caminhoNovaEntidade();
	
	public abstract String caminhoSalvarEntidade();
	
	public abstract String caminhoEditarEntidade(E entidade);
	
	public abstract String caminhoDesativarEntidade();

	public abstract void buscar(String value, String type);

	public String novo() {
		this.entidade = criaNovaEntidade();
		return caminhoNovaEntidade();
	}
	
	public String editar(E entidade) {
		return caminhoEditarEntidade(entidade);
	}

	public String salvar() {
		try {
			getDAO().save(entidade);
			//adicionarMensagem("Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		return caminhoSalvarEntidade();
	}

	public String desativar() {
		try {
			getDAO().restate(entidade);
			entidade = criaNovaEntidade();
			adicionarMensagem("Desativado com sucesso!", FacesMessage.SEVERITY_INFO);
		} catch (Exception ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		return caminhoDesativarEntidade();
	}

	public void adicionarMensagem(String mensagem, FacesMessage.Severity tipoErro) {
		FacesMessage ms = new FacesMessage(tipoErro, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, ms);
	}

	public E getEntidade() {
		return entidade;
	}

	public void setEntidade(E entidade) {
		this.entidade = entidade;
	}

	public List<E> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<E> entidades) {
		this.entidades = entidades;
	}
}
