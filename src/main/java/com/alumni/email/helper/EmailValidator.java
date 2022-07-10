package com.alumni.email.helper;


import org.springframework.stereotype.Service;

import java.util.function.Predicate;


@Service
public class EmailValidator implements Predicate<String> {


    @Override
    public boolean test(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (email.matches(regex))
            return true;
        else
            return false;
    }
}
