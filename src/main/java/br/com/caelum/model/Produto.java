
package br.com.caelum.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

//@DynamicUpdate(value=true) // serve para na hora do update dar apenas UPDATE no campo que foi alterado
@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Version // Com essa anotação e adicionado o atributo no form, nao permite 2 usuarios ao mesmo tempo alterar, lança uma OptimisticLockException
	private int versao;
	
	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}

	@NotEmpty
	private String nome;
	@NotEmpty
	private String linkDaFoto;
	
	@ManyToMany
//	@JoinTable(name="CATEGORIA_PRODUTO")
	private List<Categoria> categorias = new ArrayList<>();
	
	@NotEmpty
	@Column(columnDefinition="TEXT")
	private String descricao;
	
	@Min(20)
	private double preco;
	
	
	@Valid
	@ManyToOne
	private Loja loja;
	
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	//método auxiliar para associar categorias com o produto
	//se funcionar apos ter definido o relacionamento entre produto e categoria
	public void adicionarCategorias(Categoria... categorias) {
		for (Categoria categoria : categorias) {
			this.categorias.add(categoria);
		}
	}

	public String getLinkDaFoto() {
		return linkDaFoto;
	}
	
	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public void setLinkDaFoto(String linkDaFoto) {
		this.linkDaFoto = linkDaFoto;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	public Loja getLoja() {
		return loja;
	}
	
	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
}
