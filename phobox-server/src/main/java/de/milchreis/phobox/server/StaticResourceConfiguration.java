package de.milchreis.phobox.server;

import de.milchreis.phobox.core.Phobox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.net.MalformedURLException;

@Slf4j
@Component
@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {
	
	private ResourceHandlerRegistry registry;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	this.registry = registry;

    	log.debug(getAsUrl(Phobox.getModel().getStoragePath()));

    	registry
	    	.addResourceHandler("/ext/**")
	    	.addResourceLocations(
	    			"/ext/",
	    			getAsUrl(Phobox.getModel().getStoragePath()));
    }
    
    public void update() {
    	addResourceHandlers(this.registry);
    }

    private String getAsUrl(String path) {
    	try {
    		return new File(Phobox.getModel().getStoragePath()).toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return path;
		}
	}
}
