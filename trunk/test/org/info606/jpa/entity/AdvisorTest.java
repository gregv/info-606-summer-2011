package org.info606.jpa.entity;

import generated.Advisor;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

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
        SQLUtil.insert(1, new AdvisorEntity(), this);

        List<AdvisorEntity> list = (List<AdvisorEntity>)SQLUtil.searchXmlExistsNode(AdvisorEntity.class, "xml", "//name=\"Janet Lincecum\"");

        logger.entering("testOneInsert", null);
    }

    public String getXMLFromJAXB() {

        Advisor advisor = new Advisor();
        String fname = RandomDataGenerator.getRandomFirstname();
        String lname = RandomDataGenerator.getRandomLastname();

        advisor.setEmail(fname + "." + lname + RandomDataGenerator.getRandomEmailAddressSuffix());
        advisor.setLevel("Junior");
        advisor.setName(fname + " " + lname);
        advisor.setOffice("Room 2182");
        advisor.setPhone(RandomDataGenerator.getRandomPhoneNumber());

        StringWriter sw = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(advisor.getClass());
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(advisor, sw);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }

        String result = sw.toString();
        logger.fine("-----------------------\n" + result + "\n---------------------");
        return result;
    }
}
