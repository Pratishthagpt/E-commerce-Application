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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DatabaseProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private PriceRepository priceRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public DatabaseProductServiceImpl(ProductRepository productRepository,
                                      PriceRepository priceRepository,
                                      CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<GenericProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<GenericProductDTO> genericProductDTOS = new ArrayList<>();
        for (Product product : products) {
            GenericProductDTO genericProductDTO = convertProductToGenericProductDto(product);
            genericProductDTOS.add(genericProductDTO);
        }
        return genericProductDTOS;
    }

    @Override
    public GenericProductDTO getProductsById(String id) throws IdNotFoundException {
        UUID productId = UUID.fromString(id);

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IdNotFoundException("Product with id - " + id + " not found.");
        }
        Product product = productOptional.get();

        GenericProductDTO genericProductDTO = convertProductToGenericProductDto(product);

        return genericProductDTO;
    }

    @Override
    public List<GenericProductDTO> getProductsWithLimit(int limit) {
        List<Product> products = productRepository.findProductsByLimit(limit);

        List<GenericProductDTO> genericProductDTOS = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProduct = convertProductToGenericProductDto(product);
            genericProductDTOS.add(genericProduct);
        }
        return genericProductDTOS;
    }

    @Override
    public List<GenericProductDTO> getAllProductsWithSortById(String sortType) {

        List<Product> products;

        if (sortType.equalsIgnoreCase("Descending") || sortType.equalsIgnoreCase("desc")) {
            products = productRepository.findAllByOrderByUuidDesc();
        }
        else {
            products = productRepository.findAll();
        }

        List<GenericProductDTO> genericProductDTOS = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProduct = convertProductToGenericProductDto(product);
            genericProductDTOS.add(genericProduct);
        }
        return genericProductDTOS;

    }

    @Override
    public List<GenericProductDTO> getAllProductsWithSortByTitle(String sortType) {
        List<Product> products;

        if (sortType.equalsIgnoreCase("Descending") || sortType.equalsIgnoreCase("desc")) {
            products = productRepository.findAllByOrderByTitleDesc();
        }
        else {
            products = productRepository.findAllByOrderByTitleAsc();
        }

        List<GenericProductDTO> genericProductDTOS = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProduct = convertProductToGenericProductDto(product);
            genericProductDTOS.add(genericProduct);
        }
        return genericProductDTOS;
    }

    @Override
    public List<String> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    @Override
    public List<GenericProductDTO> getAllProductsByCategory(String categoryRequest) throws CategoryNotFoundException {
//        find the category object from category repository
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryRequest);

//        check if category exists or not
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Category with name - " + categoryRequest + " not found.");
        }

        Category category = categoryOptional.get();

//        find all products by category from product repository
        List<Product> products = productRepository.findAllByCategory(category);

//        convert the product object into generic product object
        List<GenericProductDTO> genericProductDTOS = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProduct = convertProductToGenericProductDto(product);
            genericProductDTOS.add(genericProduct);
        }
        return genericProductDTOS;

    }

    @Override
    public GenericProductDTO createNewProduct(GenericProductDTO genericProductDTO) {
        Product product = new Product();

//        1. setting values for primitive datatypes of product
        product.setTitle(genericProductDTO.getTitle());
        product.setDescription(genericProductDTO.getDescription());
        product.setImage(genericProductDTO.getImage());

//        2. Creating new price, setting product price value into that and saving into db
        Price price = new Price();
        price.setCurrency("INR");
        price.setValue(genericProductDTO.getPriceVal());

        Price savedPrice = priceRepository.save(price);
        product.setPrice(savedPrice);

//        3. Finding category by name, if category already present then assigning that category to
//        product, else creating new category
//        Adding category name and saving into db
        Optional<Category> categoryOptional = categoryRepository.findByName(genericProductDTO.getCategory_name());
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            product.setCategory(category);
        }
        else {
            Category category;category = new Category();
            category.setName(genericProductDTO.getCategory_name());

            Category savedCategory = categoryRepository.save(category);
            product.setCategory(savedCategory);
        }

//        4. saving the new product into db
        Product savedProduct = productRepository.save(product);

