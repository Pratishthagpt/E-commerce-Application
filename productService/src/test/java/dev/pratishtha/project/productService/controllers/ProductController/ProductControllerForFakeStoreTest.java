package dev.pratishtha.project.productService.controllers.ProductController;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerForFakeStoreTest {

    @Autowired
    private ProductControllerForFakeStore productControllerForFakeStore;

    @MockBean
    @Qualifier("fakeStoreProductServiceImpl")
    private ProductService productServiceMock;

    @Autowired
    private MockMvc mockMvc;

//    to convert json body to java object
    @Autowired
    private ObjectMapper objectMapper;

//  happy response
    @Test
    public void testGetAllProducts() throws Exception {
//        Arrange
        List<GenericProductDTO> expectedProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0)
        );

        when(productServiceMock.getAllProducts())
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedProducts.size()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO[] responseProducts = objectMapper.readValue(responseString, GenericProductDTO[].class);

//        Assert
        Assertions.assertNotNull(responseProducts);
        Assertions.assertEquals(expectedProducts.size(), responseProducts.length);

        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts[0].getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts[0].getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts[0].getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts[0].getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts[0].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts[0].getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts[1].getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts[1].getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts[1].getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts[1].getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts[1].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts[1].getPriceVal());
    }

    @Test
    public void testGetProductByIdGivesCorrectResponse() throws Exception {
//        Arrange
        GenericProductDTO expectedProduct = new GenericProductDTO();
        expectedProduct.setId("1");
        expectedProduct.setTitle("Remote Controlled Car");
        expectedProduct.setDescription("Remote Controlled Car for children of age 8-10 yrs.");
        expectedProduct.setCategory_name("Toys");
        expectedProduct.setImage("remote-car.jpg");
        expectedProduct.setPriceVal(2000.0);

        when(productServiceMock.getProductsById("1"))
                .thenReturn(expectedProduct);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO responseProduct = objectMapper.readValue(responseString, GenericProductDTO.class);

//        Assert
        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(expectedProduct.getId(), responseProduct.getId());
        Assertions.assertEquals(expectedProduct.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(expectedProduct.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(expectedProduct.getImage(), responseProduct.getImage());
        Assertions.assertEquals(expectedProduct.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(expectedProduct.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testGetProductByIdGivesNullResponse() throws Exception {
//        Arrange
        when(productServiceMock.getProductsById("1"))
                .thenReturn(null);

//        Act
        GenericProductDTO responseProduct = productControllerForFakeStore.getProductById("1");

//        Assert
        Assertions.assertNull(responseProduct);
    }

    @Test
    public void testGetProductsWithLimit() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0)
        );

        List<GenericProductDTO> expectedProducts = Arrays.asList(allProducts.get(0), allProducts.get(1));

        when(productServiceMock.getProductsWithLimit(2))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/limit/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedProducts.size()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO[] responseProducts = objectMapper.readValue(responseString, GenericProductDTO[].class);

//        Assert
        Assertions.assertNotNull(responseProducts);
        Assertions.assertEquals(expectedProducts.size(), responseProducts.length);

        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts[0].getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts[0].getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts[0].getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts[0].getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts[0].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts[0].getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts[1].getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts[1].getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts[1].getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts[1].getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts[1].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts[1].getPriceVal());
    }

    @Test
    public void testGetAllProductsWithSort() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0)
        );

        List<GenericProductDTO> expectedProducts = allProducts;
        Collections.reverse(expectedProducts);

        when(productServiceMock.getAllProductsWithSortById("desc"))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/sort/desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedProducts.size()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO[] responseProducts = objectMapper.readValue(responseString, GenericProductDTO[].class);

//        Assert
        Assertions.assertNotNull(responseProducts);
        Assertions.assertEquals(expectedProducts.size(), responseProducts.length);

        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts[0].getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts[0].getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts[0].getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts[0].getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts[0].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts[0].getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts[1].getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts[1].getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts[1].getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts[1].getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts[1].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts[1].getPriceVal());
    }

    @Test
    public void testGetAllCategories() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0)
        );
        List<String> expectedCategories = Arrays.asList(allProducts.get(0).getCategory_name(), allProducts.get(1).getCategory_name());

        when(productServiceMock.getAllCategories())
                .thenReturn(expectedCategories);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedCategories.size()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        String[] responseCategories = objectMapper.readValue(responseString, String[].class);

//        Assert
        Assertions.assertNotNull(responseCategories);
        Assertions.assertEquals(expectedCategories.size(), responseCategories.length);
        Assertions.assertEquals(expectedCategories.get(0), responseCategories[0]);
        Assertions.assertEquals(expectedCategories.get(1), responseCategories[1]);
    }

    @Test
    public void testGetAllProductsByCategoryGivesCorrectResponse() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0)
        );

        List<GenericProductDTO> expectedProducts = Arrays.asList(allProducts.get(0), allProducts.get(2));

        when(productServiceMock.getAllProductsByCategory("Electronics"))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedProducts.size()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO[] responseProducts = objectMapper.readValue(responseString, GenericProductDTO[].class);

//        Assert
        Assertions.assertNotNull(responseProducts);
        Assertions.assertEquals(expectedProducts.size(), responseProducts.length);

        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts[0].getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts[0].getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts[0].getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts[0].getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts[0].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts[0].getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts[1].getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts[1].getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts[1].getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts[1].getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts[1].getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts[1].getPriceVal());
    }


    @Test
    public void testGetAllProductsByCategoryGivesEmptyArray() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0)
        );

        when(productServiceMock.getAllProductsByCategory("Jewelery"))
                .thenReturn(new ArrayList<>());

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fakestore/products/category/Jewelery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO[] responseProducts = objectMapper.readValue(responseString, GenericProductDTO[].class);

//        Assert
        Assertions.assertNotNull(responseProducts);
        Assertions.assertEquals(0, responseProducts.length);
    }

    @Test
    public void testAddNewProduct() throws Exception {
//        Arrange
        GenericProductDTO productToAdd = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0);

        when(productServiceMock.createNewProduct(productToAdd))
                .thenReturn(productToAdd);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/fakestore/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToAdd)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(productToAdd.getId()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO responseProduct = objectMapper.readValue(responseString, GenericProductDTO.class);

//        Assert
        Assertions.assertNull(responseProduct);
        Assertions.assertEquals(productToAdd.getId(), responseProduct.getId());
        Assertions.assertEquals(productToAdd.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(productToAdd.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(productToAdd.getImage(), responseProduct.getImage());
        Assertions.assertEquals(productToAdd.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(productToAdd.getPriceVal(), responseProduct.getPriceVal());
    }

}