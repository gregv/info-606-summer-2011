package org.info606.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.info606.jpa.entity.Table1;

public class Main {

    private static final String         PERSISTENCE_UNIT_NAME = "myem";
    private static EntityManagerFactory factory               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private static EntityManager        em;

    public static void main(String args[]) {
        //  int counter = 0;
        //while (counter != 10000) {
        //   insertXml();
        //    counter++;
        //}

    }

    /**
     * Method: doIt<br/>
     * Sample method to test DB INSERT with JPA/EclipseLink
     */
    public static void doIt() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Read the existing entries and write to console
        Query q = em.createQuery("select t from Table1 t");
        List<Table1> todoList = q.getResultList();
        for (Table1 todo : todoList) {
            System.out.println(todo);
        }
        System.out.println("Size: " + todoList.size());

        // Create new todo
        em.getTransaction().begin();
        Table1 todo = new Table1();
        todo.setColumn1("This is a test");
        em.persist(todo);
        em.getTransaction().commit();
        em.close();

    }

}
