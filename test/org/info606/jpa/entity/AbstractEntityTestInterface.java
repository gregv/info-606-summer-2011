package org.info606.jpa.entity;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.info606.test.util.EclipseLinkLogParser;

public abstract class AbstractEntityTestInterface {
    protected static final String         ECLIPSELINK_LOG   = "eclipselink.log";
    protected static EclipseLinkLogParser eclipseLinkParser = new EclipseLinkLogParser(ECLIPSELINK_LOG);

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
        System.out.println("-----------------------\n" + result + "\n---------------------");
        return result;
    }
}
