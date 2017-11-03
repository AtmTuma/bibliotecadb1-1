package br.com.db1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="imagem", schema = "public")
public class Imagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "imagem", nullable = false)
	private byte[] arquivo;
	@Column(name = "nome", nullable = false, length = 50)
	private String nomeImagem;
	@Column(name = "extensao", nullable = false, length = 10)
	private String extensaoImagem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "livro_id", nullable = false)
	private Livro livro;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public byte[] getImagem() {
		return arquivo;
	}
	public void setImagem(byte[] imagem) {
		this.arquivo = imagem;
	}
	public String getNomeImagem() {
		return nomeImagem;
	}
	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}
	public String getExtensaoImagem() {
		return extensaoImagem;
	}
	public void setExtensaoImagem(String extensaoImagem) {
		this.extensaoImagem = extensaoImagem;
	}
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
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
		Imagem other = (Imagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
