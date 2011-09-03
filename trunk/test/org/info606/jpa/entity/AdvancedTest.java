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
import org.junit.Test;

/**
 * @class INFO 606
 *        Purpose: Provide some advanced tests
 *        Notes:
 */
public class AdvancedTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = AdvancedTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    /**
     * Method: testDelete<br/>
     * Deletes data from the Student table
     */
    @ Test
    public void testDelete() {
        // Find something in the database

        String xpath = "/Student[GPA >0.2 and GPA <2.9]";
        List<StudentEntity> list = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", xpath);

        Assert.assertTrue("List isn't null", list != null);
        Assert.assertTrue("List is empty, could not find Student", !list.isEmpty());

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

    /**
     * Method: advancedStudentQuery<br/>
     * Finds all students in program "Civil Engineering" that were admitted Spring 2008
     */
    @ Test
    public void advancedStudentQuery() {
        String xPathProgram = "existsNode(OBJECT_VALUE, '/Student[program = \"Civil Engineering\"]') = 1";
        String xPathAdmitTerm = "contains(object_value, 'Spring 2008 INPATH (/Student/admit_term)') > 0";
        // Slower query: String xPathAdmitTerm = "existsNode(object_value, '/Student/admit_term[ora:contains(text(), \"Spring 2008\") > 0]','xmlns:ora=\"http://xmlns.oracle.com/xdb\"' ) = 1";

        String nativeQuery = "SELECT OBJECT_VALUE FROM STUDENT WHERE " + xPathProgram + " AND " + xPathAdmitTerm;

        EntityManager em = SQLUtil.getEntityManager();

        Query q = em.createNativeQuery(nativeQuery, StudentEntity.class);
        List<StudentEntity> list = (List<StudentEntity>)q.getResultList();

        List<String> selects = eclipseLinkParser.getAllSelectPerformanceForEntityAsList("Student");

        logger.log(Level.INFO, "advancedStudentQuery took {0} seconds on average.", new Object[] {eclipseLinkParser.averageListOfStringsAsIntegers(selects) / (1E9)});
    }

    /**
     * Method: advancedStudentAdvisorQuery<br/>
     * Finds all of the students for each advisor
     */
    @ Test
    public void advancedStudentAdvisorQuery() {
        // Get all Students for each advisor
        EntityManager em = SQLUtil.getEntityManager();

        String getAllAdvisors = "SELECT OBJECT_VALUE FROM ADVISOR";
        List<AdvisorEntity> advisors = (List<AdvisorEntity>)em.createNativeQuery(getAllAdvisors, AdvisorEntity.class).getResultList();

        StringBuffer sb = new StringBuffer();
        for (AdvisorEntity entity : advisors) {
            Advisor advisor = (Advisor)unmarshall(entity.getXml(), Advisor.class);
            int employeeId = advisor.getEmployeeId();

            String xpath = "/Student[advisor=" + advisor.getEmployeeId() + "]";
            List<StudentEntity> students = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", xpath);

            sb.append("Advisor with empId " + employeeId + " advises " + students.size() + " students.\n");
        }
        logger.info(sb.toString());

        List<String> selects = eclipseLinkParser.getAllSelectPerformanceForEntityAsList("Student");

        logger.log(Level.INFO, "advancedStudentAdvisorQuery took {0} seconds on average.", new Object[] {eclipseLinkParser.averageListOfStringsAsIntegers(selects) / (1E9)});

    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getRandomObject()
     */
    public Object getRandomObject() {
        throw new UnsupportedOperationException("This is not supported in this class!");
    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getXMLFromJAXB()
     */
    public String getXMLFromJAXB() {
        throw new UnsupportedOperationException("This is not supported in this class!");
    }
}
