package com.nttdata.productservice.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.nttdata.productservice.FeignClient.TableIdFeignClient;
import com.nttdata.productservice.entity.Product;
import com.nttdata.productservice.entity.ProductId;
import com.nttdata.productservice.entity.TypeProduct;
import com.nttdata.productservice.model.ProductConfiguracion;
import com.nttdata.productservice.repository.ProductRepository;
import com.nttdata.productservice.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
	Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductRepository productRepository;
	@Autowired
	TableIdFeignClient tableIdFeignClient;
	/*
	 * @Autowired RestTemplate restTemplate;
	 * 
	 * @Value("${api.uri.tableId-service}") private String tableIdService;
	 */

	@Autowired
	RestTemplate restTemplate;
	@Value("${api.tableId-service.uri}")
	String tableIdService;

	@Override
	public Flux<Product> findAll() {
		return productRepository.findAll()
				.sort((prodA, prodB) -> prodA.getIdProducto().compareTo(prodB.getIdProducto()));
	}

	@Override
	public Mono<Product> findById(Long idProducto) {
		return productRepository.findById(idProducto)
				.switchIfEmpty(Mono.just(new Product(Long.valueOf(-1), null, null, null, null, null, null)));

	}

	@Override
	public Mono<Product> save(Product product) {
		Long key = generateKey(Product.class.getSimpleName());
		if (key >= 1) {
			product.setIdProducto(key);
			log.info("SAVE[product]:" + product.toString());
			product.setCreationDate(Calendar.getInstance().getTime());
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + Product.class.getSimpleName()));
		}
		return productRepository.insert(product);
	}

	@Override
	public Mono<Product> update(Product product) {
		/*
		 * Mono<Product> mono = productRepository.findById(product.getIdProducto())
		 * .flatMap(objproduct -> { return productRepository.save(product); }); return
		 * mono;
		 */
		return productRepository.save(product);
	}

	@Override
	public Mono<Void> delete(Long idProducto) {
		/*
		 * Mono<Void> mono = productRepository.findById(idProducto).flatMap(producto ->
		 * { return productRepository.delete(producto); }); mono.subscribe(p ->
		 * log.info(p.toString())); return productRepository.deleteById(idProducto);
		 */
		return productRepository.deleteById(idProducto);
	}

	Long idProducto = Long.valueOf(0);

	@Override
	public Mono<Void> fillData() {
		return productRepository.findAll().count().flatMap(x -> {
			log.info("Cantidad[X]:" + x);
			List<Product> listaProducts = new ArrayList<Product>();
			// new Product(Long.valueOf(1), "Ahorro", TypeProduct.pasivos, Long.valueOf(1)),
			// ProductId.Savings,null,null)
			listaProducts.add(new Product(Long.valueOf(1), "Ahorro", TypeProduct.pasivos, Long.valueOf(1),
					ProductId.Savings, null, null));
			listaProducts.add(new Product(Long.valueOf(2), "Cuenta corriente", TypeProduct.pasivos, Long.valueOf(2),
					ProductId.CurrentAccount, null, null));
			listaProducts.add(new Product(Long.valueOf(3), "Plazo fijo", TypeProduct.pasivos, Long.valueOf(3),
					ProductId.FixedTerm, null, null));
			listaProducts.add(new Product(Long.valueOf(4), "Personal", TypeProduct.activos, Long.valueOf(4),
					ProductId.Personnel, null, null));
			listaProducts.add(new Product(Long.valueOf(5), "Empresarial", TypeProduct.activos, Long.valueOf(5),
					ProductId.Business, null, null));
			listaProducts.add(new Product(Long.valueOf(6), "Tarjeta de Credito", TypeProduct.activos, Long.valueOf(6),
					ProductId.BusinessCreditCard, null, null));
			listaProducts.add(new Product(Long.valueOf(7), "Tarjeta de Credito personal", TypeProduct.activos,
					Long.valueOf(7), ProductId.PersonalCreditCard, null, null));
			log.info("Fill data succefull");
			idProducto = Long.valueOf(x);
			return Flux.fromIterable(listaProducts).flatMap(product -> {
				log.info("[product]:" + product);
				idProducto = idProducto + 1;
				product.setIdProducto(idProducto);
				return this.save(product);
			}).then();
		}).then();
	}

	@Override
	public Long generateKey(String nameTable) {
		log.info(tableIdService + "/generateKey/" + nameTable);
		/*
		 * ResponseEntity<Long> responseGet = restTemplate.exchange(tableIdService +
		 * "/generateKey/" + nameTable, HttpMethod.GET, null, new
		 * ParameterizedTypeReference<Long>() { }); if (responseGet.getStatusCode() ==
		 * HttpStatus.OK) { log.info("Body:" + responseGet.getBody()); return
		 * responseGet.getBody(); } else { return Long.valueOf(0); }
		 */
		return tableIdFeignClient.generateKey(nameTable);
	}
}
