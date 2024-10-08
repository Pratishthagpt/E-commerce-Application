package dev.pratishtha.project.productService.services;


import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.dtos.SearchAndSortProductDTO;
import dev.pratishtha.project.productService.dtos.SearchProductDTO;
import dev.pratishtha.project.productService.dtos.SortValue;
import dev.pratishtha.project.productService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.productService.models.Product;
import dev.pratishtha.project.productService.repositories.ProductRepository;
import dev.pratishtha.project.productService.security.JwtData;
import dev.pratishtha.project.productService.security.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService{

    private ProductRepository productRepository;
    private TokenValidator tokenValidator;

    @Autowired
    public SearchServiceImpl(ProductRepository productRepository, TokenValidator tokenValidator) {
        this.productRepository = productRepository;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public Page<GenericProductDTO> getProductsByTitleSearch(String token, SearchProductDTO searchProductDTO) {

//        checking for user validation
        Optional<JwtData> userJwtData = tokenValidator.validateToken(token);
        if (userJwtData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

//        creating a Pageable object
        Pageable pageable = PageRequest.of(searchProductDTO.getPageNumber(), searchProductDTO.getPageSize());

        Page<Product> productPage = productRepository.findAllByTitle(searchProductDTO.getQuery(), pageable);

        List<Product> products = productPage.get().toList();
        List<GenericProductDTO> genericProducts = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProductDTO = convertProductToGenericProductDto(product);
            genericProducts.add(genericProductDTO);
        }

//        converting List<GenericProductDTO> to Page<GenericProductDTO>

        Page<GenericProductDTO> genericProductPage =
                new PageImpl<GenericProductDTO>(genericProducts,
                        productPage.getPageable(), productPage.getTotalElements());

        return genericProductPage;

    }

    @Override
    public Page<GenericProductDTO> getProductsBySearchingSortedTitleAndInventoryCount(String token
            , SearchAndSortProductDTO searchAndSortProductDTO) {

//        checking for user validation
        Optional<JwtData> userJwtData = tokenValidator.validateToken(token);
        if (userJwtData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        List<SortValue> sortTypes = searchAndSortProductDTO.getSortTypes();

        Sort sort;
        if (sortTypes.get(0).getSortType().equals("desc")) {
            sort = Sort.by(sortTypes.get(0).getSortValue()).descending();
        }
        else {
            sort = Sort.by(sortTypes.get(0).getSortValue());
        }

        for (SortValue val : sortTypes) {
            if (val.getSortType().equals("desc")) {
                sort = sort.and(Sort.by(val.getSortValue()).descending());
            }
            else {
                sort = sort.and(Sort.by(val.getSortValue()));
            }
        }

//        creating a Pageable object
        Pageable pageable = PageRequest.of(searchAndSortProductDTO.getPageNumber(),
                searchAndSortProductDTO.getPageSize(), sort);

        Page<Product> productPage = productRepository.findAllByTitleContaining(searchAndSortProductDTO.getQuery(), pageable);

        List<Product> products = productPage.get().toList();
        List<GenericProductDTO> genericProducts = new ArrayList<>();

        for (Product product : products) {
            GenericProductDTO genericProductDTO = convertProductToGenericProductDto(product);
            genericProducts.add(genericProductDTO);
        }

//        converting List<GenericProductDTO> to Page<GenericProductDTO>

        Page<GenericProductDTO> genericProductPage =
                new PageImpl<GenericProductDTO>(genericProducts,
                        productPage.getPageable(), productPage.getTotalElements());

        return genericProductPage;
    }

    public GenericProductDTO convertProductToGenericProductDto (Product product) {
        GenericProductDTO genericProductDTO = new GenericProductDTO();

        genericProductDTO.setId(String.valueOf(product.getUuid()));
        genericProductDTO.setTitle(product.getTitle());
        genericProductDTO.setDescription(product.getDescription());
        genericProductDTO.setPriceVal(product.getPrice().getValue());
        genericProductDTO.setCategory_name(product.getCategory().getName());
        genericProductDTO.setImage(product.getImage());
        genericProductDTO.setInventoryCount(product.getInventoryCount());

        return genericProductDTO;
    }
}
