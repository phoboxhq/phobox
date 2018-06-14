package de.milchreis.phobox.server;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.glassfish.jersey.servlet.ServletContainer;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.server.api.StorageService;
import de.milchreis.phobox.server.servlets.FileUploadServlet;
import de.milchreis.phobox.utils.MD5Helper;

public class PhoboxServer {
	private static Logger log = Logger.getLogger(PhoboxServer.class);
	
	public static final String[] ACCESS_ROLES = {"usage"};
	
	private Server jettyServer;
	
	@SuppressWarnings("serial")
	public void init(int port) {
		
		 // REST
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		restHandler.setContextPath("/");
        restHandler.setResourceBase("./");
        restHandler.setClassLoader(Thread.currentThread().getContextClassLoader());

        ServletHolder restServlet = restHandler.addServlet(ServletContainer.class, "/api/*");
        restServlet.setInitOrder(0);
        restServlet.setInitParameters(new HashMap<String, String>() {{
        	put("jersey.config.server.provider.packages", StorageService.class.getPackage().getName()+
        			";com.myorg.myproj.api;org.codehaus.jackson.jaxrs;com.fasterxml.jackson.jaxrs");
        	put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
        }});
        
		// FileUpload
        restHandler.addServlet(FileUploadServlet.class, "/api/upload");
        
        // Front-End resources
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("WebContent"));
		
        // Resource path for files
        ResourceHandler storageHandler = new ResourceHandler();
        storageHandler.setResourceBase(Phobox.getModel().getStoragePath());
        
//        // WebDAV
//        ServletHolder davServletHolder = restHandler.addServlet(WebDavServlet.class, "/webdav/*");
//        davServletHolder.setInitParameters(new HashMap<String, String>() {{
//        	put("rootPath", PhoboxModel.getInstance().getStoragePath()); 
//        	put("storeDebug", "0"); 
//        }});
        
        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.addHandler(resourceHandler);
        handlerCollection.addHandler(storageHandler);
        handlerCollection.addHandler(restHandler);       
        
        jettyServer = new Server(port);
        
        if(PreferencesManager.getUserMap().size() > 0) {
        	
			MappedLoginService loginService = new MappedLoginService() {

				@Override
				public UserIdentity login(String username, Object credentials) {
					return super.login(username, MD5Helper.getMD5(credentials.toString()));
				}
				
				@Override
				protected UserIdentity loadUser(String username) {
					return null;
				}

				@Override
				protected void loadUsers() throws IOException {
					for(Entry<String, String> entry : PreferencesManager.getUserMap().entrySet()) {
						putUser(entry.getKey(), Credential.getCredential(entry.getValue()), ACCESS_ROLES);
					}
				}
			};
        	
        	ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        	jettyServer.setHandler(security);
        	
        	Constraint constraint = new Constraint();
        	constraint.setName("auth");
        	constraint.setAuthenticate(true);
        	constraint.setRoles(ACCESS_ROLES);
        	
        	ConstraintMapping mapping = new ConstraintMapping();
        	mapping.setPathSpec("/*");
        	mapping.setConstraint(constraint);
        	
        	security.setConstraintMappings(Collections.singletonList(mapping));
        	security.setAuthenticator(new BasicAuthenticator());
        	security.setLoginService(loginService);
        	security.setHandler(handlerCollection);
        	
        } else {
        	
	        jettyServer.setHandler(handlerCollection);
        }
	}
	
	public void restart(int port) {
		try {
			stop();
			init(port);
			start();
		} catch (Exception e) {
			log.error("Error while restarting server", e);
		}
	}
	
	public void addMBean(Object instance) {
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		jettyServer.addEventListener(mbContainer);
		jettyServer.addBean(instance);
	}
	
	public void start() throws Exception {
		log.debug("Server will be started");
		jettyServer.start();
	}
	
	public void stop() {
		log.debug("Server will be stopped");
		try {
			jettyServer.stop();
			
		} catch (Exception e) {
			log.error("Error while stopping server", e);
		}
		jettyServer.destroy();
	}
}
