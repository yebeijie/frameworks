package edu.nf.web;

import edu.nf.web.utils.UrlPatternUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 请求映射处理，根据请求的url找到相应的ActionDefinition，
 * 并构建ActionMapper实例
 */
public class HandlerMapping {

    ActionMapper handler(HttpServletRequest request){
        ActionMapper mapper = null;
        //获取请求url
        String urlPattern = UrlPatternUtil.getUrlPattern(request);
        //获取Action的描述定义
        ActionDefinition actionDefinition = getActionDefinition(request, urlPattern);
        if(actionDefinition != null){
            mapper = new ActionMapper();
            mapper.setDefinition(actionDefinition);
        }
        return mapper;
    }

    /**
     * 通过请求url从上下文作用域中找到ActionDefinition
     * @param request
     * @param urlPattern
     * @return
     */
    private ActionDefinition getActionDefinition(HttpServletRequest request, String urlPattern){
        ServletContext servletContext = request.getServletContext();
        Map<String, ActionDefinition> definitions = (Map<String, ActionDefinition>)servletContext.getAttribute(FrameworkServlet.ACTION_DEFINITION);
        return definitions.get(urlPattern);
    }
}
