package org.info606.jpa.entity;

import generated.Student;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.Ignore;
import org.junit.Test;

public class StudentTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = StudentTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ Test
    public void testBunchOfInserts() {
        logger.entering("testBunchOfInserts", null);
        int counter = 0;
        while (counter != getNumberToInsert()) {
            testOneInsert();
            counter++;
        }
        eclipseLinkParser.saveAllSelectPerformance(new File("select_results.csv"), "Student");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results.csv"), "Student");
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
        student.setStudentId(new BigInteger("234567"));
        student.setName(fname + " " + lname);
        student.setLevel("Graduate");
        student.setProgram(RandomDataGenerator.getRandomProgram());
        student.setAdmitTerm(RandomDataGenerator.getRandomTerm() + " 2008");
        student.setDepartment(null);
        student.setAdvisor(fname + " " + lname);
        student.setTotalCredits(new BigInteger(RandomDataGenerator.getRandomNumberWithRange(0, 45) + ""));
        student.setGPA(RandomDataGenerator.getRandomNumberWithRange(0, 4));
        student.setStatus("Active");
        String result = marshall(student).toString();
        return result;
    }

}
