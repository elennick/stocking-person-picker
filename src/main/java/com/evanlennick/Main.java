package com.evanlennick;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {

    private static final int LEFT = 0;

    private static final int RIGHT = 1;

    private static Properties props;

    public static void main(String[] args) throws MessagingException, IOException {
        props = System.getProperties();
        InputStream is = Main.class.getClassLoader().getResourceAsStream(("stockings.properties"));
        props.load(is);

        List<Pair> pairs = new ArrayList<>();
        pairs.add(createPersonSoPair("Evan", "elennick@gmail.com", "Nat", "nlennick@gmail.com"));
//        pairs.add(createPersonSoPair("Nick", "ndiascro@gmail.com", "Lauren", "?"));
//        pairs.add(createPersonSoPair("Tony", "adiascro@gmail.com", "Gillian", "?"));
//        pairs.add(createPersonSoPair("Dom", "ddiascro@gmail.com ", "Jen", "jen2776@gmail.com "));

        assignStockings(pairs);

        List<Person> people = pairs.stream()
                .flatMap(p -> p.getPeople().stream())
                .collect(Collectors.toList());

        for (Person person : people) {
            System.out.println("person = " + person);
            if (null != person.getStockingPerson()) {
                sendPickEmail(person.getStockingPerson().getFirstName(), person.getEmailAddress());
            }
        }
    }

    private static void assignStockings(List<Pair> pairs) {
        assignStockingsForSide(pairs, LEFT);
        assignStockingsForSide(pairs, RIGHT);
    }

    private static void assignStockingsForSide(List<Pair> pairs, int side) {
        Collections.shuffle(pairs);
        for (int i = 0; i < pairs.size(); i++) {
            int indexOfNextPair = i + 1;
            if (indexOfNextPair >= pairs.size()) {
                indexOfNextPair = 0;
            }

            if (side == LEFT) {
                Person stockingPerson = pairs.get(indexOfNextPair).getPerson2();
                pairs.get(i).getPerson1().setStockingPerson(stockingPerson);
            } else if (side == RIGHT) {
                Person stockingPerson = pairs.get(indexOfNextPair).getPerson1();
                pairs.get(i).getPerson2().setStockingPerson(stockingPerson);
            } else {
                throw new IllegalArgumentException("Unrecognized side argument!");
            }
        }
    }

    private static void sendPickEmail(String personPicked, String email) throws MessagingException {
        String content = "Congratulations! You get to buy a stocking gift for " + personPicked + "! Aren't you lucky!";

        Session getMailSession = Session.getDefaultInstance(props, null);
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject("Christmas 2016 Stocking Pick");
        generateMailMessage.setContent(content, "text/html");

        Transport transport = getMailSession.getTransport("smtp");

        final boolean emailEnabled = Boolean.valueOf(System.getProperty("email.enabled"));
        if (emailEnabled) {
            transport.connect(
                    System.getProperty("email.server"),
                    System.getProperty("email.username"),
                    System.getProperty("email.password"));
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
            System.out.println("Email sent to " + email + " saying that they got " + personPicked);
        }
    }

    private static Pair createPersonSoPair(String name1, String email1, String name2, String email2) {
        Person person1 = new Person(name1, email1);
        Person person2 = new Person(name2, email2);
        return new Pair(person1, person2);
    }

}
