package org.info606.jpa.entity;

import generated.Advisor;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.info606.test.util.RandomDataGenerator;
import org.info606.test.util.SQLUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @class INFO 606
 *        Purpose: Test the Advisor table/entity
 *        Notes:
 */
public class AdvisorTest extends AbstractEntityTestInterface {

    private static final String CLASS_NAME = AdvisorTest.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    /**
     * Method: truncateTables<br/>
     * This will truncate the table if the user's environment variable is set as such
     */
    @ BeforeClass
    public static void truncateTables() {
        if (shouldTruncateTables()) {
            SQLUtil.truncateTable("ADVISOR");
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

        eclipseLinkParser.saveAllSelectPerformance(new File("select_results_advisor.csv"), "Advisor");
        eclipseLinkParser.saveAllInsertPerformance(new File("insert_results_advisor.csv"), "Advisor");

        logger.exiting("testBunchOfInserts", null);
    }

    /**
     * Method: testOneInsert<br/>
     * This method will insert a single random record into the database
     */
    public void testOneInsert() {
        logger.entering("testOneInsert", null);
        SQLUtil.insertRandom(1, new AdvisorEntity(), this);
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        List<AdvisorEntity> list = (List<AdvisorEntity>)SQLUtil.searchXmlExistsNode(AdvisorEntity.class, "xml", "/Advisor[name=\"" + fname + " " + lname + "\"]");

        logger.entering("testOneInsert", null);
    }

    /*
     * (non-Javadoc)
     * @see org.info606.jpa.entity.AbstractEntityTestInterface#getRandomObject()
     */
    public Object getRandomObject() {
        logger.entering(CLASS_NAME, "getRandomObject");
        Advisor advisor = new Advisor();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        advisor.setEmployeeId(RandomDataGenerator.getRandomIntegerWithRange(1000, 999999));
        advisor.setEmail(fname + "." + lname + RandomDataGenerator.getRandomEmailAddressSuffix());
        advisor.setLevel("Junior");
        advisor.setName(fname + " " + lname);
        advisor.setOffice("Room 2182");
        advisor.setPhone(RandomDataGenerator.getRandomPhoneNumber());

        logger.info("Returning advisor: " + fname + " " + lname + " with employeeId of " + advisor.getEmployeeId());
        logger.exiting(CLASS_NAME, "getRandomObject");
        return advisor;
    }

}
