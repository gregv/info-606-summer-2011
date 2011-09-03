package org.info606.jpa.entity;

import generated.Course;
import generated.Schedule;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @class INFO 606
 *        Purpose: Test the Schedule table/entity
 *        Notes:
 */
public class ScheduleTest extends AbstractEntityTestInterface {
    private static final String CLASS_NAME = ScheduleTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    /**
     * Method: truncateTables<br/>
     * This will truncate the table if the user's environment variable is set as such
     */
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("SCHEDULE");
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
            testOneInsert();
            counter++;
        }
        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_course.csv"), "Schedule");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_course.csv"), "Schedule");
        logger.exiting("testBunchOfInserts", null);
    }

    /**
     * Method: testOneInsert<br/>
     * This method will insert a single random record into the database
     */
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new ScheduleEntity(), this);

        String term = RandomDataGenerator.getRandomTerm().value();
        List<ScheduleEntity> list = (List<ScheduleEntity>)SQLUtil.searchXmlExistsNode(ScheduleEntity.class, "xml", "/Schedule[term=\"" + term + "\"]");
        logger.entering("testOneInsert", null);
    }

    /**
     * Method: getRandomSchedule<br/>
     * Returns a random schedule
     * @return
     */
    private Schedule getRandomSchedule() {
        Schedule schedule = new Schedule();
        schedule.setAcademicYear(RandomDataGenerator.getRandomIntegerWithRange(1990, 2015));
        schedule.setTerm(RandomDataGenerator.getRandomTerm());
        schedule.setScheduleId(schedule.getAcademicYear() + "" + schedule.getTerm());

        return schedule;
    }

    /**
     * Method: doesScheduleExists<br/>
     * Checks to see if the schedule exists
     * @param scheduleId
     * @return true if schedule exists, false otherwise
     */
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

    /**
     * Method: getNewRandomSchedule<br/>
     * Ensures the schedule being returned is new (not exisiting in database)
     * @return
     */
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

    /**
     * Method: getExistingSchedule<br/>
     * Ensures the schedule returned exists in the database
     * @return
     */
    public Schedule getExistingSchedule() {
        Schedule schedule = null;

        // Make a new schedule if there aren't any in the database
        EntityManager em = SQLUtil.getEntityManager();
        String countSQL = "SELECT count(*) from SCHEDULE";
        BigDecimal bd = (BigDecimal)em.createNativeQuery(countSQL).getSingleResult();

        if (bd.intValue() == 0) {
            return getNewRandomSchedule();
        }

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

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getRandomObject()
     */
    public Object getRandomObject() {
        logger.entering(CLASS_NAME, "getRandomObject");

        Schedule schedule = getNewRandomSchedule();

        logger.exiting(CLASS_NAME, "getRandomObject");
        return schedule;
    }
}
