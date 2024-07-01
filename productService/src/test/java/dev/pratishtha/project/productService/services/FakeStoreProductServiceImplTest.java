package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.FakeStoreProductClient;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private GenericProductDTO genericProduct;
    private FakeStoreProductDTO fakeStoreProduct;

    @BeforeEach
    void setUp () {
        fakeStoreProduct = new FakeStoreProductDTO();
        fakeStoreProduct.setId(1L);
        fakeStoreProduct.setTitle("Iphone-15");
        fakeStoreProduct.setCategory("Electronics");
        fakeStoreProduct.setImage("image1.jpg");
        fakeStoreProduct.setDescription("Apple Iphone 15");
        fakeStoreProduct.setPrice(150000.0);

        genericProduct = new GenericProductDTO();
        genericProduct.setId("1");
        genericProduct.setTitle("Iphone-15");
        genericProduct.setCategory_name("Electronics");
        genericProduct.setImage("image1.jpg");
        genericProduct.setDescription("Apple Iphone 15");
        genericProduct.setPriceVal(150000.0);
    }

    @Test
    public void testGetAllProducts() {
        when(fakeStoreProductClientMock.getAllProductsFromFakeStore())
                .thenReturn(Arrays.asList(fakeStoreProduct));

        List<GenericProductDTO> products = fakeStoreProductService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertEquals(1, products.size());

        Assertions.assertEquals(genericProduct.getId(), products.get(0).getId());
        Assertions.assertEquals(genericProduct.getTitle(), products.get(0).getTitle());
        Assertions.assertEquals(genericProduct.getDescription(), products.get(0).getDescription());
        Assertions.assertEquals(genericProduct.getImage(), products.get(0).getImage());
        Assertions.assertEquals(genericProduct.getCategory_name(), products.get(0).getCategory_name());
        Assertions.assertEquals(genericProduct.getPriceVal(), products.get(0).getPriceVal());
    }


    @Test
    public void testGetProductsByIdGivesCorrectResponse() throws IdNotFoundException {
        when(fakeStoreProductClientMock.getProductById("1"))
                .thenReturn(fakeStoreProduct);

        GenericProductDTO toBeReturned = fakeStoreProductService.getProductsById("1");

        Assertions.assertNotNull(toBeReturned);

        Assertions.assertEquals(genericProduct.getId(), toBeReturned.getId());
        Assertions.assertEquals(genericProduct.getTitle(), toBeReturned.getTitle());
        Assertions.assertEquals(genericProduct.getDescription(), toBeReturned.getDescription());
        Assertions.assertEquals(genericProduct.getImage(), toBeReturned.getImage());
        Assertions.assertEquals(genericProduct.getCategory_name(), toBeReturned.getCategory_name());
        Assertions.assertEquals(genericProduct.getPriceVal(), toBeReturned.getPriceVal());
    }

    @Test
    public void testGetProductsByIdGivesNullForInvalidId() throws IdNotFoundException {
        when(fakeStoreProductClientMock.getProductById(any()))
                .thenThrow(new IdNotFoundException("Product with id - 2 not found."));

        Assertions.assertThrows(IdNotFoundException.class,
                () -> fakeStoreProductService.getProductsById("2"));
    }

    @Test
    public void testGetProductsWithLimit() {
        List<FakeStoreProductDTO> allProducts = Arrays.asList(
                fakeStoreProduct,
                new FakeStoreProductDTO(2L, "Nude Nail Paint", 250.0, "Maybellene nude shade nail paint", "Beauty", "image2.jpg"),
                new FakeStoreProductDTO(3L, "One Plus 10", 30000.0, "Android Phone of One Plus with 16GB RAM", "Electronics", "image3.jpg"),
                new FakeStoreProductDTO(4L, "Plazo Kurta Set", 2500.0, "BIBA Pink Plazo Kurta Set", "Clothing", "image4.jpg" )
        );

        List<FakeStoreProductDTO> productsToBeReturned = Arrays.asList(allProducts.get(0), allProducts.get(1));
        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct,
                new GenericProductDTO("2", "Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", "Beauty", 250.0)
        );


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
}