//        5. converting the saved product into generic product dto to give response
//        from controller to FE
        GenericProductDTO savedGenericProductDto = convertProductToGenericProductDto(savedProduct);

        return savedGenericProductDto;
    }

    @Override
    public GenericProductDTO updateProductById(String id, GenericProductDTO genericProductRequest) throws IdNotFoundException {

//        getting product by id from product repository
        Optional<Product> productOptional = productRepository.findById(UUID.fromString(id));

        if (productOptional.isEmpty()) {
            throw new IdNotFoundException("Product with id - " + id + " not found.");
        }

        Product product = productOptional.get();

//        1. setting values for primitive datatypes of product
        product.setTitle(genericProductRequest.getTitle());
        product.setDescription(genericProductRequest.getDescription());
        product.setImage(genericProductRequest.getImage());

//        2. Getting price from product, setting updated product price value and saving into db
        Price price = product.getPrice();
        price.setCurrency("INR");
        price.setValue(genericProductRequest.getPriceVal());

        Price savedPrice = priceRepository.save(price);
        product.setPrice(savedPrice);

//        getting the category of prev product before updating it
        Category prevCategory = product.getCategory();

//        3. Finding category by name, if category already present then assigning that category to
//        product, else creating new category
//        Adding category name and saving into db
        Optional<Category> categoryOptional = categoryRepository.findByName(genericProductRequest.getCategory_name());
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            product.setCategory(category);
        }
        else {
            Category category = new Category();
            category.setName(genericProductRequest.getCategory_name());

            Category savedCategory = categoryRepository.save(category);
            product.setCategory(savedCategory);
        }

//        4. saving the updated product into db
        Product savedProduct = productRepository.save(product);

//        check if the no. of products by category name of previous product is 0 or not,
//        if its zero then, delete that category
        List<Product> productsByCategory = productRepository.findAllByCategory(prevCategory);

        if (productsByCategory.size() == 0) {
            categoryRepository.deleteById(prevCategory.getUuid());
        }

//        5. converting the saved product into generic product dto to give response
//        from controller to FE
        GenericProductDTO updatedGenericProductDto = convertProductToGenericProductDto(savedProduct);

        return updatedGenericProductDto;
    }

    @Override
    public GenericProductDTO updateSubProductById(String id, GenericProductDTO genericProductRequest) throws IdNotFoundException {

//        getting product by id from product repository
        Optional<Product> productOptional = productRepository.findById(UUID.fromString(id));

        if (productOptional.isEmpty()) {
            throw new IdNotFoundException("Product with id - " + id + " not found.");
        }

        Product product = productOptional.get();

//        1. setting values for primitive datatypes of product
        product.setTitle(genericProductRequest.getTitle());
        product.setDescription(genericProductRequest.getDescription());
        product.setImage(genericProductRequest.getImage());

//        2. Getting price from product, setting updated product price value and saving into db
        Price price = product.getPrice();
        price.setCurrency("INR");
        price.setValue(genericProductRequest.getPriceVal());

        Price savedPrice = priceRepository.save(price);
        product.setPrice(savedPrice);

//        getting the category of prev product before updating it
        Category prevCategory = product.getCategory();

//        3. Finding category by name, if category already present then assigning that category to
//        product, else creating new category
//        Adding category name and saving into db
        Optional<Category> categoryOptional = categoryRepository.findByName(genericProductRequest.getCategory_name());
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            product.setCategory(category);
        }
        else {
            Category category = new Category();
            category.setName(genericProductRequest.getCategory_name());

            Category savedCategory = categoryRepository.save(category);
            product.setCategory(savedCategory);
        }

//        4. saving the updated product into db
        Product savedProduct = productRepository.save(product);

//        check if the no. of products by category name of previous product is 0 or not,
//        if its zero then, delete that category
        List<Product> productsByCategory = productRepository.findAllByCategory(prevCategory);

        if (productsByCategory.size() == 0) {
            categoryRepository.deleteById(prevCategory.getUuid());
        }

//        5. converting the saved product into generic product dto to give response
//        from controller to FE
        GenericProductDTO updatedGenericProductDto = convertProductToGenericProductDto(savedProduct);

        return updatedGenericProductDto;
    }

    @Override
    public GenericProductDTO deleteProductById(String id) throws IdNotFoundException {
        Optional<Product> productOptional = productRepository.findById(UUID.fromString(id));

        if (productOptional.isEmpty()) {
            throw new IdNotFoundException("Product with id - " + id + " not found.");
        }
        Product product = productOptional.get();

//        getting the category of prev product before updating it
        Category prevCategory = product.getCategory();

        GenericProductDTO genericProductDTO = convertProductToGenericProductDto(product);

//        deleting the product
        productRepository.deleteById(UUID.fromString(id));

//        check if the no. of products by category name of previous product is 0 or not,
//        if its zero then, delete that category
        List<Product> productsByCategory = productRepository.findAllByCategory(prevCategory);

        if (productsByCategory.size() == 0) {
            categoryRepository.deleteById(prevCategory.getUuid());
        }

        return genericProductDTO;
    }


    public GenericProductDTO convertProductToGenericProductDto (Product product) {
        GenericProductDTO genericProductDTO = new GenericProductDTO();

        genericProductDTO.setId(String.valueOf(product.getUuid()));
        genericProductDTO.setTitle(product.getTitle());
        genericProductDTO.setDescription(product.getDescription());
        genericProductDTO.setPriceVal(product.getPrice().getValue());
        genericProductDTO.setCategory_name(product.getCategory().getName());
        genericProductDTO.setImage(product.getImage());

        return genericProductDTO;
    }
}
