package edu.test;

import edu.demo.controller.UserController;
import edu.nf.beans.BeanFactory;
import org.junit.Test;

public class UserControllerTest {

    @Test
    public void testAdd(){
        BeanFactory factory = new BeanFactory("edu.demo");
        UserController controller = factory.getBean("controller", UserController.class);
        controller.add();

    }
}
