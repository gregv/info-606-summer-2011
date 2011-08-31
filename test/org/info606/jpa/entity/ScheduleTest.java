package org.info606.jpa.entity;

import generated.Registration;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.Ignore;
import org.junit.Test;

public class ScheduleTest extends AbstractEntityTestInterface {
    private static final String CLASS_NAME = ScheduleTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ Test
    public void testBunchOfInserts() {
        logger.entering("testBunchOfInserts", null);
        int counter = 0;
        while (counter != getNumberToInsert()) {
            testOneInsert();
            counter++;
        }
        eclipseLinkParser.saveAllSelectPerformance(new File("select_results.csv"), "Schedule");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results.csv"), "Schedule");
        logger.exiting("testBunchOfInserts", null);
    }

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insert(1, new ScheduleEntity(), this);
        List<ScheduleEntity> list = (List<ScheduleEntity>)SQLUtil.searchXmlExistsNode(ScheduleEntity.class, "xml", "//name=\"Janet Lincecum\"");
        logger.entering("testOneInsert", null);
    }

    public String getXMLFromJAXB() {
        Registration schedule = new Registration();
        schedule.setAcademicYear(2008);
        schedule.setTerm(RandomDataGenerator.getRandomTerm());
        String result = marshall(schedule).toString();
        return result;
    }

}
