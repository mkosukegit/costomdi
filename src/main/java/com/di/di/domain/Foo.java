package com.di.di.domain;

import com.di.di.domain.annotation.BeanCustom;

@BeanCustom
public class Foo {

    public String getMessage() {
        return "Inject Completed !!";
    }
}
