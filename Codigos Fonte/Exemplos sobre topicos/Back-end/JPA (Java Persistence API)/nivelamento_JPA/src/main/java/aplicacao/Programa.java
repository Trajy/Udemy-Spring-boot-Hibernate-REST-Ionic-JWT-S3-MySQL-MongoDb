package aplicacao;

import dominio.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Programa {

    public static void main(String[] args) {

        Pessoa p1 = new Pessoa(null, "Carlos da Silva", "carlos@gmail.com");
        Pessoa p2 = new Pessoa(null, "Joao da Silva", "joao@gmail.com");
        Pessoa p3 = new Pessoa(null, "Jose da Silva", "jose@gmail.com");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
        EntityManager em = emf.createEntityManager();


        Pessoa p = em.find(Pessoa.class, 2);
        em.getTransaction().begin();
        /*em.persist(p1);
        em.persist(p2);
        em.persist(p3);*/
//     Pessoa p = em.find(Pessoa.class, 2);

        Pessoa pessoaRemovida = new Pessoa(2, null, null);

        em.remove(p);
        em.getTransaction().commit();



//      System.out.println(p);

        em.close();
        emf.close();

        System.out.println("pronto !");

    }

}
