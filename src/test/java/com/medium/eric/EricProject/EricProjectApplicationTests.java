package com.medium.eric.EricProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class EricProjectApplicationTests {

	 @Autowired
	    private MockMvc mvc;

	    @Autowired
	    private ObjectMapper objectMapper;

	    String c_u = "jack", s_u = "apple", p = "pass_word";
	    private String adminToken;

	    @Test
	    void contextLoads() {
	    }
	    // open api
	    @Test
	    @Order(1)
	    public void workinngWel() throws Exception {
	        mvc.perform(MockMvcRequestBuilders.get("/users/hi")).andExpect(MockMvcResultMatchers.status().is(200));
	    }
	    private String getJSONCreds(String username, String password) throws Exception {
	       Map<String, String> creds = new HashMap<>();
	        creds.put("email", username);
	        creds.put("password", password);
	        return objectMapper.writeValueAsString(creds);
	    }
	    //below api is giving console error 
	    @Test
	    @Order(2)
	    public void consumerLoginWithBadCreds() throws Exception {
	        mvc.perform(post("/auth/login")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(getJSONCreds("bad_username", "password")))
	                .andExpect(status().is(401));
	    }
	    public MockHttpServletResponse loginHelper(String u, String p) throws Exception {
	        return mvc
	                .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(getJSONCreds(u, p)))
	                .andReturn().getResponse();
	    }
	    @Order(3)
	    public void consumerLoginWithValidCreds() throws Exception {
	        assertEquals(200, loginHelper(c_u, p).getStatus());
	        assertNotEquals("", loginHelper(c_u, p).getContentAsString());
	    }
	 
	    @Test
	    @Order(4)
	    public void testCreateProduct() throws Exception {
	        mvc.perform(post("/postProduct")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(getProductDetails("Laptop", 1500, true).toString()))
	                .andExpect(status().is(403));
	        //403: Forbidden User is authenticated but does not have permission When the user is logged in 
	        //but lacks access to a resource A normal user tries to access an admin-only API
	    }
	    private String obtainAccessToken(String username, String password) throws Exception {
	        MvcResult result = mvc.perform(post("/auth/login")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(getJSONCreds(username, password)))
	                .andExpect(status().isOk())
	                .andReturn();

	        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
	        return response.get("token");
	    }
    @Order(4)
    public void postProductWithValidToken() throws Exception {
        String token = obtainAccessToken(s_u, p);
	        mvc.perform(MockMvcRequestBuilders.post("/postProduct")
	                        .header("Authorization", "Bearer " + token)
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(getProductDetails("Laptop", 1500, true).toString()))
	                .andExpect(status().is(200));
	    }
	    private String getProductDetails(String productName, Integer productCost, boolean isProductAvailable) throws Exception {
	        Map<String, Object> map = new HashMap<>();
	        map.put("productName", productName);
	        map.put("productCost", productCost);
	        map.put("isProductAvailable", isProductAvailable);
	        return objectMapper.writeValueAsString(map); // Convert Map to JSON String
	    }
	    
//	    @Test
//	    @Order(4)
//	    public void postProductWithValidToken() throws Exception {
//	        String token = obtainAccessToken(c_u, p);
//
//	        mvc.perform(MockMvcRequestBuilders.post("/postProduct")
//	                        .header("Authorization", "Bearer " + token)
//	                        .contentType(MediaType.APPLICATION_JSON)
//	                        .content(getProductDetails("Laptop", 1500, true)))
//	                .andExpect(status().isCreated());
//	    }
	    @Order(5)
	    public void testDeleteProductAsAdmin() throws Exception {
	    	 String token = obtainAccessToken(c_u, p);
	        mvc.perform(delete("/product/1")
	        		  .header("Authorization", "Bearer " + token)
	                        .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk());
	    }
}
