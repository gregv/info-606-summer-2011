package org.info606.test.util;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.info606.jpa.entity.AbstractEntityTestInterface;
import org.info606.jpa.entity.AbstractXmlTypeEntity;
import org.info606.mock.MockEntityManager;

/**
 * @class INFO 606
 *        Purpose: Provide utilities for accessing data
 *        Notes:
 */
public class SQLUtil {

    private static final String         PERSISTENCE_UNIT_NAME = "myem";
    private static EntityManagerFactory factory               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private static Logger               logger                = Logger.getLogger(SQLUtil.class.getName());
    private static String               CLASS_NAME            = SQLUtil.class.getName();

    /**
     * Method: truncateTable<br/>
     * Truncate a given table
     * @param tablename
     * @return
     */
    public static int truncateTable(String tablename) {
        logger.entering(CLASS_NAME, "truncateTable");
        EntityManager em = factory.createEntityManager();

        String sql = "TRUNCATE TABLE " + tablename;

        em.getTransaction().begin();
        int numrows = em.createNativeQuery(sql).executeUpdate();
        em.getTransaction().commit();
        em.close();

        logger.exiting(CLASS_NAME, "truncateTable", numrows);
        return numrows;
    }

    /**
     * Method: searchXml<br/>
     * Sample method to perform XPath query
     */
    public static List<?> searchXmlExistsNode(Class<?> entityClass, String attributeName, String XPath) {
        logger.entering(CLASS_NAME, "searchXmlExistsNode");
        EntityManager em = getEntityManager();

        logger.log(Level.FINE, "attributeName = {0}, xPath = {1}", new Object[] {attributeName, XPath});
        ExpressionBuilder builder = new ExpressionBuilder(entityClass);
        Expression criteria = builder.get(attributeName).existsNode(XPath).equal(true);

        ReadAllQuery query = new ReadAllQuery(entityClass);
        query.setSelectionCriteria(criteria);

        Query q = ((JpaEntityManager)em).createQuery(query);

        List<?> list = null;
        if (q != null) {
            list = q.getResultList();
        }

        em.close();

        logger.exiting(CLASS_NAME, "searchXmlExistsNode");
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

    /**
     * Method: getAllRowsFromTable<br/>
     * @param tablename
     * @return
     */
    public static Object getAllRowsFromTable(String tablename) {
        EntityManager em = factory.createEntityManager();

        // Read the existing entries and write to console
        Query q = em.createQuery("select t from :tablename t").setParameter("tablename", tablename);
        return q.getResultList();
    }

    /**
     * Method: insertRandom<br/>
     * @param numToMake
     * @param entity
     * @param testInterface
     */
    public static void insertRandom(int numToMake, AbstractXmlTypeEntity entity, AbstractEntityTestInterface testInterface) {
        logger.entering("insertRandom", null);
        EntityManager em = getEntityManager();

        int insertCounter = 0;
        while (insertCounter != numToMake) {
            em.getTransaction().begin();

            String xml = testInterface.getXMLFromJAXB();
            entity.setXml(xml);

            em.persist(entity);
            em.getTransaction().commit();

            em.close();

            insertCounter++;

        }

        logger.exiting("insertRandom", null);
    }

    /**
     * Method: insert<br/>
     * Inserts into database
     * @param entity
     * @param xml
     */
    public static void insert(AbstractXmlTypeEntity entity, String xml) {
        logger.entering("insert", null);
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        entity.setXml(xml);

        em.persist(entity);
        em.getTransaction().commit();
        em.close();

        logger.exiting("insert", null);
    }

    /**
     * Method: getEntityManager<br/>
     * Returns a real entity manager if there is a connection to the database, otherwise it returns a mock entity
     * manager so you can still run your tests
     * @return
     */
    public static EntityManager getEntityManager() {
        logger.entering("EntityManager", "");
        InetSocketAddress address = new InetSocketAddress("oracledb", 1521);
        boolean dbAvailable = !address.isUnresolved();
        logger.fine("dbAvailable = " + dbAvailable);

        EntityManager em = null;

        if (dbAvailable) {
            logger.fine("Returning real EntityManager");
            em = factory.createEntityManager();
        } else {
            logger.warning("\n\n\n Real Database Not Available! Returning Mock Entity Manager!!!\n\n\n");
            logger.info("Returning MockEntityManager");
            em = new MockEntityManager();
        }

        logger.exiting("EntityManager", "");
        return em;
    }
}
