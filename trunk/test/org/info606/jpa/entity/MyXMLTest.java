package org.info606.jpa.entity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.info606.book.AuthorType;
import org.info606.book.Book;
import org.info606.jpa.util.EclipseLinkLogParser;
import org.info606.jpa.util.SQLUtil;
import org.info606.test.util.RandomDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class MyXMLTest {

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
        int insertCounter = 0;
        while (insertCounter != numToMake) {
            // System.out.println("Insert # " + insertCounter + ",");

            // If you would rather insert via XML File, use this line
            // SQLUtil.insertXml(FileIO.getFileContentsAsString("Book.xml", false));
            // SQLUtil.insertXml(FileIO.getFileContentsAsString("Registration.xml", false));
            // SQLUtil.insertXml(getXMLFromJAXB());
            insertCounter++;
        }
    }

    @ Ignore
    public void testPerformance() {
        Assert.assertTrue(SQLUtil.truncateTable("registeredXml") >= 0);
        int numberOfIterations = 500;

        for (int i = 0; i < numberOfIterations; i++) {
            insert(500);
            testPerformance2();
        }

        System.out.println("\n\n\nInserts");
        getAllInserts();

        System.out.println("\n\n\nSelects");
        getAllSelects();
    }

    @ Ignore
    private void testPerformance2() {
        // System.out.println("Rows = " + SQLUtil.searchXmlExistsNode("xml", "/books/book/description").size());
        System.out.println("Rows = " + SQLUtil.searchXmlExtract("xml", "/book/description/text()", "This is a description").size());
        System.out.println("Rows = " + SQLUtil.searchXmlExistsNode("xml", "/book/author[firstname=\"Joe\"]").size());
        System.out.println("Rows = " + SQLUtil.searchXmlExistsNode("xml", "/book/author[firstname=\"Joe\"]").size());
        System.out.println("Rows = " + SQLUtil.searchXmlExtract("xml", "/book/author/firstname/text()", "Joe").size());
        getAllSelects();
    }

    @ Test
    public void printXML() {
        System.out.println(getXMLFromJAXB());
    }

    public static String getXMLFromJAXB() {
        Book blah = new Book();
        blah.setDescription("This is a description");
        blah.setTitle("Title of a Book");
        blah.getAuthor().addAll(getRandomAuthors());
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(Book.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(blah, sw);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        return sw.toString();
    }

    private static List<AuthorType> getRandomAuthors() {
        Random ra = new Random();
        int numberofAuthors = ra.nextInt(3) + 1;
        List<AuthorType> authors = new ArrayList<AuthorType>();
        for (int i = 0; i < numberofAuthors; i++) {
            AuthorType author = new AuthorType();
            author.setFirstname(RandomDataGenerator.getRandomFirstname());
            author.setLastname(RandomDataGenerator.getRandomLastname());
            authors.add(author);
        }
        return authors;
    }

}
