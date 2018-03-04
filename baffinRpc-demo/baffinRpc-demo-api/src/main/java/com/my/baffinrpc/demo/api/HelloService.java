package com.my.baffinrpc.demo.api;


import com.my.baffinrpc.demo.api.model.Person;

public interface HelloService {

    String hello(String name, int number);

    String hiPerson(Person person);
}
