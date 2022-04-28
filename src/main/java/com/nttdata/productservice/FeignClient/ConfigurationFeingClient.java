package com.nttdata.productservice.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.productservice.FeignClient.FallBackImpl.ConfigurationFeingClientFallback;
import com.nttdata.productservice.model.Configuration;

@FeignClient(name = "${api.configuration-service.uri}", fallback = ConfigurationFeingClientFallback.class)
public interface ConfigurationFeingClient {
	@GetMapping
	List<Configuration> findAll();

	@PostMapping
	Configuration save(@RequestBody Configuration configuration);

	@PutMapping
	Configuration update(@RequestBody Configuration configuration);

}
