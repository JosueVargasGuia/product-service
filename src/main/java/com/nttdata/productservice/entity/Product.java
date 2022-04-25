package com.nttdata.productservice.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private Long idProducto;
	private ProductId productId;
	private String descriptionProducto;
	private TypeProduct typeProduct;
	private Long idConfiguration;
	
	@Override
	public String toString() {
		return "Product [idProducto=" + idProducto + ", productId=" + productId + ", descriptionProducto="
				+ descriptionProducto + ", typeProduct=" + typeProduct + ", idConfiguration=" + idConfiguration + "]";
	}
	
	//comentario
}
