package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.models.Category;
import dev.pratishtha.project.productService.models.Price;
import dev.pratishtha.project.productService.models.Product;
import dev.pratishtha.project.productService.repositories.CategoryRepository;
import dev.pratishtha.project.productService.repositories.PriceRepository;
import dev.pratishtha.project.productService.repositories.ProductRepository;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.FakeStoreProductClient;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
//import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DatabaseProductServiceImplTest {

    @Autowired
    private DatabaseProductServiceImpl databaseProductService;

    @MockBean
    private ProductRepository productRepositoryMock;

    @MockBean
    private CategoryRepository categoryRepositoryMock;

    @MockBean
    private PriceRepository priceRepositoryMock;

    private GenericProductDTO genericProduct1;
    private GenericProductDTO genericProduct2;
    private GenericProductDTO genericProduct3;
    private Product product1;
    private Product product2;
    private Product product3;
    private Price price1;
    private Price price2;
    private Price price3;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp () {
        category1 = new Category();
        category1.setName("Electronics");
        category1.setUuid(UUID.randomUUID());

        category2 = new Category();
        category2.setName("Beauty");
        category2.setUuid(UUID.randomUUID());

        price1 = new Price();
        price1.setValue(150000.0);
        price1.setCurrency("INR");
        price1.setUuid(UUID.randomUUID());

        price2 = new Price();
        price2.setValue(250.0);
        price2.setCurrency("INR");
        price1.setUuid(UUID.randomUUID());

        price3 = new Price();
        price3.setValue(30000.0);
        price3.setCurrency("INR");
        price1.setUuid(UUID.randomUUID());

        product1 = new Product("Iphone-15", "Apple Iphone 15", "image1.jpg", category1, price1, 3);
        product1.setUuid(UUID.randomUUID());

        product2 = new Product("Nude Nail Paint", "Maybellene nude shade nail paint", "image2.jpg", category2, price2, 5);
        product2.setUuid(UUID.randomUUID());

        product3 = new Product("One Plus 10", "Android Phone of One Plus with 16GB RAM", "image3.jpg", category1, price3, 10);
        product3.setUuid(UUID.randomUUID());

        genericProduct1 = new GenericProductDTO(product1.getUuid().toString(), product1.getTitle(), product1.getDescription(), product1.getImage(), product1.getCategory().getName(), product1.getPrice().getValue(), product1.getInventoryCount());
        genericProduct2 = new GenericProductDTO(product2.getUuid().toString(), product2.getTitle(), product2.getDescription(), product2.getImage(), product2.getCategory().getName(), product2.getPrice().getValue(), product2.getInventoryCount());
        genericProduct3 = new GenericProductDTO(product3.getUuid().toString(), product3.getTitle(), product3.getDescription(), product3.getImage(), product3.getCategory().getName(), product3.getPrice().getValue(), product3.getInventoryCount());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);
        List<GenericProductDTO> expectedProducts = Arrays.asList(genericProduct1, genericProduct2);
        
        when(productRepositoryMock.findAll()).thenReturn(products);

        List<GenericProductDTO> responseProducts = databaseProductService.getAllProducts();

        Assertions.assertNotNull(products);
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
    public void testGetProductsByIdGivesCorrectResponse() throws IdNotFoundException {
        when(productRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.of(product1));
//        String token = RandomStringUtils.randomAlphanumeric(30);
        String token = "bfuh89ey239ruihfjsbdbshjvbhjbvs89r";

        GenericProductDTO toBeReturned = databaseProductService.getProductsById(token, String.valueOf(product1.getUuid()));

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
        when(productRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
//        String token = RandomStringUtils.randomAlphanumeric(30);
        String token = "bfuh89ey239ruihfjsbdbshjvbhjbvs89r";

        Assertions.assertThrows(IdNotFoundException.class,
                () -> databaseProductService.getProductsById(token, String.valueOf(UUID.randomUUID())));
    }

    @Test
    public void testGetProductsWithLimit() {
        List<Product> allProducts = Arrays.asList(
                product1, product2, product3);

        List<Product> products = Arrays.asList(allProducts.get(0), allProducts.get(1));
        List<GenericProductDTO> expectedProducts = Arrays.asList(genericProduct1, genericProduct2);

        when(productRepositoryMock.findProductsByLimit(anyInt()))
                .thenReturn(products);

        List<GenericProductDTO> responseProducts = databaseProductService.getProductsWithLimit(2);

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
        List<Product> productsTobeReturned = Arrays.asList(
                product3, product2);

        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct3, genericProduct2
        );

        when(productRepositoryMock.findAllByOrderByUuidDesc())
                .thenReturn(productsTobeReturned);

        List<GenericProductDTO> responseProducts = databaseProductService.getAllProductsWithSortById("desc");

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
    public void testGetAllProductsWithSortByTitleDesc() {
        List<Product> allProducts = Arrays.asList(
                product1, product3, product2);

        List<Product> productsTobeReturned = Arrays.asList(
                allProducts.get(1), allProducts.get(2), allProducts.get(0));

        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct3, genericProduct2, genericProduct1);

        when(productRepositoryMock.findAllByOrderByTitleDesc())
                .thenReturn(productsTobeReturned);

        List<GenericProductDTO> responseProducts = databaseProductService.getAllProductsWithSortByTitle("desc");

        Assertions.assertEquals(3, responseProducts.size());
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

        Assertions.assertEquals(expectedProducts.get(2).getId(), responseProducts.get(2).getId());
        Assertions.assertEquals(expectedProducts.get(2).getTitle(), responseProducts.get(2).getTitle());
        Assertions.assertEquals(expectedProducts.get(2).getDescription(), responseProducts.get(2).getDescription());
        Assertions.assertEquals(expectedProducts.get(2).getImage(), responseProducts.get(2).getImage());
        Assertions.assertEquals(expectedProducts.get(2).getCategory_name(), responseProducts.get(2).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(2).getPriceVal(), responseProducts.get(2).getPriceVal());
    }

    @Test
    public void testGetAllProductsWithSortByTitleAsc() {
        List<Product> allProducts = Arrays.asList(
                product1, product3, product2);

        List<Product> productsTobeReturned = Arrays.asList(
                allProducts.get(0), allProducts.get(2), allProducts.get(1));

        List<GenericProductDTO> expectedProducts = Arrays.asList(
                genericProduct1, genericProduct2, genericProduct3);

        when(productRepositoryMock.findAllByOrderByTitleAsc())
                .thenReturn(productsTobeReturned);

        List<GenericProductDTO> responseProducts = databaseProductService.getAllProductsWithSortByTitle("Asc");

        Assertions.assertEquals(3, responseProducts.size());
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

        Assertions.assertEquals(expectedProducts.get(2).getId(), responseProducts.get(2).getId());
        Assertions.assertEquals(expectedProducts.get(2).getTitle(), responseProducts.get(2).getTitle());
        Assertions.assertEquals(expectedProducts.get(2).getDescription(), responseProducts.get(2).getDescription());
        Assertions.assertEquals(expectedProducts.get(2).getImage(), responseProducts.get(2).getImage());
        Assertions.assertEquals(expectedProducts.get(2).getCategory_name(), responseProducts.get(2).getCategory_name());
        Assertions.assertEquals(expectedProducts.get(2).getPriceVal(), responseProducts.get(2).getPriceVal());
    }

    @Test
    public void testGetAllCategories() {
        HashSet<Category> categorySet = new HashSet<>();
        categorySet.add(product1.getCategory());
        categorySet.add(product2.getCategory());
        categorySet.add(product3.getCategory());

        List<Category> allCategoriesToBeReturned = new ArrayList<>(categorySet);
        List<String> expectedAllCategories = new ArrayList<>();
        for (Category category : allCategoriesToBeReturned) {
            expectedAllCategories.add(category.getName());
        }

        when(categoryRepositoryMock.findAll()).thenReturn(allCategoriesToBeReturned);

        List<String> responseCategories = databaseProductService.getAllCategories();

        Assertions.assertNotNull(responseCategories);
        Assertions.assertEquals(2, responseCategories.size());
        Assertions.assertEquals(expectedAllCategories.get(0), responseCategories.get(0));
        Assertions.assertEquals(expectedAllCategories.get(1), responseCategories.get(1));
    }

    @Test
    public void testGetAllProductsByCategoryGivesCorrectResponse() throws CategoryNotFoundException {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        List<Product> productsToBeReturned = Arrays.asList(allProducts.get(0), allProducts.get(2));

        List<GenericProductDTO> expectedProducts = Arrays.asList(genericProduct1, genericProduct3);

        when(categoryRepositoryMock.findByName(any(String.class)))
                .thenReturn(Optional.of(category1));

        when(productRepositoryMock.findAllByCategory(any(Category.class)))
                .thenReturn(productsToBeReturned);

        List<GenericProductDTO> responseProducts = databaseProductService.getAllProductsByCategory("Electronics");

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
    public void testGetAllProductsByCategoryGivesEmptyListForInvalidCategory() throws CategoryNotFoundException {

        when(categoryRepositoryMock.findByName(any(String.class)))
                .thenReturn(Optional.empty());

        when(productRepositoryMock.findAllByCategory(any(Category.class)))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> databaseProductService.getAllProductsByCategory("Jewelry"));
    }

    @Test
    public void testCreateNewProduct() {
        when(priceRepositoryMock.save(any(Price.class))).thenReturn(price3);
        when(categoryRepositoryMock.save(any(Category.class))).thenReturn(category1);
        when(categoryRepositoryMock.findByName(any(String.class))).thenReturn(Optional.of(category1));

        when(productRepositoryMock.save(any(Product.class))).thenReturn(product3);

        GenericProductDTO responseProduct = databaseProductService.createNewProduct(genericProduct3);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(genericProduct3.getId(), responseProduct.getId());
        Assertions.assertEquals(genericProduct3.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(genericProduct3.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(genericProduct3.getImage(), responseProduct.getImage());
        Assertions.assertEquals(genericProduct3.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(genericProduct3.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateProductByIdGivesCorrectResponse() throws IdNotFoundException {
        when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product2));
        when(categoryRepositoryMock.findByName(any(String.class))).thenReturn(Optional.of(category2));
        when(categoryRepositoryMock.save(any(Category.class))).thenReturn(category2);
        when(priceRepositoryMock.save(any(Price.class))).thenReturn(price2);

        Product updatedProduct = product2;
        when(productRepositoryMock.save(any(Product.class))).thenReturn(updatedProduct);

        GenericProductDTO expectedUpdatedProduct = genericProduct2;

        GenericProductDTO responseProduct = databaseProductService.updateProductById(product2.getUuid().toString(), expectedUpdatedProduct);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(expectedUpdatedProduct.getId(), responseProduct.getId());
        Assertions.assertEquals(expectedUpdatedProduct.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(expectedUpdatedProduct.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(expectedUpdatedProduct.getImage(), responseProduct.getImage());
        Assertions.assertEquals(expectedUpdatedProduct.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(expectedUpdatedProduct.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateProductByIdGivesErrorForInvalidId() {

        when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(IdNotFoundException.class,
                () -> databaseProductService.updateProductById(String.valueOf(UUID.randomUUID()), genericProduct1));
    }

    @Test
    public void testUpdateSubProductByIdGivesCorrectResponse() throws IdNotFoundException {
        when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product2));
        when(categoryRepositoryMock.findByName(any(String.class))).thenReturn(Optional.of(category2));
        when(categoryRepositoryMock.save(any(Category.class))).thenReturn(category2);
        when(priceRepositoryMock.save(any(Price.class))).thenReturn(price2);

        Product updatedProduct = product2;
        when(productRepositoryMock.save(any(Product.class))).thenReturn(updatedProduct);

        GenericProductDTO expectedUpdatedProduct = genericProduct2;

        GenericProductDTO responseProduct = databaseProductService.updateSubProductById(product2.getUuid().toString(), expectedUpdatedProduct);

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(expectedUpdatedProduct.getId(), responseProduct.getId());
        Assertions.assertEquals(expectedUpdatedProduct.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(expectedUpdatedProduct.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(expectedUpdatedProduct.getImage(), responseProduct.getImage());
        Assertions.assertEquals(expectedUpdatedProduct.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(expectedUpdatedProduct.getPriceVal(), responseProduct.getPriceVal());
    }

    @Test
    public void testUpdateSubProductByIdGivesErrorForInvalidId() {

        when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(IdNotFoundException.class,
                () -> databaseProductService.updateSubProductById(String.valueOf(UUID.randomUUID()), genericProduct1));
    }

    @Test
    public void testDeleteProductByIdGivesCorrectResponse() throws IdNotFoundException {

        when(productRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.of(product1));

        GenericProductDTO responseProduct = databaseProductService.deleteProductById(product1.getUuid().toString());

        Assertions.assertNotNull(responseProduct);

        Assertions.assertEquals(genericProduct1.getId(), responseProduct.getId());
        Assertions.assertEquals(genericProduct1.getTitle(), responseProduct.getTitle());
        Assertions.assertEquals(genericProduct1.getDescription(), responseProduct.getDescription());
        Assertions.assertEquals(genericProduct1.getImage(), responseProduct.getImage());
        Assertions.assertEquals(genericProduct1.getCategory_name(), responseProduct.getCategory_name());
        Assertions.assertEquals(genericProduct1.getPriceVal(), responseProduct.getPriceVal());

        verify(productRepositoryMock, times(1)).deleteById(any(UUID.class));
    }

    @Test
    public void testDeleteProductByIdGivesNullForInvalidId(){
        when(productRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(IdNotFoundException.class,
                () -> databaseProductService.deleteProductById(UUID.randomUUID().toString()));
    }

}