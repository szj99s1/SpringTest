package spring.test.freemarker;

import java.io.Writer;

import org.apache.log4j.Logger;

import freemarker.core.Environment;
import freemarker.core.InvalidReferenceException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class SLTemplateExceptionHandler implements
        TemplateExceptionHandler {

    private static Logger logger = Logger
            .getLogger(TemplateExceptionHandler.class);

    @Override
    public void handleTemplateException(TemplateException te, Environment env,
            Writer out) throws TemplateException {
        if (!env.isInAttemptBlock()) {
            if (te instanceof InvalidReferenceException) {
                // 不存在的变量引用，直接返�?
                return;
            }
            // 其余错误，日志记录下，不继续抛错，使得页面继续渲�?
            logger.error("模板解析出错: " + te.getMessage());
        }
    }

}
