package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.FakeStoreProductClient;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class FakeStoreProductServiceImplTest {

    @Autowired
    private FakeStoreProductServiceImpl fakeStoreProductService;

    @MockBean
    private FakeStoreProductClient fakeStoreProductClientMock;

    private GenericProductDTO genericProduct1;
    private FakeStoreProductDTO fakeStoreProduct1;
    private GenericProductDTO genericProduct2;
    private FakeStoreProductDTO fakeStoreProduct2;
    private GenericProductDTO genericProduct3;
    private FakeStoreProductDTO fakeStoreProduct3;

    @BeforeEach
    void setUp () {
        fakeStoreProduct1 = new FakeStoreProductDTO(1L, "Iphone-15", 150000.0, "Apple Iphone 15", "Electronics", "image1.jpg");
        fakeStoreProduct2 = new FakeStoreProductDTO(2L, "Nude Nail Paint", 250.0, "Maybellene nude shade nail paint", "Beauty", "image2.jpg");
        fakeStoreProduct3 = new FakeStoreProductDTO(3L, "One Plus 10", 30000.0, "Android Phone of One Plus with 16GB RAM", "Electronics", "image3.jpg");

        genericProduct1 = new GenericProductDTO("1", "Iphone-15", "Apple Iphone 15", "image1.jpg", "Electronics", 150000.0);
        genericProduct2 = new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0);
        genericProduct3 = new GenericProductDTO("3", "One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", "Electronics", 30000.0);
    }

    @Test
    public void testGetAllProducts() {
        when(fakeStoreProductClientMock.getAllProductsFromFakeStore())
                .thenReturn(Arrays.asList(fakeStoreProduct1));

        List<GenericProductDTO> products = fakeStoreProductService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertEquals(1, products.size());

        Assertions.assertEquals(genericProduct1.getId(), products.get(0).getId());
        Assertions.assertEquals(genericProduct1.getTitle(), products.get(0).getTitle());
        Assertions.assertEquals(genericProduct1.getDescription(), products.get(0).getDescription());
        Assertions.assertEquals(genericProduct1.getImage(), products.get(0).getImage());
        Assertions.assertEquals(genericProduct1.getCategory_name(), products.get(0).getCategory_name());
        Assertions.assertEquals(genericProduct1.getPriceVal(), products.get(0).getPriceVal());
    }


    @Test
    public void testGetProductsByIdGivesCorrectResponse() throws IdNotFoundException {
        when(fakeStoreProductClientMock.getProductById("1"))
                .thenReturn(fakeStoreProduct1);
        String token = RandomStringUtils.randomAlphanumeric(30);

        GenericProductDTO toBeReturned = fakeStoreProductService.getProductsById(token, "1");

        Assertions.assertNotNull(toBeReturned);

        Assertions.assertEquals(genericProduct1.getId(), toBeReturned.getId());
        Assertions.assertEquals(genericProduct1.getTitle(), toBeReturned.getTitle());
        Assertions.assertEquals(genericProduct1.getDescription(), toBeReturned.getDescription());
        Assertions.assertEquals(genericProduct1.getImage(), toBeReturned.getImage());
        Assertions.assertEquals(genericProduct1.getCategory_name(), toBeReturned.getCategory_name());
        Assertions.assertEquals(genericProduct1.getPriceVal(), toBeReturned.getPriceVal());
    }

    @Test
    public void testGetProductsByIdGivesNullForInvalidId() throws IdNotFoundException {
        when(fakeStoreProductClientMock.getProductById(any()))
                .thenThrow(new IdNotFoundException("Product with id - 2 not found."));
        String token = RandomStringUtils.randomAlphanumeric(30);

        Assertions.assertThrows(IdNotFoundException.class,
                () -> fakeStoreProductService.getProductsById(token, "2"));
    }

    @Test
    public void testGetProductsWithLimit() {
        List<FakeStoreProductDTO> allProducts = Arrays.asList(
                fakeStoreProduct1, fakeStoreProduct2, fakeStoreProduct3);

        List<FakeStoreProductDTO> productsToBeReturned = Arrays.asList(allProducts.get(0), allProducts.get(1));
        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct1, genericProduct2);

        when(fakeStoreProductClientMock.getAllProductsWithLimitFromFakeStore(anyInt()))
                .thenReturn(productsToBeReturned);

        List<GenericProductDTO> responseProducts = fakeStoreProductService.getProductsWithLimit(2);

        Assertions.assertEquals(2, responseProducts.size());
        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts.get(0).getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts.get(0).getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts.get(0).getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts.get(0).getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts.get(0).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts.get(0).getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts.get(1).getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts.get(1).getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts.get(1).getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts.get(1).getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts.get(1).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts.get(1).getPriceVal());
    }

    @Test
    public void testGetAllProductsWithSortById() {
        List<FakeStoreProductDTO> fakestoreProducts = Arrays.asList(
                fakeStoreProduct1, fakeStoreProduct2);
        Collections.reverse(fakestoreProducts);

        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct2, genericProduct1
        );

        when(fakeStoreProductClientMock.getAllProductsWithSortingFromFakeStore("desc"))
                .thenReturn(fakestoreProducts);

        List<GenericProductDTO> responseProducts = fakeStoreProductService.getAllProductsWithSortById("desc");

        Assertions.assertEquals(2, responseProducts.size());
        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts.get(0).getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts.get(0).getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts.get(0).getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts.get(0).getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts.get(0).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts.get(0).getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts.get(1).getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts.get(1).getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts.get(1).getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts.get(1).getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts.get(1).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts.get(1).getPriceVal());
}

    @Test
    public void testGetAllCategories() {
        HashSet<String> categorySet = new HashSet<>();
        categorySet.add(fakeStoreProduct1.getCategory());
        categorySet.add(fakeStoreProduct2.getCategory());
        categorySet.add(fakeStoreProduct3.getCategory());

        List<String> allCategories = new ArrayList<>(categorySet);

        when(fakeStoreProductClientMock.getAllCategoriesFromFakeStore())
                .thenReturn(allCategories);

        List<String> responseCategories = fakeStoreProductService.getAllCategories();

        Assertions.assertNotNull(responseCategories);
        Assertions.assertEquals(2, responseCategories.size());
        Assertions.assertEquals(allCategories.get(0), responseCategories.get(0));
        Assertions.assertEquals(allCategories.get(1), responseCategories.get(1));
    }

    @Test
    public void testGetAllProductsByCategoryGivesCorrectResponse() throws CategoryNotFoundException {
        List<FakeStoreProductDTO> fakeStoreProducts = Arrays.asList(fakeStoreProduct1, fakeStoreProduct2, fakeStoreProduct3);
        List<FakeStoreProductDTO> expectedFakeStoreProducts = Arrays.asList(fakeStoreProduct1, fakeStoreProduct3);

        List<GenericProductDTO> expectedProducts = Arrays.asList(genericProduct1, genericProduct3);

        when(fakeStoreProductClientMock.getAllProductsByCategoryFromFakeStore(any(String.class)))
                .thenReturn(expectedFakeStoreProducts);

        List<GenericProductDTO> responseProducts = fakeStoreProductService.getAllProductsByCategory("Electronics");

        Assertions.assertEquals(2, responseProducts.size());
        Assertions.assertEquals(expectedProducts.get(0).getId(), responseProducts.get(0).getId());
        Assertions.assertEquals(expectedProducts.get(0).getTitle(), responseProducts.get(0).getTitle());
        Assertions.assertEquals(expectedProducts.get(0).getDescription(), responseProducts.get(0).getDescription());
        Assertions.assertEquals(expectedProducts.get(0).getImage(), responseProducts.get(0).getImage());
        Assertions.assertEquals(expectedProducts.get(0).getCategory_name(), responseProducts.get(0).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(0).getPriceVal(), responseProducts.get(0).getPriceVal());

        Assertions.assertEquals(expectedProducts.get(1).getId(), responseProducts.get(1).getId());
        Assertions.assertEquals(expectedProducts.get(1).getTitle(), responseProducts.get(1).getTitle());
        Assertions.assertEquals(expectedProducts.get(1).getDescription(), responseProducts.get(1).getDescription());
        Assertions.assertEquals(expectedProducts.get(1).getImage(), responseProducts.get(1).getImage());
        Assertions.assertEquals(expectedProducts.get(1).getCategory_name(), responseProducts.get(1).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(1).getPriceVal(), responseProducts.get(1).getPriceVal());
    }

    @Test
    public void testGetAllProductsByCategoryGivesErrorForInvalidCategory() throws CategoryNotFoundException {

        when(fakeStoreProductClientMock.getAllProductsByCategoryFromFakeStore(any(String.class)))
                .thenThrow(new CategoryNotFoundException("Category with name - Jewelry not found."));

        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> fakeStoreProductService.getAllProductsByCategory("Jewelry"));
    }

    @Test
    public void testCreateNewProduct() {
        when(fakeStoreProductClientMock.createNewProductInFakeStore(any(FakeStoreProductDTO.class)))
                .thenReturn(fakeStoreProduct1);

        GenericProductDTO responseProduct = fakeStoreProductService.createNewProduct(genericProduct1);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(genericProduct1.getId(), responseProduct.getId());
        Assertions.assertEquals(genericProduct1.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(genericProduct1.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(genericProduct1.getImage(), responseProduct.getImage());
        Assertions.assertEquals(genericProduct1.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(genericProduct1.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateProductById() {
        FakeStoreProductDTO updatedFakeStoreProduct = fakeStoreProduct1;
        updatedFakeStoreProduct.setTitle("Sports shoes");
        updatedFakeStoreProduct.setCategory("Shoes");
        updatedFakeStoreProduct.setDescription("Men's Nike Sports shoes");
        updatedFakeStoreProduct.setPrice(25000.0);
        updatedFakeStoreProduct.setImage("image123.jpg");

        GenericProductDTO expectedUpdatedProduct = genericProduct1;
        expectedUpdatedProduct.setTitle("Sports shoes");
        expectedUpdatedProduct.setCategory_name("Shoes");
        expectedUpdatedProduct.setDescription("Men's Nike Sports shoes");
        expectedUpdatedProduct.setPriceVal(25000.0);
        expectedUpdatedProduct.setImage("image123.jpg");

        when(fakeStoreProductClientMock.updateProductById(any(String.class), any(FakeStoreProductDTO.class)))
                .thenReturn(updatedFakeStoreProduct);

        GenericProductDTO responseProduct = fakeStoreProductService.updateProductById("1", expectedUpdatedProduct);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(expectedUpdatedProduct.getId(), responseProduct.getId());
        Assertions.assertEquals(expectedUpdatedProduct.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(expectedUpdatedProduct.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(expectedUpdatedProduct.getImage(), responseProduct.getImage());
        Assertions.assertEquals(expectedUpdatedProduct.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(expectedUpdatedProduct.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateSubProductById() {
        FakeStoreProductDTO updatedFakeStoreProduct = fakeStoreProduct1;
        updatedFakeStoreProduct.setTitle("Sports shoes");
        updatedFakeStoreProduct.setCategory("Shoes");
        updatedFakeStoreProduct.setDescription("Men's Nike Sports shoes");

        GenericProductDTO expectedUpdatedProduct = genericProduct1;
        expectedUpdatedProduct.setTitle("Sports shoes");
        expectedUpdatedProduct.setCategory_name("Shoes");
        expectedUpdatedProduct.setDescription("Men's Nike Sports shoes");

        when(fakeStoreProductClientMock.updateSubProductById(any(String.class), any(FakeStoreProductDTO.class)))
                .thenReturn(updatedFakeStoreProduct);

        GenericProductDTO responseProduct = fakeStoreProductService.updateSubProductById("1", expectedUpdatedProduct);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(expectedUpdatedProduct.getId(), responseProduct.getId());
        Assertions.assertEquals(expectedUpdatedProduct.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(expectedUpdatedProduct.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(expectedUpdatedProduct.getImage(), responseProduct.getImage());
        Assertions.assertEquals(expectedUpdatedProduct.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(expectedUpdatedProduct.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testDeleteProductByIdGivesCorrectResponse() throws IdNotFoundException {

        when(fakeStoreProductClientMock.deleteProductById("1"))
                .thenReturn(fakeStoreProduct1);

        GenericProductDTO responseProduct = fakeStoreProductService.deleteProductById("1");

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(genericProduct1.getId(), responseProduct.getId());
        Assertions.assertEquals(genericProduct1.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(genericProduct1.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(genericProduct1.getImage(), responseProduct.getImage());
        Assertions.assertEquals(genericProduct1.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(genericProduct1.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testDeleteProductByIdGivesNullForInvalidId()throws IdNotFoundException{
        when(fakeStoreProductClientMock.deleteProductById("2"))
                .thenReturn(null);

        Assertions.assertThrows(IdNotFoundException.class,
                () -> fakeStoreProductService.deleteProductById("2"));
    }

}