package org.info606.jpa.entity;

import generated.CourseInfo.Course;
import generated.DayOfWeekType;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.Ignore;
import org.junit.Test;

public class CourseTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = CourseTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ Test
    public void testBunchOfInserts() {
        logger.entering("testBunchOfInserts", null);

        int counter = 0;
        while (counter != getNumberToInsert()) {
            testOneInsert();
            counter++;
        }

        eclipseLinkParser.saveAllSelectPerformance(new File("select_results.csv"), "Course");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results.csv"), "Course");

        logger.exiting("testBunchOfInserts", null);
    }

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insert(1, new CourseEntity(), this);

        List<CourseEntity> list = (List<CourseEntity>)SQLUtil.searchXmlExistsNode(CourseEntity.class, "xml", "//name=\"Janet Lincecum\"");

        logger.entering("testOneInsert", null);
    }

    public String getXMLFromJAXB() {

        Course course = new Course();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        course.setCapacity((short)RandomDataGenerator.getRandomNumberWithRange(0, 50));
        course.setFilled((short)RandomDataGenerator.getRandomNumberWithRange(0, course.getCapacity()));
        course.setRemaining((short)(course.getCapacity() - course.getFilled()));
        course.setWaitlist((short)0);

        course.setCoursePrefix(RandomDataGenerator.getRandomCoursePrefix());
        course.setCredits(RandomDataGenerator.getRandomNumberWithRange(0, 5));

        DayOfWeekType[] days = {DayOfWeekType.M, DayOfWeekType.W, DayOfWeekType.F};
        course.getDay().addAll(Arrays.asList(days));

        course.setCrn(new BigInteger("12345"));
        course.setStarttime(null);
        course.setEndtime(null);

        course.setInstructor(fname + " " + lname);
        course.setLocation(RandomDataGenerator.getRandomLocation());

        String result = marshall(course).toString();

        return result;
    }
}
