package de.milchreis.phobox.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.milchreis.phobox.core.Phobox;

@Component
@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {
	
	private ResourceHandlerRegistry registry;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	this.registry = registry;
    	
    	registry
	    	.addResourceHandler("/ext/**")
	    	.addResourceLocations(
	    			"/ext/", 
	    			"file://" + Phobox.getModel().getStoragePath()+"/");
    }
    
    public void update() {
    	addResourceHandlers(this.registry);
    }
	
}
