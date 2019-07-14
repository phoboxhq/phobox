package de.milchreis.phobox.db;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;

@Slf4j
@Component
public class DBConnectionConfiguration {
	
	@Bean
	@Primary
	public DataSource getDatasource(){
		String storagePath = Phobox.getModel().getStoragePath();

		if(storagePath == null) {
			log.warn("No storage path found. Using in-memory database");

			return DataSourceBuilder
				.create()
				.username("")
				.password("")
				.url("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1")
				.driverClassName("org.h2.Driver")
				.build();

		} else {

			return DataSourceBuilder
				.create()
				.username("")
				.password("")
				.url("jdbc:h2:" + storagePath +"/phobox/phobox;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;IGNORECASE=TRUE;AUTO_SERVER=TRUE")
				.driverClassName("org.h2.Driver")
				.build();
		}

	}
	
}
