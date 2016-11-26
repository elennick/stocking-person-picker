package com.evanlennick;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {

    private static Properties props;

    public static void main(String[] args) throws MessagingException, IOException {
        props = System.getProperties();
        InputStream is = Main.class.getClassLoader().getResourceAsStream(("stockings.properties"));
        props.load(is);

        List<Person> people = new LinkedList<>();
        people.addAll(createPersonPair("Evan", "elennick@gmail.com", "Nat", "nlennick@gmail.com"));
        people.addAll(createPersonPair("Nick", "ndiascro@gmail.com", "Lauren", "lbaaronson@gmail.com"));
        people.addAll(createPersonPair("Tony", "adiascro@gmail.com", "Gillian", "garnaboldi7@gmail.com"));
        people.addAll(createPersonPair("Dom", "ddiascro@gmail.com ", "Jen", "jen2776@gmail.com "));

        assignStockings(people);

        for (Person person : people) {
            //System.out.println("final list = " + person);
            if (null != person.getStockingPerson()) {
                sendPickEmail(person.getStockingPerson().getFirstName(), person.getEmailAddress());
            }
        }
    }

    private static void assignStockings(List<Person> people) {
        boolean listIsGood = false;
        while (!listIsGood) {
            Collections.shuffle(people);
            for (int i = 0; i < people.size(); i++) {
                Person stockingPerson;
                if (i < people.size() - 1) {
                    stockingPerson = people.get(i + 1);
                } else {
                    stockingPerson = people.get(0);
                }
                people.get(i).setStockingPerson(stockingPerson);
            }
            //System.out.println("current list = " + people);
            listIsGood = listIsGood(people);
            //System.out.println("list is good = " + listIsGood);
        }
    }

    private static boolean listIsGood(List<Person> people) {
        long count = people.stream()
                .filter(p -> p.getStockingPerson().equals(p.getSignificantOther()))
                .count();
        return count <= 0;
    }

    private static void sendPickEmail(String personPicked, String email) throws MessagingException {
        final boolean emailEnabled = Boolean.valueOf(System.getProperty("email.enabled"));
        if (emailEnabled) {
            try {
                String content = "Congratulations! You get to buy a stocking gift for " + personPicked + "! Aren't you lucky!";

                Session getMailSession = Session.getDefaultInstance(props, null);
                MimeMessage generateMailMessage = new MimeMessage(getMailSession);
                generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                generateMailMessage.setSubject("Christmas 2016 Stocking Pick");
                generateMailMessage.setContent(content, "text/html");

                Transport transport = getMailSession.getTransport("smtp");

                transport.connect(
                        System.getProperty("email.server"),
                        System.getProperty("email.username"),
                        System.getProperty("email.password"));
                transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                transport.close();
                //System.out.println("Email sent to " + email + " saying that they got " + personPicked);
                System.out.println("email sent successfully!");
            } catch (Exception e) {
                System.out.println("Exception encountered sending email to " + email + "! Cause: " + e.getMessage());
            }
        }
    }

    private static List<Person> createPersonPair(String name1, String email1, String name2, String email2) {
        Person person1 = new Person(name1, email1);
        Person person2 = new Person(name2, email2);
        person1.setSignificantOther(person2);
        person2.setSignificantOther(person1);
        return Arrays.asList(person1, person2);
    }

}
