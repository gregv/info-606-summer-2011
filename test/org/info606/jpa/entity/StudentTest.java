package org.info606.jpa.entity;

import generated.Advisor;
import generated.Schedule;
import generated.Student;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @class INFO 606
 *        Purpose: Test the Student table/entity
 *        Notes:
 */
public class StudentTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = StudentTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    /**
     * Method: truncateTables<br/>
     * This will truncate the table if the user's environment variable is set as such
     */
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("STUDENT");
        }
    }

    @ Test
    /**
     * Method: testBunchOfInserts<br/>
     * This is the only runnable unit test, it performs however many inserts the user wanted as given by the numInsert
     * environment variable
     */
    public void testBunchOfInserts() {
        logger.entering("testBunchOfInserts", null);
        int counter = 0;
        while (counter != getNumberToInsert()) {
            try {
                testOneInsert();
            } catch (DatabaseException dbe) {
                logger.log(Level.WARNING, "Constraint Violation!", dbe);
                logger.fine("values - " + dbe.getErrorCode() + ", " + dbe.getDatabaseErrorCode() + ", " + dbe.getMessage());
                dbe.printStackTrace();
            }

            counter++;
        }
        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_student.csv"), "Student");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_student.csv"), "Student");
        logger.exiting("testBunchOfInserts", null);
    }

    /**
     * Method: testOneInsert<br/>
     * This method will insert a single random record into the database
     */
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new StudentEntity(), this);
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        List<StudentEntity> list = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", "/Student[name=\"" + fname + " " + lname + "\"]");
        logger.entering("testOneInsert", null);
    }

    /**
     * Method: getRandomSchedule<br/>
     * Returns a random schedule
     * @return
     */
    private Schedule getRandomSchedule() {
        boolean coinFlip = (RandomDataGenerator.getRandomIntegerWithRange(1, 10) <= 8);
        ScheduleTest scheduleTest = new ScheduleTest();

        if (coinFlip) {
            return scheduleTest.getExistingSchedule();
        }

        Schedule newSchedule = scheduleTest.getNewRandomSchedule();
        SQLUtil.insert(new ScheduleEntity(), marshall(newSchedule));
        return newSchedule;
    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getRandomObject()
     */
    public Student getRandomObject() {
        Student student = new Student();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();
        student.setStudentId(new BigInteger(RandomDataGenerator.getRandomIntegerWithRange(1000, 10000000) + ""));
        student.setName(fname + " " + lname);
        student.setLevel("Graduate");
        student.setProgram(RandomDataGenerator.getRandomProgram());
        student.setAdmitTerm(RandomDataGenerator.getRandomTerm() + " 2004");
        student.setDepartment(null);
        student.setTotalCredits(new BigInteger(RandomDataGenerator.getRandomIntegerWithRange(0, 45) + ""));
        student.setGPA(RandomDataGenerator.getRandomDoubleWithRange(1, 4, 0.2));
        student.setStatus("Active");

        student.getSchedules().add(getRandomSchedule().getScheduleId());

        // See if any advisors exist
        List<AdvisorEntity> advisors = (List<AdvisorEntity>)SQLUtil.searchXmlExistsNode(AdvisorEntity.class, "xml", "/Advisor/*");
        if (advisors != null && !advisors.isEmpty() && RandomDataGenerator.getRandomIntegerWithRange(0, 10) <= 9) {
            // If there is an advisor, 90% of the time we will set the student to use them
            AdvisorEntity advisorEntity = advisors.get(RandomDataGenerator.getRandomIntegerWithRange(0, advisors.size() - 1));
            Advisor advisorJaxb = (Advisor)unmarshall(advisorEntity.getXml(), Advisor.class);
            logger.log(Level.INFO, "Reusing an advisor instead of making a new one. Using employeeId of {0}.", advisorJaxb.getEmployeeId());
            student.setAdvisor(advisorJaxb.getEmployeeId());
        } else {
            // Create a new advisor and assign it to the Student
            AdvisorTest advisorTest = new AdvisorTest();
            Advisor advisor = (Advisor)advisorTest.getRandomObject();

            SQLUtil.insert(new AdvisorEntity(), marshall(advisor));

            logger.log(Level.INFO, "Creating a new advisor for student. Using employeeId of {0}.", advisor.getEmployeeId());
            student.setAdvisor(advisor.getEmployeeId());
        }

        return student;
    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getXMLFromJAXB()
     */
    public String getXMLFromJAXB() {
        String result = marshall(getRandomObject()).toString();
        return result;
    }

}
