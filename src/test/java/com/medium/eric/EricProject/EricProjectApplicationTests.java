package com.medium.eric.EricProject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medium.eric.EricProject.dto.Product;
import com.medium.eric.EricProject.dto.Role;
import com.medium.eric.EricProject.service.ProductService;


@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class EricProjectApplicationTests {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private ProductService productService;

	String signupEmail = "testsignup@example.com";
	String signupPassword = "testpassword123";
	String fullName = "Test User";
	
	private String adminToken;

	@Test
	void contextLoads() {
	}

	// open api
	@Test
	@Order(2)
	public void workinngWel() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/users/hi")).andExpect(MockMvcResultMatchers.status().is(200));
	}

	// all working
	private String getJSONCreds(String username, String password) throws Exception {
		Map<String, String> creds = new HashMap<>();
		creds.put("email", username);
		creds.put("password", password);
		return objectMapper.writeValueAsString(creds);
	}

	private String getJSONSignupCreds(String email, String password, String fullName) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		map.put("fullName", fullName);
		return objectMapper.writeValueAsString(map);
	}

	private String getJSONLoginCreds(String email, String password) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		return objectMapper.writeValueAsString(map);
	}

	@Test
	@Order(3)
	public void signupAndLoginSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
				.content(getJSONSignupCreds(signupEmail, signupPassword, fullName))).andExpect(status().isOk());

		// 2. Login
		MvcResult loginResult = mvc
				.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content(getJSONLoginCreds(signupEmail, signupPassword)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.expiresIn").exists()).andReturn();
	}

	@Test
	@Order(4)
	public void loginWithInvalidPassword() throws Exception {
		// 1. Signup
		String signupEmail = "testsignup2@example.com";
		String signupPassword = "testpassword123";
		String fullName = "Test User2";

		mvc.perform(MockMvcRequestBuilders.post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
				.content(getJSONSignupCreds(signupEmail, signupPassword, fullName))).andExpect(status().isOk());

		mvc.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(getJSONLoginCreds(signupEmail, "invalidPassword"))).andExpect(status().isUnauthorized());
	}

	@Test
	@Order(5)
	public void loginWithNonExistantUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(getJSONLoginCreds("nonexistent@example.com", "password123")))
				.andExpect(status().isUnauthorized());
	}

	// all ok
