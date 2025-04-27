package com.medium.eric.EricProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medium.eric.EricProject.dto.Product;
import com.medium.eric.EricProject.repository.ProductRepository;
import com.medium.eric.EricProject.service.ProductService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	  @Autowired
	    private ProductRepository productRepository;

	  @GetMapping("/allProducts")
	  public ResponseEntity<List<Product>> getAllProducts() {
	      List<Product> products = productService.getAllProducts();
	      if (products != null && !products.isEmpty()) {
	          return new ResponseEntity<>(products, HttpStatus.OK);
	      } else {
	          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
	  }

//	@GetMapping("/{id}")
//	public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
//		Optional<Product> product = productService.getProductById(id);
//		if (product.isPresent()) {
//			return new ResponseEntity<>(product.get(), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
	  @GetMapping("/getProductById/{id}")
	  public ResponseEntity<?> getProductById(@PathVariable Integer id) {
	      System.out.println("getProductById called with id: " + id); // Add logging
	      Optional<Product> product = productService.getProductById(id);
	      if (product.isPresent()) {
	          System.out.println("Product found: " + product.get()); // Add logging
	          return new ResponseEntity<>(product.get(), HttpStatus.OK);
	      } else {
	          System.out.println("Product not found"); // Add logging
	          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
	  }
	@PostMapping("/postProduct")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		if (product != null) {
			return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
//		  "productId" : 1,
//		    "productName":"Cetaaphil",
//		     "productCost": 1265,
//		     "isProductAvailable": true
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
		if (productDetails != null) {
			Product updatedProduct = productService.updateProduct(id, productDetails);
			if (updatedProduct != null) {
				return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@RolesAllowed("ADMIN")
	@DeleteMapping("deleteProduct/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
		Optional<Product> product = productService.getProductById(id);
		if (product.isPresent()) {
			productService.deleteProduct(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping("/delByAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delByAdmin() {
		String str= "got accessed by adm";
		return new ResponseEntity<>(str, HttpStatus.OK);
	}
	@GetMapping("/searchByproductCost")
	public ResponseEntity searchByproductCost (@RequestParam Product productCost) {
		Optional<Product> li = productRepository.findByProductCost(productCost.getProductCost());
		if(li.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatusCode.valueOf(200));
	}
	
}