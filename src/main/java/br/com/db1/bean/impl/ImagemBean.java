package br.com.db1.bean.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.com.db1.bean.BEAN;
import br.com.db1.dao.impl.ImagemDAO;
import br.com.db1.model.Imagem;
import br.com.db1.util.exception.ErroSistema;


@RequestScoped
@Named
public class ImagemBean extends BEAN<Imagem, ImagemDAO>{
	
	@Inject
	private ImagemDAO imagemDao;
	
	private EntityManager manager;

	private List<Imagem> list;

	private String nomeArquivoFiltrado;

	private Imagem arquivo;

	private Part imagemUpado;

	public Part getArquivoUpado() {
		return imagemUpado;
	}

	public void setArquivoUpado(Part arquivoUpado) {
		this.imagemUpado = arquivoUpado;
	}

	@PostConstruct
	public void init() {
		zerarLista();
	}

	public void exibirImagem() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) ec.getResponse();

		for (Imagem arquivo : list) {
			if (arquivo.getImagem() != null) {
				response.setContentType(ec.getMimeType(arquivo.getNomeImagem()));
				response.setContentLength(arquivo.getImagem().length);
				try {
					response.getOutputStream().write(arquivo.getImagem());
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}

			}
		}
	}

	public void download(Imagem arquivoParametro) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		externalContext.setResponseHeader("Content-Type", arquivoParametro.getExtensaoImagem());
		externalContext.setResponseHeader("Content-Length", "" + arquivoParametro.getImagem().length);
		externalContext.setResponseHeader("Content-Disposition",
				"attachment;filename=\"" + arquivoParametro.getNomeImagem() + "\"");
		externalContext.getResponseOutputStream().write(arquivoParametro.getImagem());
		facesContext.responseComplete();
	}

	public String getNomeArquivo() {
		String header = imagemUpado.getHeader("content-disposition");
		if (header == null)
			return "";
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "";
	}

	public String importa() {
		try {
			this.arquivo.setNomeImagem(getNomeArquivo());
			this.arquivo.setExtensaoImagem(imagemUpado.getContentType());

			byte[] arquivoByte = IOUtils.toByteArray(imagemUpado.getInputStream());
			this.arquivo.setImagem(arquivoByte);
			salvar();

		} catch (IOException e) {
			adicionarMensagem("Erro ao enviar o arquivo " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		return "arquivo";
	}
	
	private void zerarLista() {
		list = new ArrayList<Imagem>();
	}

	public Imagem getArquivo() {
		return arquivo;
	}

	public void setArquivo(Imagem arquivo) {
		this.arquivo = arquivo;
	}

	public String getNomeUfFiltrada() {
		return nomeArquivoFiltrado;
	}

	public void setNomeUfFiltrada(String nomeUfFiltrada) {
		this.nomeArquivoFiltrado = nomeUfFiltrada;
	}

	public String getNomeArquivoFiltrado() {
		return nomeArquivoFiltrado;
	}

	public void setNomeArquivoFiltrado(String nomeArquivoFiltrado) {
		this.nomeArquivoFiltrado = nomeArquivoFiltrado;
	}

	public List<Imagem> getList() {
		return list;
	}

	public String novo() {
		this.arquivo = new Imagem();
		return "cadastrarArquivo";
	}

	public String salvar() {
		try {
			if (!imagemDao.save(this.arquivo)) {
				adicionarMensagem("Erro ao enviar o arquivo.", FacesMessage.SEVERITY_ERROR);
			} else {
				adicionarMensagem("Arquivo salvo com sucesso.", FacesMessage.SEVERITY_INFO);
				nomeArquivoFiltrado = this.arquivo.getNomeImagem();
				listarArquivo();
			}
		} catch (ErroSistema e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nomeArquivoFiltrado;

	}

	public String editar(Imagem arquivo) {
		try {
			this.arquivo = imagemDao.findById(arquivo.getId());
		} catch (ErroSistema e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "cadastrarArquivo";
	}

	public String remover(Imagem arquivo) {
		try {
			if (!imagemDao.restate(arquivo)) {
				adicionarMensagem("Erro ao remover o arquivo.", FacesMessage.SEVERITY_ERROR);
			} else {
				adicionarMensagem("Arquivo removido com sucesso.", FacesMessage.SEVERITY_INFO);
				listarArquivo();
			}
		} catch (ErroSistema e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "arquivo";
	}

	public void listarArquivo() {
		zerarLista();
		if (!nomeArquivoFiltrado.isEmpty()) {
			try {
				list.addAll(imagemDao.findByName(nomeArquivoFiltrado));
			} catch (ErroSistema e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				list.addAll(imagemDao.findAll());
			} catch (ErroSistema e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Imagem arquivo:list) {
			escreverArquivoDiretorio(arquivo);
		}
	}

	private void escreverArquivoDiretorio(Imagem arquivo) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String absoluteWebPath = ec.getRealPath("/");
		String destPath = absoluteWebPath + "/resources/imagem/" + arquivo.getNomeImagem();
		File destFile = new File(destPath);
		
		InputStream is = new ByteArrayInputStream(arquivo.getImagem());
		try {
			FileUtils.copyInputStreamToFile(is, destFile);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Override
	public ImagemDAO getDAO() {
		if(imagemDao == null) {
			imagemDao = new ImagemDAO(manager);
		}
		return imagemDao;
	}

	@Override
	public Imagem criaNovaEntidade() {
		return new Imagem();
	}

	@Override
	public String caminhoNovaEntidade() {
		return null;
	}

	@Override
	public String caminhoSalvarEntidade() {
		return null;
	}

	@Override
	public String caminhoEditarEntidade(Imagem imagem) {
		return null;
	}

	@Override
	public String caminhoDesativarEntidade() {
		return null;
	}

	@Override
	public void buscar(String value, String type) {
		try {
			if(value.isEmpty()){
				setEntidades(getDAO().findAll());
			} else if ("name".equals(type)) {
				setEntidades(getDAO().findByName(value));
			}
		} catch (ErroSistema ex) {
			Logger.getLogger(BEAN.class.getName()).log(Level.SEVERE, null, ex);
			adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		
	}
	
	public Part getImagemUpado() {
		return imagemUpado;
	}

	public void setImagemUpado(Part imagemUpado) {
		this.imagemUpado = imagemUpado;
	}

}
