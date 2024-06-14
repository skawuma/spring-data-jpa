package com.skawuma;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skawuma.controller.ProductController;
import com.skawuma.entity.Product;
import com.skawuma.repository.ProductRepository;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc


class SpringDataJpaApplicationTests {
	@Autowired
	private ProductController productController;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductRepository productRepository;
	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(ProductController.class)
				.build();
	}

	@Test
	public void addProductTest() throws Exception {
		Product demoproduct = new Product(1,"demo",1000,"demoProduct","Sample product");
		when(productRepository.save(any())).thenReturn(demoproduct);
		mockMvc.perform(MockMvcRequestBuilders
				.post("/products")
				.content(covertObjectAsString(demoproduct))
				.contentType("application/json")
				.accept("application/json"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());




	}
	@Test
	public void getProductShouldReturnALlProductsTest() throws Exception {
		when(productRepository.findAll()).thenReturn(Arrays.asList(
				new Product(1,"demo",1000.0,"demoProduct","Sample product"),
	            new Product(2,"demo2",2000.0,"demoProduct2","Sample product2")));
		mockMvc.perform(MockMvcRequestBuilders
						.get("/products")
						.accept("application/json"))
				         .andDo(print())
				         .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());

	}
	@Test
	public void getProductByIdTest() throws Exception {
		when(productRepository.findById(2)).thenReturn(Optional.of(
				new Product(2,"test",165000.0,"DESC","type")));
mockMvc.perform(MockMvcRequestBuilders
						.get("/products/"+2)
						.accept("application/json"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));

	}

	@Test
	public void addUpdateTest() throws Exception {
		Product demoproduct = new Product(1,"IPHONE",165000.0,"mobile PHONE","Electronic");
		when(productRepository.findById(1)).thenReturn(Optional.of(demoproduct));
		when(productRepository.save(any())).thenReturn(new Product(1,"IPHONE 13",250000.0,"mobile PHONE","Electronic"));
		mockMvc.perform(MockMvcRequestBuilders
						.put("/products/{id}",1)
						.content(covertObjectAsString(demoproduct))
						.contentType("application/json")
						.accept("application/json"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("IPHONE 13"));
	}
	@Test
	public void deleteProductByIdTest() throws Exception {
		Mockito.doNothing().when(productRepository).deleteById(anyInt());
		when(productRepository.count()).thenReturn(Long.valueOf(100));

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/products/{id}",12))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value(100));

	}

	private String covertObjectAsString(Object object){
		try {
			return  new ObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}




	public void getSensitiveInfoEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("skawuma");// private -key
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(1);
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		System.out.println(encryptor.encrypt("root"));
	}

}
