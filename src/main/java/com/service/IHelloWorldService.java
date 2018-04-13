package com.service;

import java.rmi.Remote;

public interface IHelloWorldService extends Remote{

    public String sayHello(String name, int age);
}