package de.milchreis.phobox.db;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;

@Component
public class DBConnectionConfiguration {
	
	@Bean
	@Primary
	public DataSource getDatasource(){
	    return DataSourceBuilder
	        .create()
	        .username("")
	        .password("")
	        .url("jdbc:h2:" + Phobox.getModel().getStoragePath()+"/phobox/phobox;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;IGNORECASE=TRUE;AUTO_SERVER=TRUE")
	        .driverClassName("org.h2.Driver")
	        .build();
	}
	
}
