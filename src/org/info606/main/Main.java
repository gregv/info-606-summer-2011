package org.info606.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.info606.jpa.entity.MyXML;
import org.info606.jpa.entity.Table1;

/**
 * @author Greg Vannoni
 * @class INFO 606
 *        Purpose:
 *        Notes:
 */
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

        searchXml();
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

    /**
     * Method: searchXml<br/>
     * Sample method to perform XPath query
     */
    public static void searchXml() {
        em = factory.createEntityManager();

        ExpressionBuilder builder = new ExpressionBuilder(MyXML.class);
        Expression criteria = builder.get("xml").extract("/books/book/description/text()").getStringVal().like("%This is a discription of book 2%");

        ReadAllQuery query = new ReadAllQuery(MyXML.class);
        query.setSelectionCriteria(criteria);

        List<MyXML> list = (List<MyXML>)((JpaEntityManager)em).createQuery(query).getResultList();
        System.out.println("Results");
        for (MyXML x : list) {
            System.out.println(x.getId());
        }
    }

    /**
     * Method: insertXml<br/>
     * Sample method for inserting XML into database from file
     */
    public static void insertXml() {
        em = factory.createEntityManager();

        // Read the existing entries and write to console
        Query q = em.createQuery("select t from MyXML t");
        List<MyXML> todoList = q.getResultList();
        for (MyXML todo : todoList) {
            System.out.println(todo);
        }
        System.out.println("Size: " + todoList.size());

        // Create new todo
        em.getTransaction().begin();
        MyXML xmldb = new MyXML();
        String strXml = getXMLFromFile();
        // xmldb.setId(-1);
        xmldb.setXml(strXml);

        em.persist(xmldb);
        em.getTransaction().commit();
        em.close();

    }

    public static String getXMLFromFile() {

        StringBuffer result = new StringBuffer(50);
        File f = new File("Book.xml");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = null;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
