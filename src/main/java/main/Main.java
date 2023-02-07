package main;

import dao.util.ConnectionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("service.xml");

        ConnectionManager connectionManager = xmlContext.getBean("ConnectionManager",
                ConnectionManager.class);
        EntityManager entityManager = connectionManager.open();


    }
}
