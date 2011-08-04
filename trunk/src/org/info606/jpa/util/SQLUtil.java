package org.info606.jpa.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.ReadAllQuery;

public class SQLUtil {

    private static final String         PERSISTENCE_UNIT_NAME = "myem";
    private static EntityManagerFactory factory               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static int truncateTable(String tablename) {
        EntityManager em = factory.createEntityManager();

        String sql = "TRUNCATE TABLE " + tablename;

        em.getTransaction().begin();
        int numrows = em.createNativeQuery(sql).executeUpdate();
        em.getTransaction().commit();
        em.close();
        return numrows;
    }

    /**
     * Method: searchXml<br/>
     * Sample method to perform XPath query
     */
    public static List<?> searchXmlExistsNode(Class<?> entityClass, String attributeName, String XPath) {
        EntityManager em = factory.createEntityManager();

        ExpressionBuilder builder = new ExpressionBuilder(entityClass);
        Expression criteria = builder.get(attributeName).existsNode(XPath).equal(true);

        ReadAllQuery query = new ReadAllQuery(entityClass);
        query.setSelectionCriteria(criteria);

        List<?> list = (List<?>)((JpaEntityManager)em).createQuery(query).getResultList();

        em.close();
        return list;
    }

    /**
     * Method: searchXml<br/>
     * Sample method to perform XPath query
     */
    public static List<?> searchXmlExtract(Class entityClass, String attributeName, String XPath, String value) {
        EntityManager em = factory.createEntityManager();

        ExpressionBuilder builder = new ExpressionBuilder(entityClass);
        Expression criteria = builder.get(attributeName).extract(XPath).getStringVal().containsSubstring(value);

        ReadAllQuery query = new ReadAllQuery(entityClass);
        query.setSelectionCriteria(criteria);

        List<?> list = (List<?>)((JpaEntityManager)em).createQuery(query).getResultList();

        em.close();
        return list;
    }

    public static Object getAllRowsFromTable(String tablename) {
        EntityManager em = factory.createEntityManager();

        // Read the existing entries and write to console
        Query q = em.createQuery("select t from :tablename t").setParameter("tablename", tablename);
        return q.getResultList();
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

}
