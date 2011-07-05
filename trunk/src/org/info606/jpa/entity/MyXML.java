package org.info606.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author Greg Vannoni
 * @class CS 575
 *        Purpose:
 *        Notes:
 */
@ Entity
public class MyXML {

    @ Id
    @ GeneratedValue(generator = "InvSeq")
    @ SequenceGenerator(name = "InvSeq", sequenceName = "ID_SEQUENCE", allocationSize = 1)
    private long   id;

    @ Column
    private String xml;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

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

}
