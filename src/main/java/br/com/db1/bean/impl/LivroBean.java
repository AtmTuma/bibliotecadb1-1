package br.com.db1.bean.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.Part;

import br.com.db1.bean.BEAN;
import br.com.db1.dao.impl.LivroDAO;
import br.com.db1.model.Livro;
import br.com.db1.util.exception.ErroSistema;

@RequestScoped
@Named
public class LivroBean extends BEAN<Livro, LivroDAO> {

	private EntityManager manager;

	private LivroDAO livrodao;

	private Part imagemUpado;
	
	private String campoPesquisa = "";

	@Inject
	public LivroBean(LivroDAO livroDao) {
		this.livrodao = livroDao;
	}

	@PostConstruct
	public void init() {
		buscar("","");
		novo();
	}
	
	public void filtrar() throws ErroSistema {
		buscar(campoPesquisa, "nome");
	}

	@Override
	public LivroDAO getDAO() {
		if (livrodao == null) {
			livrodao = new LivroDAO(manager);
		}
		return livrodao;
	}

	@Override
	public Livro criaNovaEntidade() {
		return new Livro();
	}

	@Override
	public String caminhoNovaEntidade() {
		return "cadastroLivro.xhtml";
	}

	@Override
	public String caminhoSalvarEntidade() {
		return "acervo";
	}

	@Override
	public String caminhoEditarEntidade(Livro livro) {
		this.setEntidade(livro);
		return "cadastroLivro";
	}

	@Override
	public String caminhoDesativarEntidade() {
		return "acervo";
	}

	@Override
	public void buscar(String value, String type) {
		try {
			if (value.isEmpty()) {
				setEntidades(getDAO().findAll());
			} else if ("nome".equals(type)) {
				setEntidades(getDAO().findByName(value));
			} else if ("autor".equals(type)) {
				setEntidades(getDAO().findByAuthor(value));
			} else if ("categoria".equals(type)) {
				setEntidades(getDAO().findByCategory(value));
			}
			if (getEntidades() == null || getEntidades().isEmpty()) {
				adicionarMensagem("Não tem nada cadastrado!", FacesMessage.SEVERITY_WARN);
			}
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public String desativarLivro(Livro livro) {
		this.setEntidade(livro);
		return this.desativar();
	}

	public Part getImagemUpado() {
		return imagemUpado;
	}

	public void setImagemUpado(Part imagemUpado) {
		this.imagemUpado = imagemUpado;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
}