//	    
//	    @Test
//		@Order(6)
//	    public void sellerAddNewProductWithAuth() throws Exception {
//	        // 1. Signup
//	        String signupEmail = "sellerProductTest@example.com";
//	        String signupPassword = "testpassword123";
//	        String fullName = "Seller Product Test User";
//
//	        mvc.perform(MockMvcRequestBuilders.post("/auth/signup")
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getJSONSignupCreds(signupEmail, signupPassword, fullName)))
//	                .andExpect(status().isOk());
//
//	        // 2. Login to get JWT token
//	        MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getJSONLoginCreds(signupEmail, signupPassword)))
//	        		  .andExpect(status().isOk())
//	                .andReturn();
//
//	        String responseBody = loginResult.getResponse().getContentAsString();
//	        String token = objectMapper.readTree(responseBody).get("token").asText();
//
//	        // 3. Add new product with JWT token
//	        mvc.perform(MockMvcRequestBuilders.post("/products") // Assuming this is your product creation endpoint
//	                        .header("Authorization", "Bearer " + token)
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getProduct(0, "iPhone 11", 49000.0, 2, "Electronics").toString()))
//	                .andExpect(status().isCreated()) // Or whatever status your API returns
//	                .andExpect(jsonPath("$.name").value("iPhone 11")); // Example assertion
//	    }
//	    private JSONObject getProduct(int id, String name, double price, int categoryId, String categoryName) {
//	        Map<String, Object> categoryMap = new HashMap<>();
//	        categoryMap.put("id", categoryId);
//	        categoryMap.put("name", categoryName);
//
//	        Map<String, Object> productMap = new HashMap<>();
//	        productMap.put("id", id);
//	        productMap.put("name", name);
//	        productMap.put("price", price);
//	        productMap.put("category", categoryMap);
//
//	        return new JSONObject(productMap);
//	    }
	private MockHttpServletResponse signupHelper(String email, String password, String fullName) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		map.put("fullName", fullName);
		String requestJson = objectMapper.writeValueAsString(map);

		return mvc.perform(MockMvcRequestBuilders.post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)).andReturn().getResponse();
	}

	private MockHttpServletResponse loginHelper(String email, String password) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		String requestJson = objectMapper.writeValueAsString(map);

		return mvc.perform(
				MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andReturn().getResponse();
	}

	public JSONObject getProduct(Integer productId, String productName, Integer productCost,
			boolean isProductAvailable) {
		Map<String, Object> productMap = new HashMap<>();
		productMap.put("productId", productId);
		productMap.put("productName", productName);
		productMap.put("productCost", productCost);
		productMap.put("isProductAvailable", isProductAvailable);

		return new JSONObject(productMap);
	}

	public JSONObject getProduct(Integer productId, String productName, Integer productCost, boolean isProductAvailable,
			Role role) {
		Map<String, Object> productMap = new HashMap<>();
		productMap.put("productId", productId);
		productMap.put("productName", productName);
		productMap.put("productCost", productCost);
		productMap.put("isProductAvailable", isProductAvailable);
		productMap.put("role", role);

		return new JSONObject(productMap);
	}

	@BeforeEach
	void setUp() {
		String jwtToken = "Bearer sample.jwt.token"; // Replace with a real JWT if necessary
	}

	@Test // 6
	@Order(6)
	void testCreateProduct_Success() throws Exception {
		Product product = new Product("Shanaya", 1, true);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

//	        ResultActions response = MockMvc.perform(post("/postProduct")
//	                .header(HttpHeaders.AUTHORIZATION, jwtToken)
//	                .contentType(MediaType.APPLICATION_JSON)
//	                .content(objectMapper.writeValueAsString(product)));
		String signupEmail = "productTest@example.com";
		String signupPassword = "testpassword123";
		String fullName = "Product Test User";

		signupHelper(signupEmail, signupPassword, fullName);

		// 2. Login to get JWT token
		MockHttpServletResponse loginResponse = loginHelper(signupEmail, signupPassword);
		String responseBody = loginResponse.getContentAsString();
		String token = objectMapper.readTree(responseBody).get("token").asText();

		mvc.perform(MockMvcRequestBuilders.post("/products/postProduct").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(getProduct(1, "Shanaya", 99, true).toString()))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.productName").value("Shanaya")); // Adjust
																										// assertion to
																										// match the
																										// field name

	}

	@Test // 7
	public void createProductWithoutAuth() throws Exception {
		// 3. Create product without JWT token
		mvc.perform(MockMvcRequestBuilders.post("/products/postProduct").contentType(MediaType.APPLICATION_JSON)
				.content(getProduct(0, "ShanayaShanaya", 99, true).toString())).andExpect(status().isUnauthorized());
	}

//	    @Test  //8
//	    public void testCreateProductAndGetById() throws Exception {
//	        // 1. Signup
//	        String signupEmail = "productTest@example.com";
//	        String signupPassword = "testpassword123";
//	        String fullName = "Product Test User";
//
//	        signupHelper(signupEmail, signupPassword, fullName);
//
//	        // 2. Login to get JWT token
//	        MockHttpServletResponse loginResponse = loginHelper(signupEmail, signupPassword);
//	        String responseBody = loginResponse.getContentAsString();
//	        String token = objectMapper.readTree(responseBody).get("token").asText();
//
//	        // 3. Create product
//	        MvcResult postResult = mvc.perform(MockMvcRequestBuilders.post("/products/postProduct")
//	                        .header("Authorization", "Bearer " + token)
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getProduct(1, "Shanaya", 99, true).toString()))
//	                .andExpect(status().isCreated())
//	                .andExpect(jsonPath("$.productName").value("Shanaya"))
//	                .andExpect(status().isCreated())
//	                .andReturn();
//	        
//	        //Extract the product ID if your post response returns it. If not, you may need to find a way to retrieve the created ID.
//	        //Integer productId = objectMapper.readTree(postResult.getResponse().getContentAsString()).get("productId").asInt();
//	        String responseBody2 = postResult.getResponse().getContentAsString();
//	        Integer  productId = objectMapper.readTree(responseBody2).get("productId").asInt(); // Assuming the response has an "productId" field.
//	   
//	        // 4. Get product by ID
//	        mvc.perform(MockMvcRequestBuilders.get("/products/" + productId)
//	                        .header("Authorization", "Bearer " + token))
//	                .andExpect(status().isOk())
//	                .andExpect(jsonPath("$.productName").value("Shanaya"));
//	    }
	@Test
	public void deleteProductWithoutAuth() throws Exception {
		// 3. Attempt to delete a product without auth(should fail)
		mvc.perform(MockMvcRequestBuilders.delete("/products/deleteProduct/1")).andExpect(status().isUnauthorized());
	}

	private MockHttpServletResponse loginAdminHelper(String email, String password) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		String requestJson = objectMapper.writeValueAsString(map);

		return mvc.perform(
				MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andReturn().getResponse();
	}

	private MockHttpServletResponse signupAdminHelper(String email, String password, String fullName) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("password", password);
		map.put("fullName", fullName);
		map.put("role", "ADMIN");
		String requestJson = objectMapper.writeValueAsString(map);

		return mvc.perform(MockMvcRequestBuilders.post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)).andReturn().getResponse();
	}

