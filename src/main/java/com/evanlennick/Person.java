package com.evanlennick;

public class Person {

    private String firstName;

    private String emailAddress;

    private Person stockingPerson;

    private Person significantOther;

    public Person(String firstName) {
        this.firstName = firstName;
    }

    public Person(String firstName, String emailAddress) {
        this.firstName = firstName;
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }

    public Person getStockingPerson() {
        return stockingPerson;
    }

    public void setStockingPerson(Person stockingPerson) {
        this.stockingPerson = stockingPerson;
    }

    public Person getSignificantOther() {
        return significantOther;
    }

    public void setSignificantOther(Person significantOther) {
        this.significantOther = significantOther;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Person{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');

        if(null != stockingPerson) {
            sb.append(", stockingPerson=").append(stockingPerson.firstName);
        }

        if(null != significantOther) {
            sb.append(", significantOther=").append(significantOther.firstName);
        }

        sb.append('}');
        return sb.toString();
    }
}
