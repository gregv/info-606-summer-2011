package org.info606.jpa.entity;

import generated.Advisor;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AdvisorTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = AdvisorTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    @ BeforeClass
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("ADVISOR");
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

        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_advisor.csv"), "Advisor");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_advisor.csv"), "Advisor");

        logger.exiting("testBunchOfInserts", null);
    }

    @ Ignore
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new AdvisorEntity(), this);

        List<AdvisorEntity> list = (List<AdvisorEntity>)SQLUtil.searchXmlExistsNode(AdvisorEntity.class, "xml", "//name=\"Janet Lincecum\"");

        logger.entering("testOneInsert", null);
    }

    public Object getRandomObject() {
        Advisor advisor = new Advisor();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        advisor.setEmployeeId(RandomDataGenerator.getRandomIntegerWithRange(1000, 999999));
        advisor.setEmail(fname + "." + lname + RandomDataGenerator.getRandomEmailAddressSuffix());
        advisor.setLevel("Junior");
        advisor.setName(fname + " " + lname);
        advisor.setOffice("Room 2182");
        advisor.setPhone(RandomDataGenerator.getRandomPhoneNumber());

        return advisor;
    }

}
