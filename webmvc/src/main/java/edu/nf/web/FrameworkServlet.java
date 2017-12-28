package edu.nf.web;

import edu.nf.web.factory.WebAppHandlerFactory;
import edu.nf.web.utils.ActionDefinitionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FrameworkServlet extends HttpServlet {

    /**
     * Response
     */
    public final static String RESPONSE = "edu.nf.web.FrameWorkServlet.response";

    /**
     * Request
     */
    public final static String REQUEST = "edu.nf.web.FrameWorkServlet.request";

    /**
     * Request scope
     */
    public final static String REQUEST_MAP = "edu.nf.web.FrameWorkServlet.request.map";

    /**
     * Session scope
     */
    public final static String SESSION_MAP = "edu.nf.web.FrameWorkServlet.session.map";

    /**
     * ServletContext scope
     */
    public final static String APPLICATION_MAP = "edu.nf.web.FrameWorkServlet.application.map";


    /**
     * Tomcat, Jetty, JBoss, GlassFish 默认Servlet名称
     * 如果是静态资源或者是控制器无法处理的请求，则交给容器的默认Servlet处理
     */
    private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

    /**
     * 从容器中获取的默认Servlet名
     */
    String defaultServletName;

    final static String ACTION_DEFINITION = "edu.nf.web.actionMapper";

    /**
     * Handler工厂，负责创建handler实例，如果整合IOC容器，则从容器中获取handler，
     * 否则则由webmvc框架来创建handler实例
     */
    public final static String HANDLER_FACTORY = "edu.mf.web.handlerFactory";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 初始化web容器的默认servlet
        initDefaultServlet(config);
        // 初始化ActionMapper请求映射描述
        initActionDefinition(config.getServletContext());
        //初始化handler工厂
        initHandlerFactory(config.getServletContext());
    }

    /**
     * 初始化web容器的默认Servlet名称
     *
     * @param config
     */
    private void initDefaultServlet(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        defaultServletName = servletContext
                .getInitParameter("defaultServletName");
        if (defaultServletName == null || "".equals(defaultServletName)) {
            if (servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = COMMON_DEFAULT_SERVLET_NAME;
            } else {
                throw new IllegalStateException("Unable to locate the default servlet for serving static content. " +
                        "Please set the 'defaultServletName' property explicitly.");
            }
        }
    }

    /**
     * 初始化所有Action请求的描述信息,存入ServletContext作用域中
     *
     * @param servletContext
     */
    private void initActionDefinition(ServletContext servletContext) throws ServletException {
        // 初始化所有Action的描述定义
        Map<String, ActionDefinition> definitionMap = ActionDefinitionUtil.createActionDefinition();
        // 将所有描述定义存入上下文
        servletContext.setAttribute(ACTION_DEFINITION, definitionMap);
    }

    /**
     * 初始化handler工厂,HandlerFactory负责构建具体的Handler(请求处理对象)实例
     */
    private void initHandlerFactory(ServletContext servletContext){
        if(servletContext.getAttribute(HANDLER_FACTORY) == null){
            servletContext.setAttribute(HANDLER_FACTORY, new WebAppHandlerFactory());
        }
    }

    /**
     * 当请求匹配不到相应的映射描述, 则给容器默认的servlet处理
     *
     * @throws IOException
     * @throws ServletException
     */
    protected void forwardDefaultServlet(HttpServletRequest request,
                                         HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getServletContext().getNamedDispatcher(
                defaultServletName);
        if (rd == null) {
            throw new IllegalStateException(
                    "A RequestDispatcher could not be located for the default servlet '"
                            + this.defaultServletName + "'");
        }
        rd.forward(request, response);
    }
}
