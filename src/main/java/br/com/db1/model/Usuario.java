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

import br.com.db1.enumeration.TipoColaborador;

@Entity
@Table(name = "usuario", schema = "public")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = true, length = 50)
	private String nome;
	@Column(nullable = false, length = 50)
	private String email;
	@Column(nullable = false, length = 20)
	private String senha;
	@Column(nullable = false)
	private Boolean administrador = Boolean.FALSE;
	@Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
	
	@Column(nullable = true, length = 20)
	@Enumerated(EnumType.STRING)
	private TipoColaborador tipo;
	
	@OneToMany(fetch=FetchType.LAZY , mappedBy="usuario")
	private List<Reserva> reservas;
	
	@OneToMany(fetch=FetchType.LAZY , mappedBy="usuario")
	private List<Emprestimo> emprestimos;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public Boolean getAdministrador() {
		return administrador;
	}
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}
	public TipoColaborador getTipo() {
		return tipo;
	}
	public void setTipo(TipoColaborador tipo) {
		this.tipo = tipo;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
