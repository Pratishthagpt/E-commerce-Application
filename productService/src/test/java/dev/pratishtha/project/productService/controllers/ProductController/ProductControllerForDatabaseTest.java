package dev.pratishtha.project.productService.controllers.ProductController;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
//import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerForDatabaseTest {

    @Autowired
    private ProductControllerForDatabase productControllerForDatabase;

    @MockBean
    @Qualifier("databaseProductServiceImpl")
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
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 5),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 10)
        );

        when(productServiceMock.getAllProducts())
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products"))
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
//        String token = RandomStringUtils.randomAlphanumeric(30);
        String token  = "bbbfjdsbfsdhfoijopor3j2989hddjknsj93";
        GenericProductDTO expectedProduct = new GenericProductDTO();
        expectedProduct.setId("1");

        expectedProduct.setTitle("Remote Controlled Car");
        expectedProduct.setDescription("Remote Controlled Car for children of age 8-10 yrs.");
        expectedProduct.setCategory_name("Toys");
        expectedProduct.setImage("remote-car.jpg");
        expectedProduct.setPriceVal(2000.0);

        when(productServiceMock.getProductsById(token, "1"))
                .thenReturn(expectedProduct);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/1"))
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
//        String token = RandomStringUtils.randomAlphanumeric(30);

        String token  = "bbbfjdsbfsdhfoijopor3j2989hddjknsj93";
        when(productServiceMock.getProductsById(token, "1"))
                .thenThrow(new IdNotFoundException("Product with id - 1 not found."));

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with id - 1 not found."));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

//        Assert
        Assertions.assertTrue(responseString.contains("Product with id - 1 not found."));
    }

    @Test
    public void testGetProductsWithLimit() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 150),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 100),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0, 155),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0, 420)
        );

        List<GenericProductDTO> expectedProducts = Arrays.asList(allProducts.get(0), allProducts.get(1));

        when(productServiceMock.getProductsWithLimit(2))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/limit/2"))
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
    public void getAllProductsWithSortById() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 90),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 150)
        );

        List<GenericProductDTO> expectedProducts = allProducts;
        Collections.reverse(expectedProducts);

        when(productServiceMock.getAllProductsWithSortById("desc"))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/sort-by-id/desc"))
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
    public void testGetAllProductsWithSortByTitle() throws Exception {
//        Arrange
        List<GenericProductDTO> allProducts = Arrays.asList(
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 55),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 180)
        );

        List<GenericProductDTO> expectedProducts = allProducts;
        Collections.sort(expectedProducts, new Comparator<GenericProductDTO>() {
            @Override
            public int compare(GenericProductDTO a, GenericProductDTO b) {
                return b.getTitle().compareTo(a.getTitle());
            }
        });

        when(productServiceMock.getAllProductsWithSortByTitle("desc"))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/sort-by-title/desc"))
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
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 150),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 200)
        );
        List<String> expectedCategories = Arrays.asList(allProducts.get(0).getCategory_name(), allProducts.get(1).getCategory_name());

        when(productServiceMock.getAllCategories())
                .thenReturn(expectedCategories);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/categories"))
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
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 150),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 48),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0, 150),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0, 180)
        );

        List<GenericProductDTO> expectedProducts = Arrays.asList(allProducts.get(0), allProducts.get(2));

        when(productServiceMock.getAllProductsByCategory("Electronics"))
                .thenReturn(expectedProducts);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/category/Electronics"))
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
                new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 150),
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0, 48),
                new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0, 150),
                new GenericProductDTO("4", "Plazo Kurta Set", "BIBA Pink Plazo Kurta Set", "image4.jpg", "Clothing", 2500.0, 180)
        );

        when(productServiceMock.getAllProductsByCategory("Jewelery"))
                .thenReturn(new ArrayList<>());

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/db/products/category/Jewelery"))
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
        GenericProductDTO productToAdd = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 48);

//        used any(GenericProductDTO.class) in when clause to ensure that the mock setup is flexible and accepts any instance of GenericProductDTO passed to the createNewProduct method.
        when(productServiceMock.createNewProduct(any(GenericProductDTO.class)))
                .thenReturn(productToAdd);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/db/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToAdd)))
                .andExpect(status().isCreated());

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO responseProduct = objectMapper.readValue(responseString, GenericProductDTO.class);

