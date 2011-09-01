package org.info606.jpa.entity;

import generated.Course;
import generated.Schedule;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ScheduleTest extends AbstractEntityTestInterface {
    private static final String CLASS_NAME = ScheduleTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("SCHEDULE");
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
        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_course.csv"), "Schedule");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_course.csv"), "Schedule");
        logger.exiting("testBunchOfInserts", null);
    }

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new ScheduleEntity(), this);
        List<ScheduleEntity> list = (List<ScheduleEntity>)SQLUtil.searchXmlExistsNode(ScheduleEntity.class, "xml", "//name=\"Janet Lincecum\"");
        logger.entering("testOneInsert", null);
    }

    private Schedule getRandomSchedule() {
        Schedule schedule = new Schedule();
        schedule.setAcademicYear(RandomDataGenerator.getRandomIntegerWithRange(2002, 2011));
        schedule.setTerm(RandomDataGenerator.getRandomTerm());
        schedule.setScheduleId(schedule.getAcademicYear() + "" + schedule.getTerm());

        return schedule;
    }

    private boolean doesScheduleExists(String scheduleId) {
        int numResults = 0;

        String xpath = "/Schedule[scheduleId = \"" + scheduleId + "\"]";
        List<ScheduleEntity> existingSchedules = (List<ScheduleEntity>)SQLUtil.searchXmlExistsNode(ScheduleEntity.class, "xml", xpath);
        numResults = existingSchedules.size();

        if (numResults > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Schedule getNewRandomSchedule() {
        Schedule schedule = null;

        boolean scheduleExists = true;
        do {
            schedule = getRandomSchedule();
            String scheduleId = schedule.getScheduleId();

            scheduleExists = doesScheduleExists(scheduleId);
            logger.log(Level.INFO, "There is {0} a schedule that has the scheduleId of {1}.", new Object[] {scheduleExists ? "" : "no", scheduleId});
        } while (scheduleExists);

        int numCourses = RandomDataGenerator.getRandomIntegerWithRange(5, 30);

        logger.log(Level.INFO, "Generating {0} new random courses.", numCourses);

        for (int i = 0; i < numCourses; i++) {
            CourseTest courseTest = new CourseTest();
            Course course = (Course)courseTest.getRandomObject();

            // Store each course
            SQLUtil.insert(new CourseEntity(), marshall(course));

            schedule.getCourses().add(course.getCourseId());
        }

        logger.log(Level.INFO, "Returning new schedule that has the scheduleId of {0}.", new Object[] {schedule.getScheduleId()});
        return schedule;
    }

    public Schedule getExistingSchedule() {
        Schedule schedule = null;

        boolean scheduleExists = true;
        do {
            schedule = getRandomSchedule();
            String scheduleId = schedule.getScheduleId();

            scheduleExists = doesScheduleExists(scheduleId);
            logger.log(Level.INFO, "There is {0} a schedule that has the scheduleId of {1}.", new Object[] {scheduleExists ? "" : "no", scheduleId});
        } while (!scheduleExists);

        logger.log(Level.INFO, "Returning existing schedule that has the scheduleId of {0}.", new Object[] {schedule.getScheduleId()});
        return schedule;
    }

    public Object getRandomObject() {
        logger.entering(CLASS_NAME, "getRandomObject");

        Schedule schedule = getNewRandomSchedule();

        logger.exiting(CLASS_NAME, "getRandomObject");
        return schedule;
    }
}
