package org.info606.jpa.entity;

import generated.Course;
import generated.DayOfWeekType;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CourseTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = CourseTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("COURSE");
        }
    }

    @ Test
    public void testBunchOfInserts() {
        logger.entering("testBunchOfInserts", null);

        int counter = 0;
        while (counter != getNumberToInsert()) {
            testOneInsert();
            counter++;
        }

        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_course.csv"), "Course");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_course.csv"), "Course");

        logger.exiting("testBunchOfInserts", null);
    }

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new CourseEntity(), this);

        List<CourseEntity> list = (List<CourseEntity>)SQLUtil.searchXmlExistsNode(CourseEntity.class, "xml", "//name=\"Janet Lincecum\"");

        logger.entering("testOneInsert", null);
    }

    private Course getRandomCourse() {
        Course course = new Course();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        course.setCapacity((short)RandomDataGenerator.getRandomIntegerWithRange(0, 50));
        course.setFilled((short)RandomDataGenerator.getRandomIntegerWithRange(0, course.getCapacity()));
        course.setRemaining((short)(course.getCapacity() - course.getFilled()));
        course.setWaitlist((short)0);

        course.setCoursePrefix(RandomDataGenerator.getRandomCoursePrefix());
        course.setCredits(RandomDataGenerator.getRandomIntegerWithRange(0, 5));

        DayOfWeekType[] days = {DayOfWeekType.M, DayOfWeekType.W, DayOfWeekType.F};
        course.getDay().addAll(Arrays.asList(days));

        course.setCrn(RandomDataGenerator.getRandomIntegerWithRange(100, 999));
        course.setStarttime(null);
        course.setEndtime(null);

        course.setInstructor(fname + " " + lname);
        course.setLocation(RandomDataGenerator.getRandomLocation());

        course.setCourseId(course.getCoursePrefix() + course.getCrn());
        return course;
    }

    public Object getRandomObject() {
        Course course = null;

        int numResults = 0;

        do {
            course = getRandomCourse();
            String courseId = course.getCourseId();

            String xpath = "/Course[courseId = \"" + courseId + "\"]";
            numResults = SQLUtil.searchXmlExistsNode(CourseEntity.class, "xml", xpath).size();
            logger.log(Level.INFO, "There are {0} courses that have the courseId of {1}.", new Object[] {numResults, courseId});
        } while (numResults >= 1);

        logger.log(Level.INFO, "Returning course with courseId {0}.", new Object[] {course.getCourseId()});
        return course;
    }
}
