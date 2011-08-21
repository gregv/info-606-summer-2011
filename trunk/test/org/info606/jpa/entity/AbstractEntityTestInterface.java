package org.info606.jpa.entity;

import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.info606.test.util.EclipseLinkLogParser;

public abstract class AbstractEntityTestInterface {
    protected static final String         ECLIPSELINK_LOG             = "eclipselink.log";
    protected static EclipseLinkLogParser eclipseLinkParser           = new EclipseLinkLogParser(ECLIPSELINK_LOG);
    private static Logger                 logger                      = Logger.getLogger(AbstractEntityTestInterface.class.getName());
    private static int                    NUMBER_OF_RECORDS_TO_INSERT = 1;

    public abstract String getXMLFromJAXB();

    public String marshall(Object o) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(o, sw);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }

        String result = sw.toString();
        logger.fine("-----------------------\n" + result + "\n---------------------");
        return result;
    }

    public int getNumberToInsert() {
        String numToInsert = System.getenv("numInsert");
        if (numToInsert != null) {
            try {
                NUMBER_OF_RECORDS_TO_INSERT = Integer.parseInt(numToInsert);
            } catch (Exception e) {
            }
        }

        return NUMBER_OF_RECORDS_TO_INSERT;
    }
}
