package com.di.di.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.di.di.domain.Bar;
import com.di.di.domain.DiContainer;
import com.di.di.domain.Foo;
import com.di.di.domain.annotation.Inject;

@RestController
@RequestMapping("api/di")
public class SampleController {

    @RequestMapping("/")
    String display() {

        Bar bar = (Bar) DiContainer.getBean("bar");
        String message = bar.getMessageFoo();

        return message;
    }
}
