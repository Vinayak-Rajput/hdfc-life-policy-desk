package com.hdfclife.desk.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class StoreLifeCycle {

    @PostConstruct
    public void init() {
        System.out.println("PolicyStore ready");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PolicyStore shutdown");
    }

}