//        Assert
        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(productToAdd.getId(), responseProduct.getId());
        Assertions.assertEquals(productToAdd.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(productToAdd.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(productToAdd.getImage(), responseProduct.getImage());
        Assertions.assertEquals(productToAdd.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(productToAdd.getPriceVal(), responseProduct.getPriceVal());

    }

//    Not testing for invalid id case bcoz we are not getting anything back from fakestore api and neither the products are actually getting updated,
//    It's just for hitting the api. So, we are getting null after hitting api and that will then leads to throw exception,
//    hence, we are just checking for whether we received 200 status code and an empty json string.

    @Test
    public void testUpdateProductById() throws Exception {
//        Arrange
        GenericProductDTO productToUpdate = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 180);

        when(productServiceMock.updateProductById(any(String.class), any(GenericProductDTO.class)))
                .thenReturn(productToUpdate);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/db/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk());

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

        GenericProductDTO responseProduct = objectMapper.readValue(responseString, GenericProductDTO.class);

//        Assert
        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(productToUpdate.getId(), responseProduct.getId());
        Assertions.assertEquals(productToUpdate.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(productToUpdate.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(productToUpdate.getImage(), responseProduct.getImage());
        Assertions.assertEquals(productToUpdate.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(productToUpdate.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateProductByIdGivesErrorForInvalidId() throws Exception {
//        Arrange
        GenericProductDTO productToUpdate = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 150);

        when(productServiceMock.updateProductById(any(String.class), any(GenericProductDTO.class)))
                .thenThrow(new IdNotFoundException("Product with id - 2 not found."));

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/db/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with id - 2 not found."));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

//        Assert
        Assertions.assertTrue(responseString.contains("Product with id - 2 not found."));
}

    @Test
    public void testUpdateSubProductById() throws Exception {
//        Arrange
        GenericProductDTO productToUpdate = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 88);
        productToUpdate.setTitle("One-Plus 10R");
        productToUpdate.setDescription("Android Phone");
        productToUpdate.setPriceVal(40000.0);

        when(productServiceMock.updateSubProductById(any(String.class), any(GenericProductDTO.class)))
                .thenReturn(productToUpdate);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/db/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk());

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

       GenericProductDTO responseProduct = objectMapper.readValue(responseString, GenericProductDTO.class);

//        Assert
        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(productToUpdate.getId(), responseProduct.getId());
        Assertions.assertEquals(productToUpdate.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(productToUpdate.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(productToUpdate.getImage(), responseProduct.getImage());
        Assertions.assertEquals(productToUpdate.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(productToUpdate.getPriceVal(), responseProduct.getPriceVal());

    }

    @Test
    public void testUpdateSubProductByIdGivesErrorForInvalidId() throws Exception {
//        Arrange
        GenericProductDTO productToUpdate = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 88);

        when(productServiceMock.updateSubProductById(any(String.class), any(GenericProductDTO.class)))
                .thenThrow(new IdNotFoundException("Product with id - 2 not found."));

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/db/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with id - 2 not found."));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

//        Assert
        Assertions.assertTrue(responseString.contains("Product with id - 2 not found."));
    }

    @Test
    public void testDeleteProductById() throws Exception {
//        Arrange
        GenericProductDTO productToDelete = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0, 99);

        when(productServiceMock.deleteProductById("1"))
                .thenReturn(productToDelete);

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/db/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

//        the actual response from your controller contains the JSON representation of the deleted product, not an empty string.
//        Assert
        Assertions.assertEquals(objectMapper.writeValueAsString(productToDelete), responseString);
    }

    @Test
    public void testDeleteProductByIdGivesErrorForInvalidId() throws Exception {
//        Arrange
        when(productServiceMock.deleteProductById("2"))
                .thenThrow(new IdNotFoundException("Product Id not found."));

//        Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/db/products/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        String responseString = resultActions.andReturn().getResponse().getContentAsString();

//        the actual response from your controller contains the JSON representation of the deleted product, not an empty string.
//        Assert
        Assertions.assertTrue(responseString.contains("Product Id not found."));
    }

}