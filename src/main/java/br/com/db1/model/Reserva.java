package br.com.db1.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.db1.enumeration.StatusReserva;

@Entity
@Table(name = "reserva", schema = "public")
public class Reserva {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "datareserva", nullable = false)
	private LocalDateTime dataReserva;
	@Column(name = "prazoretirada", nullable = false)
	private Integer prazoRetirada;
	@Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
	
	@Column(name = "statusreserva", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private StatusReserva statusReserva;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "livro_id", nullable = false)
	private Livro livro;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(LocalDateTime dataReserva) {
		this.dataReserva = dataReserva;
	}

	public Integer getPrazoRetirada() {
		return prazoRetirada;
	}

	public void setPrazoRetirada(Integer prazoRetirada) {
		this.prazoRetirada = prazoRetirada;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public StatusReserva getStatusReserva() {
		return statusReserva;
	}

	public void setStatusReserva(StatusReserva statusReserva) {
		this.statusReserva = statusReserva;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		Reserva other = (Reserva) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
