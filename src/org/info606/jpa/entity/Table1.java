package org.info606.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Greg Vannoni
 * @class CS 575
 *        Purpose:
 *        Notes:
 */
@ Entity(name = "Table1")
public class Table1 {

    @ Id
    private String column1;

    /**
     * @return the column1
     */
    public String getColumn1() {
        return column1;
    }

    /**
     * @param column1 the column1 to set
     */
    public void setColumn1(String column1) {
        this.column1 = column1;
    }

}
