package com.nttdata.productservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.productservice.FeignClient.ConfigurationFeingClient;
import com.nttdata.productservice.entity.Product;
import com.nttdata.productservice.entity.ProductId;
import com.nttdata.productservice.entity.TypeProduct;
import com.nttdata.productservice.model.Configuration;
import com.nttdata.productservice.model.ProductConfiguracion;
import com.nttdata.productservice.service.ProductService;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableFeignClients
@Log4j2
public class ProductServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Autowired
	ProductService productService;

	@Autowired
	ConfigurationFeingClient configurationFeingClient;


	/*
	 *Callback ,Logica que nos registra los productos predeteminados para no cargarlos manualmente.
	 */
	@Override
	public void run(String... args) throws Exception {
		Flux.fromIterable(filldata()).flatMap(o -> {

			Flux.fromIterable(configurationFeingClient.findAll())
					.filter(config -> config.getIdConfiguration() == o.getConfiguration().getIdConfiguration())
					.collect(Collectors.counting()).map(x -> {
						Configuration tmp = null;
						if (x == 0) {
							log.info("Save:" + o.getConfiguration().toString());
							productService.save(o.getProduct()).subscribe(e -> log.info("Product:" + e.toString()));
							tmp = configurationFeingClient.save(o.getConfiguration());
						} else {							
							log.info("update:" + o.getConfiguration().toString());
							productService.update(o.getProduct()).subscribe(e -> log.info("Product:" + e.toString()));
							tmp = configurationFeingClient.update(o.getConfiguration());
						}
						if (tmp != null) {
							return tmp;
						} else {
							return Mono
									.error(new InterruptedException("Servicio de configuration-service no responde"));
						}
					}).subscribe(e -> log.info("Configuration:" + e.toString()));
			return Mono.just(o);
		}).subscribe(e -> log.info("List:" + e.toString()));
		 

	}

	List<ProductConfiguracion> filldata() {

		List<ProductConfiguracion> l = new ArrayList<ProductConfiguracion>();
		ProductConfiguracion savings = new ProductConfiguracion();
		savings.setProduct(new Product(Long.valueOf(1), "Ahorro", TypeProduct.pasivos, Long.valueOf(1),
				ProductId.Savings, null, null));
		savings.setConfiguration(new Configuration(Long.valueOf(1), null, 5, null, null, null, null, null, null, null));

		ProductConfiguracion currentAccount = new ProductConfiguracion();
		currentAccount.setProduct(new Product(Long.valueOf(2), "Cuenta corriente", TypeProduct.pasivos, Long.valueOf(2),
				ProductId.CurrentAccount, null, null));
		currentAccount.setConfiguration(
				new Configuration(Long.valueOf(2), 10.00, null, null, null, null, null, null, null, null));

		ProductConfiguracion fixedTerm = new ProductConfiguracion();
		fixedTerm.setProduct(new Product(Long.valueOf(3), "Plazo fijo", TypeProduct.pasivos, Long.valueOf(3),
				ProductId.FixedTerm, null, null));
		fixedTerm.setConfiguration(
				new Configuration(Long.valueOf(3), null, Integer.valueOf(1), "12", null, null, null, null, null, null));

		ProductConfiguracion personnel = new ProductConfiguracion();
		personnel.setProduct(new Product(Long.valueOf(4), "Personal", TypeProduct.activos, Long.valueOf(4),
				ProductId.Personnel, null, null));
		personnel.setConfiguration(
				new Configuration(Long.valueOf(4), null, null, null, 1, null, null, null, null, null));

		ProductConfiguracion business = new ProductConfiguracion();
		business.setProduct(new Product(Long.valueOf(5), "Empresarial", TypeProduct.activos, Long.valueOf(5),
				ProductId.Business, null, null));
		business.setConfiguration(
				new Configuration(Long.valueOf(5), null, null, null, null, null, null, null, null, null));

		ProductConfiguracion businessCreditCard = new ProductConfiguracion();
		businessCreditCard.setProduct(new Product(Long.valueOf(6), "Tarjeta de Credito", TypeProduct.activos,
				Long.valueOf(6), ProductId.BusinessCreditCard, null, null));
		businessCreditCard.setConfiguration(
				new Configuration(Long.valueOf(6), null, null, null, null, null, null, null, null, null));

		ProductConfiguracion personalCreditCard = new ProductConfiguracion();
		personalCreditCard.setProduct(new Product(Long.valueOf(7), "Tarjeta de Credito personal", TypeProduct.activos,
				Long.valueOf(7), ProductId.PersonalCreditCard, null, null));
		personalCreditCard.setConfiguration(
				new Configuration(Long.valueOf(7), null, null, null, null, null, null, null, null, null));
		
		
		ProductConfiguracion vip = new ProductConfiguracion();
		vip.setProduct(new Product(Long.valueOf(8), "Vip", TypeProduct.activos,
				Long.valueOf(8), ProductId.Vip, null, null));
		vip.setConfiguration(
				new Configuration(Long.valueOf(8), null, null, null, null, null, null, Double.valueOf(20.00), null, null));
		
		ProductConfiguracion Pyme = new ProductConfiguracion();
		Pyme.setProduct(new Product(Long.valueOf(9), "Pyme", TypeProduct.activos,
				Long.valueOf(9), ProductId.Pyme, null, null));
		Pyme.setConfiguration(
				new Configuration(Long.valueOf(9), null, null, null, null, null, null, null, null, null));
		
		
		l.add(savings);
		l.add(currentAccount);
		l.add(fixedTerm);
		l.add(personnel);
		l.add(business);
		l.add(businessCreditCard);
		l.add(personalCreditCard);
		l.add(vip);
		l.add(Pyme);
		return l;
	}
}
