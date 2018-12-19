package com.mageddo.service;

import org.springframework.stereotype.Service;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/11/16 5:50 PM
 */
@Service
public class HelloService {
    public String getHelloMessage(){
        return "Hello World!!!";
    }
}
