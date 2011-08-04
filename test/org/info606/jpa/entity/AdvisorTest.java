package org.info606.jpa.entity;

import generated.Advisor;

import java.io.StringWriter;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.info606.jpa.util.EclipseLinkLogParser;
import org.info606.jpa.util.SQLUtil;
import org.info606.test.util.RandomDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdvisorTest {

    private static final String ECLIPSELINK_LOG = "F:\\workspace\\INFO606_Project\\eclipselink.log";

    /**
     * Method: setUpBeforeClass<br/>
     * @throws java.lang.Exception
     */
    @ BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Method: tearDownAfterClass<br/>
     * @throws java.lang.Exception
     */
    @ AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Method: setUp<br/>
     * @throws java.lang.Exception
     */
    @ Before
    public void setUp() throws Exception {
    }

    /**
     * Method: tearDown<br/>
     * @throws java.lang.Exception
     */
    @ After
    public void tearDown() throws Exception {
    }

    private void getAllInserts() {
        EclipseLinkLogParser parser = new EclipseLinkLogParser(ECLIPSELINK_LOG);

        String regex = "InsertObjectQuery.*?total time=(\\d{1,30})";
        parser.match(regex);
    }

    private void getAllSelects() {
        EclipseLinkLogParser parser = new EclipseLinkLogParser(ECLIPSELINK_LOG);

        String regex = "ReadAllQuery\\(referenceClass=MyXML.*?total time=(\\d{1,30})";
        parser.match(regex);

    }

    // Insert into the database using random JAXB object
    private void insert(int numToMake) {
        EntityManager em = SQLUtil.getEntityManager();

        int insertCounter = 0;
        while (insertCounter != numToMake) {
            em.getTransaction().begin();

            AdvisorEntity xmldb = new AdvisorEntity();

            String xml = getXMLFromJAXB();
            Assert.assertTrue("Check that xml is not null", xml != null);
            Assert.assertTrue("Check that xml is not blank", !xml.isEmpty());

            xmldb.setXml(xml);
            em.persist(xmldb);
            em.getTransaction().commit();
            em.close();

            insertCounter++;
        }
    }

    @ Test
    public void testInsert() {
        insert(1);
    }

    public static String getXMLFromJAXB() {

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
        System.out.println("-----------------------\n" + result + "\n---------------------");
        return result;
    }
}
