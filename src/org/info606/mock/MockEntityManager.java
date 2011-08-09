package org.info606.mock;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.internal.jpa.transaction.EntityTransactionImpl;
import org.eclipse.persistence.internal.jpa.transaction.EntityTransactionWrapper;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.AttributeGroup;
import org.eclipse.persistence.queries.Call;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.sessions.broker.SessionBroker;
import org.eclipse.persistence.sessions.server.ServerSession;

public class MockEntityManager implements JpaEntityManager {
    private static Logger logger = Logger.getLogger(MockEntityManager.class.getName());

    @ Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @ Override
    public void close() {
        logger.info("close()");

    }

    @ Override
    public boolean contains(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public Query createNamedQuery(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createNativeQuery(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createNativeQuery(String arg0, Class arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createNativeQuery(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQuery(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public void detach(Object arg0) {
        // TODO Auto-generated method stub

    }

    @ Override
    public <T> T find(Class<T> arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @ Override
    public CriteriaBuilder getCriteriaBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Object getDelegate() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public EntityManagerFactory getEntityManagerFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public FlushModeType getFlushMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public LockModeType getLockMode(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Metamodel getMetamodel() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public <T> T getReference(Class<T> arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public EntityTransaction getTransaction() {
        logger.info("getTransaction()");
        DatabaseSessionImpl dsi = new DatabaseSessionImpl();

        EntityManagerImpl emi = new EntityManagerImpl(dsi);

        EntityTransactionWrapper wrapper = new EntityTransactionWrapper(emi);

        EntityTransaction et = new EntityTransactionImpl(wrapper) {
            public void begin() {

            }

            public void commit() {

            }
        };

        logger.info("exiting getTransaction()");
        return et;
    }

    @ Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public void joinTransaction() {
        // TODO Auto-generated method stub

    }

    @ Override
    public void lock(Object arg0, LockModeType arg1) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        // TODO Auto-generated method stub

    }

    @ Override
    public <T> T merge(T arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public void persist(Object arg0) {
        System.out.println("Persisting: " + arg0);
        // TODO Auto-generated method stub

    }

    @ Override
    public void refresh(Object arg0) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void refresh(Object arg0, Map<String, Object> arg1) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void refresh(Object arg0, LockModeType arg1) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void remove(Object arg0) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void setFlushMode(FlushModeType arg0) {
        // TODO Auto-generated method stub

    }

    @ Override
    public void setProperty(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    @ Override
    public <T> T unwrap(Class<T> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Object copy(Object arg0, AttributeGroup arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createDescriptorNamedQuery(String arg0, Class arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createDescriptorNamedQuery(String arg0, Class arg1, List arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQuery(DatabaseQuery arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQuery(Call arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQuery(Expression arg0, Class arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQuery(Call arg0, Class arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Query createQueryByExample(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Session getActiveSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public DatabaseSessionImpl getDatabaseSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public DatabaseSessionImpl getMemberDatabaseSession(Class arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public ServerSession getMemberServerSession(Class arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public String getMemberSessionName(Class arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public ServerSession getServerSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public SessionBroker getSessionBroker() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public UnitOfWork getUnitOfWork() {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public boolean isBroker() {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public void load(Object arg0, AttributeGroup arg1) {
        // TODO Auto-generated method stub

    }

}
