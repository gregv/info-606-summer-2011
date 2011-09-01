package org.info606.jpa.entity;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.info606.test.util.EclipseLinkLogParser;

public abstract class AbstractEntityTestInterface {
    protected static final String         ECLIPSELINK_LOG             = "eclipselink.log";
    protected static EclipseLinkLogParser eclipseLinkParser           = new EclipseLinkLogParser(ECLIPSELINK_LOG);
    private static Logger                 logger                      = Logger.getLogger(AbstractEntityTestInterface.class.getName());
    private static int                    NUMBER_OF_RECORDS_TO_INSERT = 1;
    private static final String           CLASS_NAME                  = AbstractEntityTestInterface.class.getName();

    public abstract Object getRandomObject();

    public String getXMLFromJAXB() {
        return marshall(getRandomObject()).toString();
    }

    public static boolean shouldTruncateTables() {
        logger.entering(CLASS_NAME, "truncateTables");
        boolean result = false;

        String truncate = System.getenv("truncate");
        if (truncate != null && truncate.equalsIgnoreCase("yes") || truncate.equalsIgnoreCase("true")) {
            result = true;
        } else {
            logger.fine("truncate was set to false, not truncating tables");
        }

        logger.exiting(CLASS_NAME, "truncateTables", result);
        return result;
    }

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

    public Object unmarshall(String str, Class targetClass) {
        Object result = null;

        try {
            JAXBContext jc = JAXBContext.newInstance(targetClass);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            result = unmarshaller.unmarshal(new StringReader(str));
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }

        return result;
    }

    public int getNumberToInsert() {
        String numToInsert = System.getenv("numInsert");
        if (numToInsert != null) {
            try {
                NUMBER_OF_RECORDS_TO_INSERT = Integer.parseInt(numToInsert);
            } catch (Exception e) {
            }
        } else {
            logger.warning("*** Environment variable \"numInsert\" was not set, defaulting to 1.");
        }

        return NUMBER_OF_RECORDS_TO_INSERT;
    }
}
