package com.iyuba.CET4bible.viewpager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Simple implements InvocationHandler {

    private Object proxied ;

    public Simple(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
