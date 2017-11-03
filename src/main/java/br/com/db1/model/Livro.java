package br.com.db1.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.db1.enumeration.CategoriaLivro;
import br.com.db1.enumeration.StatusLivro;

@Entity
@Table(name = "livro", schema = "public")
public class Livro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = true, length = 50)
	private String codigo;
	@Column(nullable = false, length = 80)
	private String titulo;
	@Column(nullable = true, length = 750)
	private String descricao;
	@Column(nullable = true, length = 50)
	private String editora;
	@Column(nullable = false, length = 50)
	private String autor;
	@Column(nullable = true)
	private Integer edicao;
	@Column(name = "anopublicacao", nullable = true)
	private Integer anoPublicacao;
	@Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
	
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private StatusLivro status = StatusLivro.DISPONIVEL;
	@Column(nullable = true, length = 20)
	@Enumerated(EnumType.STRING)
	private CategoriaLivro categoria;
	
	@OneToMany(fetch=FetchType.LAZY , mappedBy="livro")
	private List<Reserva> reservas;
	
	@OneToMany(fetch=FetchType.LAZY , mappedBy="livro")
	private List<Emprestimo> emprestimos;
	
//	@OneToMany(fetch=FetchType.LAZY , mappedBy="livro")
//	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
//	private List<Imagem> imagens;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Integer getEdicao() {
		return edicao;
	}
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getEditora() {
		return editora;
	}
	public void setEditora(String editora) {
		this.editora = editora;
	}
	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}
	public void setAnoPublicacao(Integer anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public StatusLivro getStatus() {
		return status;
	}
	public void setStatus(StatusLivro status) {
		this.status = status;
	}
	public CategoriaLivro getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaLivro categoria) {
		this.categoria = categoria;
	}
	public List<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}
	public void setEmprestimos(List<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}
	
//	/**
//	 * @return the imagens
//	 */
//	public List<Imagem> getImagens() {
//		return imagens;
//	}
//	/**
//	 * @param imagens the imagens to set
//	 */
//	public void setImagens(List<Imagem> imagens) {
//		this.imagens = imagens;
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
