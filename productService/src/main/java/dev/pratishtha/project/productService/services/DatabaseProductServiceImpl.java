package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.models.Category;
import dev.pratishtha.project.productService.models.Price;
import dev.pratishtha.project.productService.models.Product;
import dev.pratishtha.project.productService.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Autowired
    public DatabaseProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        return null;
    }

    @Override
    public List<GenericProductDTO> getProductByIdWithLimit(int limit) {
        return List.of();
    }

    @Override
    public List<GenericProductDTO> getProductByIdWithSort(String sortType) {
        return List.of();
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
        return null;
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
    }
}
