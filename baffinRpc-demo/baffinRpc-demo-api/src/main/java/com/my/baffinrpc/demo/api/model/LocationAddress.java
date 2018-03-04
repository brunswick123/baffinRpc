package com.my.baffinrpc.demo.api.model;

import java.io.Serializable;

public class LocationAddress implements Serializable{
    private int no;
    private String street;

    public LocationAddress(int no, String street) {
        this.no = no;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "no=" + no +
                ", street='" + street + '\'' +
                '}';
    }
}
