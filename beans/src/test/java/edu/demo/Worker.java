package edu.demo;

import edu.nf.beans.annotations.Component;
import edu.nf.beans.annotations.Scope;

@Component("worker")
@Scope("prototype")
public class Worker {
    public void work(Tools tools){
        tools.repair();
    }
}
