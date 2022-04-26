package com.nttdata.productservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.productservice.FeignClient.FallBackImpl.TableIdFeignClientFallBack;
 
@FeignClient(name = "${api.tableId-service.uri}" ,
//url = "TableId-service/tableid"
//,
fallback = TableIdFeignClientFallBack.class
)
public interface TableIdFeignClient {
	 
	@GetMapping("/generateKey/{nameTable}")
	public Long generateKey(@PathVariable(name = "nameTable") String nameTable);
}
