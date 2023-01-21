package com.di.di.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.di.di.domain.annotation.BeanCustom;
import com.di.di.domain.annotation.Inject;

@BeanCustom
public class Bar {

    @Autowired
    Foo foo;

    public String getMessageFoo() {
        return foo.getMessage();
    }
}
