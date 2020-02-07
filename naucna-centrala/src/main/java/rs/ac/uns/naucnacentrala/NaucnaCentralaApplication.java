package rs.ac.uns.naucnacentrala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class NaucnaCentralaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaucnaCentralaApplication.class, args);
	}

}
