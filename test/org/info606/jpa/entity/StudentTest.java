package org.info606.jpa.entity;

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
import org.junit.Ignore;
import org.junit.Test;

public class StudentTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = StudentTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("STUDENT");
        }
    }

    @ Test
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

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insert(1, new StudentEntity(), this);
        List<StudentEntity> list = (List<StudentEntity>)SQLUtil.searchXmlExistsNode(StudentEntity.class, "xml", "//name=\"Janet Lincecum\"");
        logger.entering("testOneInsert", null);
    }

    public String getXMLFromJAXB() {
        Student student = new Student();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();
        // student.setStudentId(new BigInteger("234567"));
        student.setStudentId(new BigInteger(RandomDataGenerator.getRandomIntegerWithRange(1000, 10000000) + ""));
        student.setName(fname + " " + lname);
        student.setLevel("Graduate");
        student.setProgram(RandomDataGenerator.getRandomProgram());
        student.setAdmitTerm(RandomDataGenerator.getRandomTerm() + " 2008");
        student.setDepartment(null);
        student.setAdvisor(fname + " " + lname);
        student.setTotalCredits(new BigInteger(RandomDataGenerator.getRandomIntegerWithRange(0, 45) + ""));
        student.setGPA(RandomDataGenerator.getRandomDoubleWithRange(1, 4, 0.2));
        student.setStatus("Active");
        String result = marshall(student).toString();
        return result;
    }

}
