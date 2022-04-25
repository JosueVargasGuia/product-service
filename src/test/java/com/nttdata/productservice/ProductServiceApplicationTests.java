package com.nttdata.productservice;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;

@SpringBootTest
class ProductServiceApplicationTests {

	@Test
	void contextLoads() {
		Flux<String> flux = Flux.just(
				"website_0:bezkoder.com", 
				"meta_0:Java Tutorial",
				"meta_1:Project Reactor");
		List<String> list1 = flux.collectList().block();
		for (String string : list1) {
			System.out.println(string);
		}
	}

}