//	    @Test
//	    public void deleteProductAsAdmin() throws Exception {
//	        // 1. Signup admin user
//	        String adminEmail = "admin@example.com";
//	        String adminPassword = "adminpassword";
//	        String adminFullName = "Admin User";
//
//	        signupAdminHelper(adminEmail, adminPassword, adminFullName);
//
//	        // 2. Login as admin to get JWT token
//	        MockHttpServletResponse loginResponse = loginAdminHelper(adminEmail, adminPassword);
//	        String responseBody = loginResponse.getContentAsString();
//	        String token = objectMapper.readTree(responseBody).get("token").asText();
//
//	        // 3. Create a product (for deletion)
//	        MvcResult postResult = mvc.perform(MockMvcRequestBuilders.post("/products/postProduct")
//	                        .header("Authorization", "Bearer " + token)
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getProduct(1, "Delete Test Product", 99, true).toString()))
//	                .andExpect(status().isCreated())
//	                .andReturn();
//
//	        // Extract the product ID
//	        String responseBody2 = postResult.getResponse().getContentAsString();
//	        Integer productId = objectMapper.readTree(responseBody2).get("productId").asInt();
//
//	        // 4. Delete the product as admin
//	        mvc.perform(MockMvcRequestBuilders.delete("/products/deleteProduct/" + productId)
//	                        .header("Authorization", "Bearer " + token))
//	                .andExpect(status().isOk());
//
//	        // 5. Attempt to get the deleted product (should fail)
//	        mvc.perform(MockMvcRequestBuilders.get("/products/" + productId)
//	                        .header("Authorization", "Bearer " + token))
//	                .andExpect(status().isBadRequest());
//	    }

