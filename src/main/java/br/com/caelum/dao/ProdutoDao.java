package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.caelum.model.Loja;
import br.com.caelum.model.Produto;

@Repository
public class ProdutoDao {

	@PersistenceContext
	private EntityManager em;

	public List<Produto> getProdutos() {
		return em.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto getProduto(Integer id) {
		Produto produto = em.find(Produto.class, id);
		return produto;
	}

	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();//CriteriaBuilder = e como se fosse a inicialização de uma fabrica
																 // um codigo para se utilizar para a produção de um determinado produto
		
		CriteriaQuery<Produto> query = criteriaBuilder.createQuery(Produto.class);//É a inicialização da queri como se fosse a string
																				 // do JPA por exmplo (Select * from Produto)
		Root<Produto> from = query.from(Produto.class);// O Root é quem define qual entidade estamos buscando, então, ela seria seria análoga 
													//a cláusula from. Portanto, usamos a classe CriteriaQuery, que é a responsável em montar a query.

		Expression<String> nomePath = from.<String>get("nome");// A raiz é usada para definir os caminhos (Path) até os atributos do objeto
		Path<Integer> lojaPath = from.<Loja>get("loja").<Integer>get("id");
		Path<Integer> categoriaPath = from.join("categorias").<Integer>get("id");

		List<Predicate> predicates = new ArrayList<>();
		
		if (!nome.isEmpty()) {
			Predicate nomeIgual = criteriaBuilder.like(nomePath, "%"  + nome + "%");
			predicates.add(nomeIgual);
		}
		if(categoriaId != null) {
			Predicate categoriaIgual = criteriaBuilder.equal(categoriaPath, categoriaId);
			predicates.add(categoriaIgual);
		}
		if (lojaId != null) {
			Predicate lojaIgual = criteriaBuilder.equal(lojaPath, lojaId);
			predicates.add(lojaIgual);
		}
		
		query.where((Predicate[]) predicates.toArray(new Predicate[0])); //Adiciona os Predicates no WHere
		TypedQuery<Produto> typedQuery = em.createQuery(query);
		
		return typedQuery.getResultList();
	}
	
//Usando Criteria do HIBERNATE
//	
//	@Transactional(ReadOnly=true)
//	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {
//	    Session session = em.unwrap(Session.class);
//	    Criteria criteria = session.createCriteria(Produto.class);
//
//	    if (!nome.isEmpty()) {
//	        criteria.add(Restrictions.like("nome", "%" + nome + "%"));
//	    }
//
//	    if (lojaId != null) {
//	        criteria.add(Restrictions.like("loja.id", lojaId));
//	    }
//
//	    if (categoriaId != null) {
//	        criteria.setFetchMode("categorias", FetchMode.JOIN)
//	            .createAlias("categorias", "c")
//	            .add(Restrictions.like("c.id", categoriaId));
//	    }
//
//	    return (List<Produto>) criteria.list();
//	}
	

	public void insere(Produto produto) {
		if (produto.getId() == null)
			em.persist(produto);
		else
			em.merge(produto);
	}

}
