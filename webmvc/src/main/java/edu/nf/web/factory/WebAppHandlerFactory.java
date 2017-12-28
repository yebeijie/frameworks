package edu.nf.web.factory;

import edu.nf.web.ActionDefinition;
import edu.nf.web.HandlerFactory;

public class WebAppHandlerFactory implements HandlerFactory{

    @Override
    public Object crateAction(ActionDefinition definition) {
        try {
            return definition.getControllerClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Create handler instance fail.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Create handler instance fail.", e);
        }
    }
}
