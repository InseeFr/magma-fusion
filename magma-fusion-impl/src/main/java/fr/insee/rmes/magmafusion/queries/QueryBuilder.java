package fr.insee.rmes.magmafusion.queries;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Component
@Slf4j
public record QueryBuilder(Configuration freemarkerConfiguration) {
    public Query build(Map<String, Object> parameters, String queryfile) {
        try {
            Template template = freemarkerConfiguration.getTemplate(queryfile);
            Writer out = new StringWriter();
            template.process(parameters, out);
            return new Query(out);
        } catch (IOException | TemplateException e) {
            log.error("Failed to process FreeMarker template '{}' with parameters: {}",
                    queryfile, parameters, e);
            throw new QueryBuildException("Can't read query {} : " + queryfile, e);
        }
    }
    public static class QueryBuildException extends RuntimeException {
        public QueryBuildException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

