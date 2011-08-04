package org.info606.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private static final String         PERSISTENCE_UNIT_NAME = "myem";
    private static EntityManagerFactory factory               = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private static EntityManager        em;

    public static void main(String args[]) {
    }

}
