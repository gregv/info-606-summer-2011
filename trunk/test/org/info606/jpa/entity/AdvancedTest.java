package org.info606.jpa.entity;

import generated.Student;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.info606.test.util.SQLUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Greg Vannoni
 * @class CS 575
 *        Purpose:
 *        Notes:
 */
public class AdvancedTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = AdvancedTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ Test
    public void testDelete() {
        // Find something in the database

        String xpath = "/Student[GPA >0.9 and GPA <1.2]";
        List<StudentEntity> list = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", xpath);

        Assert.assertTrue("List isn't null", list != null);
        Assert.assertTrue("List isn't empty", !list.isEmpty());

        logger.log(Level.INFO, "Found {0} Student records to remove.", list.size());

        EntityManager em = SQLUtil.getEntityManager();
        int numDeleted = 0;
        for (StudentEntity student : list) {
            // Make the entity managed before removing it
            em.getTransaction().begin();
            Student jaxBstudent = (Student)unmarshall(student.getXml(), Student.class);

            int studentId = jaxBstudent.getStudentId().intValue();

            // JPA is bad with single apostrophes and named queries so this is a hack to make it work
            String nativeQuery = "DELETE FROM STUDENT WHERE existsNode(OBJECT_VALUE, '/Student[ studentId = " + studentId + " ]') = 1";
            Query q = em.createNativeQuery(nativeQuery);
            numDeleted += q.executeUpdate();

            em.getTransaction().commit();
        }

        logger.log(Level.INFO, "Deleted {0} Student records.", numDeleted);

    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getXMLFromJAXB()
     */
    @ Override
    public String getXMLFromJAXB() {
        throw new UnsupportedOperationException("This is not supported in this class!");
    }
}
