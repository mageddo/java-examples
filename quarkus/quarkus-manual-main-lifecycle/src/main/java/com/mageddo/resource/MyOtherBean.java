package com.mageddo.resource;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyOtherBean {

    public String hello() {
        return "hello";
    }

    public String bye() {
        return "bye bye";
    }

}
