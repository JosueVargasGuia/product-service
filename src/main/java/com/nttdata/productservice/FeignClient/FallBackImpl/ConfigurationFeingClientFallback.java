package com.nttdata.productservice.FeignClient.FallBackImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.productservice.FeignClient.ConfigurationFeingClient;
import com.nttdata.productservice.model.Configuration;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ConfigurationFeingClientFallback implements ConfigurationFeingClient {
	@Value("${api.configuration-service.uri}")
	String configService;

	public List<Configuration> findAll() {
		log.info("ConfigurationFeingClientFallback findAll[" + configService + "]");
		return new ArrayList<Configuration>();
	}

	public Configuration save(Configuration configuration) {
		log.info("ConfigurationFeingClientFallback save[" + configService + "]:" + configuration.toString());
		return null;
	}

	public Configuration update(Configuration configuration) {
		log.info("ConfigurationFeingClientFallback update[" + configService + "]:" + configuration.toString());
		return null;
	}

}
