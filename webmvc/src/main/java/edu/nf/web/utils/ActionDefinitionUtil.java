package edu.nf.web.utils;

import edu.nf.web.ActionDefinition;
import edu.nf.web.annotation.RequestMapping;

import javax.servlet.ServletException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析所有控制器的请求处理方法，并创建ActionMapper映射描述对象
 */
public class ActionDefinitionUtil {

    public static Map<String, ActionDefinition> createActionDefinition() throws ServletException {
        Map<String, ActionDefinition> definitionMap = new HashMap<>();
        List<String> classNames = getControllerClassNames();
        for (String className : classNames) {
            Class<?> actionClass = createControllerClass(className);
            // 根据class中的请求方法上定义的注解构建ActionMapper实例
            for (Method method : actionClass.getMethods()) {
                // 如果方法上定义了RequestMapping注解，则构建ActionMapper实例
                if(method.isAnnotationPresent(RequestMapping.class)){
                    ActionDefinition actionMapper = resolveMethod(method);
                    //将请求url作为key， actionMapper实例作为value保存到mappers中
                    definitionMap.put(method.getAnnotation(RequestMapping.class).value(), actionMapper);
                }
            }
        }
        return definitionMap;
    }

    /**
     * 扫描项目包，获取或有控制器的全限定类名
     * @return
     */
    private static List<String> getControllerClassNames(){
        return ScanUtil.scanPackage();
    }

    /**
     * 构建控制器Class实例
     * @param className
     * @return
     * @throws ServletException
     */
    private static Class<?> createControllerClass(String className) throws ServletException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Not found controller class: " + className, e);
        }
    }

    /**
     * 解析请求方法，构建ActionMapper
     */
    private static ActionDefinition resolveMethod(Method method){
        ActionDefinition actionDefinition = new ActionDefinition();
        actionDefinition.setControllerMethod(method);
        actionDefinition.setControllerClass(method.getDeclaringClass());
        return actionDefinition;
    }
}
