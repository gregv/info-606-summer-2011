package org.info606.jpa.entity;

import generated.Advisor;
import generated.Student;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.info606.test.util.SQLUtil;
import org.junit.Assert;
import org.junit.Ignore;
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

    @ Ignore
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

    @ Test
    public void advancedStudentQuery() {
        String xPathProgram = "existsNode(OBJECT_VALUE, '/Student[program = \"Civil Engineering\"]') = 1";
        String xPathAdmitTerm = "contains(object_value, 'Spring 2008 INPATH (/Student/admit_term)') > 0";
        // Slower query: String xPathAdmitTerm = "existsNode(object_value, '/Student/admit_term[ora:contains(text(), \"Spring 2008\") > 0]','xmlns:ora=\"http://xmlns.oracle.com/xdb\"' ) = 1";

        String nativeQuery = "SELECT OBJECT_VALUE FROM STUDENT WHERE " + xPathProgram + " AND " + xPathAdmitTerm;

        EntityManager em = SQLUtil.getEntityManager();

        Query q = em.createNativeQuery(nativeQuery, StudentEntity.class);
        List<StudentEntity> list = (List<StudentEntity>)q.getResultList();

        double time = Integer.parseInt(eclipseLinkParser.getAllSelectPerformanceForEntity("Student").trim()) / (1E9);

        logger.log(Level.INFO, "advancedStudentQuery returned {0} records and took {1} seconds.", new Object[] {list.size(), time});
    }

    @ Test
    public void advancedStudentAdvisorQuery() {
        // Get all Students for each advisor
        EntityManager em = SQLUtil.getEntityManager();

        String getAllAdvisors = "SELECT OBJECT_VALUE FROM ADVISOR";
        List<AdvisorEntity> advisors = (List<AdvisorEntity>)em.createNativeQuery(getAllAdvisors, AdvisorEntity.class).getResultList();
        for (AdvisorEntity entity : advisors) {
            Advisor advisor = (Advisor)unmarshall(entity.getXml(), Advisor.class);
            int employeeId = advisor.getEmployeeId();

            String xpath = "/Student[advisor=" + advisor.getEmployeeId() + "]";
            List<StudentEntity> students = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", xpath);

            logger.log(Level.INFO, "Advisor with empId {0} advises {1} students.", new Object[] {employeeId, students.size()});
        }

        List<String> selects = eclipseLinkParser.getAllSelectPerformanceForEntityAsList("Student");

        logger.log(Level.INFO, "advancedStudentAdvisorQuery took {0} seconds on average.", new Object[] {eclipseLinkParser.averageListOfStringsAsIntegers(selects) / (1E9)});

    }

    public Object getRandomObject() {
        throw new UnsupportedOperationException("This is not supported in this class!");
    }

    public String getXMLFromJAXB() {
        throw new UnsupportedOperationException("This is not supported in this class!");
    }
}
