package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.validation.ValidationFeature;

/**
 * ApplicationConfig class for configuring JAX-RS application.
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);

        // Register Jersey ValidationFeature for Bean Validation
        resources.add(ValidationFeature.class);

        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.bookstore.exception.ValidationExceptionMapper.class);
        resources.add(com.bookstore.resource.AuthorResource.class);
        resources.add(com.bookstore.resource.BookResource.class);
        resources.add(com.bookstore.resource.CartResource.class);
        resources.add(com.bookstore.resource.CustomerResource.class);
        resources.add(com.bookstore.resource.OrderResource.class);

    }
}
