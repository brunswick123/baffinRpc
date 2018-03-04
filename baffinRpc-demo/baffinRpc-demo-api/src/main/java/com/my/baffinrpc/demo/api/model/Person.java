package com.my.baffinrpc.demo.api.model;

import java.io.Serializable;

public class Person implements Serializable{
    private String name;
    private LocationAddress address;

    public Person(String name, LocationAddress address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }



}

