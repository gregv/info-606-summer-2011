package org.info606.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @class INFO606
 *        Purpose: Provides everything the concrete entities need
 *        Notes:
 */
@ MappedSuperclass
public abstract class AbstractXmlTypeEntity {

    @ Id
    @ Column(name = "OBJECT_VALUE")
    private String xml;

    /**
     * @return the xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * @param xml the xml to set
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    public String toString() {
        return "xml: " + getXml();
    }
}
