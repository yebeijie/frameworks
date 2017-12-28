package edu.nf.plugin;

import edu.nf.beans.BeanFactory;
import edu.nf.web.FrameworkServlet;
import edu.nf.web.HandlerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener{

    private final static String SCAN_PACKAGE = "scanPackage";

    /**
     * 初始化容器
     */
    public void contextInitialized(ServletContextEvent event) {
        String path = event.getServletContext().getInitParameter(SCAN_PACKAGE);
        //创建并初始化IOC容器工厂
        BeanFactory factory = new BeanFactory(path);
        //初始化HandlerFactory
        HandlerFactory handlerFactory = new ContainerHandlerFactory(factory);
        //将HandlerFactory保存到ServletContext作用域
        event.getServletContext().setAttribute(FrameworkServlet.HANDLER_FACTORY, handlerFactory);
    }

    /**
     * 关闭容器
     */
    public void contextDestroyed(ServletContextEvent event) {
        ContainerHandlerFactory handlerFactory = (ContainerHandlerFactory)event.getServletContext().getAttribute(FrameworkServlet.HANDLER_FACTORY);
        handlerFactory.getBeanFactory().close();
        event.getServletContext().removeAttribute(FrameworkServlet.HANDLER_FACTORY);
    }
}
