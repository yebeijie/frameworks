package edu.test;

import edu.demo.Worker;
import edu.nf.beans.BeanFactory;
import org.junit.Test;

public class BeanFactoryTest {
    @Test
    public void testGetBean(){
        //创建工厂容器
        BeanFactory factory = new BeanFactory("edu.demo");
        //从容器中直接获取Worker对象
        Worker w1 = factory.getBean("worker", Worker.class);
        Worker w2 = factory.getBean("worker", Worker.class);
        System.out.println(w1 == w2);
    }
}