//	    @Test
//	    @WithMockUser(username = "user1", roles = {"SUPER"})  // Simulate a non-admin user
//	    void testDeleteProductForbiddenForAdmin() throws Exception {
//	    	mvc.perform(delete("/delByAdmin")) 
//	                .andExpect(status().isOk()); 
//	    }

	/*
	 * @Test
	 * 
	 * @WithMockUser( roles = {"ADMIN"}) // Simulate a non-admin user void
	 * testDeleteProductForbiddenForAdminWithoutAuth() throws Exception { Product
	 * product = new Product(4,"Nivia",10000,true);
	 * when(productService.createProduct(any(Product.class))).thenReturn(product);
	 * 
	 * // ResultActions response = MockMvc.perform(post("/postProduct") //
	 * .header(HttpHeaders.AUTHORIZATION, jwtToken) //
	 * .contentType(MediaType.APPLICATION_JSON) //
	 * .content(objectMapper.writeValueAsString(product))); String signupEmail =
	 * "sr@example.com"; String signupPassword = "pass"; String fullName = "User2";
	 * 
	 * signupHelper(signupEmail, signupPassword, fullName);
	 * 
	 * // 2. Login to get JWT token MockHttpServletResponse loginResponse =
	 * loginHelper(signupEmail, signupPassword); String responseBody =
	 * loginResponse.getContentAsString(); String token =
	 * objectMapper.readTree(responseBody).get("token").asText();
	 * 
	 * mvc.perform(MockMvcRequestBuilders.post("/products/postProduct")
	 * .header("Authorization", "Bearer " + token)
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(getProduct(4,"Nivia",10000,true).toString()))
	 * .andExpect(status().isCreated()) .andExpect(jsonPath("$.productId").value(4))
	 * .andExpect(jsonPath("$.productName").value("Nivia"));
	 * 
	 * 
	 * mvc.perform(delete("/deleteProduct/4")) .andExpect(status().isOk());
	 */

	@Test
	public void testPostProduct_Success() throws Exception {
		String signupEmail = "test@example.com";
		String signupPassword = "password";
		String fullName = "Test User";

		signupHelper(signupEmail, signupPassword, fullName);
		Product product = new Product(9, "Laptop", 1200, true, Role.ADMIN);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

		MockHttpServletResponse loginResponse = loginHelper(signupEmail, signupPassword);
		String responseBody = loginResponse.getContentAsString();
		String token = objectMapper.readTree(responseBody).get("token").asText();

		// Product product = new Product(9, "Laptop", 1200, true,Role.ADMIN);

		// Product product = new Product(9, "Laptop", 1200, true, Role.ADMIN);

		MvcResult postResult = mvc
				.perform(MockMvcRequestBuilders.post("/products/postProduct").header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.productName").value("Laptop"))
				.andExpect(jsonPath("$.productCost").value(1200)).andExpect(jsonPath("$.role").value("ADMIN"))
				.andReturn();
		String postResponseBody = postResult.getResponse().getContentAsString();
		Integer productId = objectMapper.readTree(postResponseBody).get("productId").asInt();

		// Retrieve the product by ID

//        mvc.perform(MockMvcRequestBuilders.post("/products/postProduct")
//                .header("Authorization", "Bearer " + token)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(getProduct(1, "Shanaya", 99, true).toString()))
//        .andExpect(status().isCreated())
//        .andExpect(jsonPath("$.productName").value("Shanaya")); 

	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }) // Simulating an admin user
	void testDelByAdmin_WithAdminRole_ShouldPass() throws Exception {
		mvc.perform(delete("/products/delByAdmin")).andExpect(status().isOk())
				.andExpect(content().string("got accessed by adm")); // Optional: Verify response body
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" }) // Simulate a non-admin user
	void testDeleteProductForbiddenForNonAdmin() throws Exception {
		mvc.perform(delete("/deleteProduct/1")).andExpect(status().isForbidden());
	}
	
//	@Test
//	public void testAddAndFetchProduct() throws Exception {
//	    // Setup user credentials
//	    String signupEmail = "tcs@example.com";
//	    String signupPassword = "tcs";
//	    String fullName = "Tcs";
//
//	    // Register the user
//	    signupHelper(signupEmail, signupPassword, fullName);
//
//	    // Create Product using parameterized constructor
//	    Product product = new Product("refriz", 999, true);
//
//	    // Mock productService to return this product when createProduct is called
//	    when(productService.createProduct(any(Product.class))).thenReturn(product);
//
//	    // Login and extract token
//	    MockHttpServletResponse loginResponse = loginHelper(signupEmail, signupPassword);
//	    String responseBody = loginResponse.getContentAsString();
//	    String token = objectMapper.readTree(responseBody).get("token").asText();
//
//	    // Convert product to JSON
//	    String productJson = objectMapper.writeValueAsString(product);
//
//	    // Post product
//	    MvcResult postResult = mvc.perform(post("/products/postProduct")
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .content(productJson)
//	            .header("Authorization", "Bearer " + token))
//	            .andExpect(status().isCreated())
//	            .andReturn();
//
//	    // Simulate fetching the same product by ID and validating fields
//	    mvc.perform(get("/products/getProductById" + product.getProductId())
//	            .header("Authorization", "Bearer " + token))
//	            .andExpect(status().isOk())
//	            .andExpect(jsonPath("$.productId").isNotEmpty())
//	            .andExpect(jsonPath("$.productName").value("refriz"))
//	            .andExpect(jsonPath("$.productCost").value(999))
//	            .andExpect(jsonPath("$.isProductAvailable").value(true));
//	        
//	}
	private String getProductJson(Integer productId, String name, int price, boolean available) throws JsonProcessingException {
		 Map<String, Object> product = new HashMap<>();
		   product.put("productId", productId);
		    product.put("productName", name);
		    product.put("productPrice", price);
		    product.put("available", available);
	    return objectMapper.writeValueAsString(product);
	}
//	@Test
//	@Order(12)
//	void createMultipleProducts() throws Exception {
//	    String signupEmail = "bulkadd@example.com";
//	    String password = "pass123";
//	    String fullName = "Bulk Add Tester";
//
//	    signupHelper(signupEmail, password, fullName);
//	    String token = objectMapper.readTree(loginHelper(signupEmail, password).getContentAsString()).get("token").asText();
//
//	    Product product = new Product(19, "SAP", 9000, true, Role.ADMIN);
//		when(productService.createProduct(any(Product.class))).thenReturn(product);
//	    
//		MvcResult result1 = mvc.perform(post("/products/postProduct")
//		        .header("Authorization", "Bearer " + token)
//		        .contentType(MediaType.APPLICATION_JSON)
//		        .content(objectMapper.writeValueAsString(product)))
//		    .andExpect(status().isCreated())
//		    .andReturn();
//		
//		String responseContent = result1.getResponse().getContentAsString();
//		System.out.println("Product creation response: " + responseContent);
//
//		JsonNode responseJson = objectMapper.readTree(responseContent);
//		Long productId1 = responseJson.get("productId").asLong(); // Change key based on actual JSON
//
//
//	    mvc.perform(get("/products/getProductById/" + product.getProductId() )
//	            .header("Authorization", "Bearer " + token))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.productId").isNotEmpty());
//	}


}