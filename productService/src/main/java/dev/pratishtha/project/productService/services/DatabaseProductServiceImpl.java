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
        return List.of();
    }

    @Override
    public List<GenericProductDTO> getAllProductsByCategory(String category) throws CategoryNotFoundException {
        return List.of();
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
    public GenericProductDTO updateProductById(String id, GenericProductDTO genericProductRequest) {
        return null;
    }

    @Override
    public GenericProductDTO updateSubProductById(String id, GenericProductDTO genericProductRequest) {
        return null;
    }

    @Override
    public GenericProductDTO deleteProductById(String id) throws IdNotFoundException {
        return null;
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
