package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.FakeStoreProductClient;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductFakeStoreServiceImpl implements ProductService{

    private FakeStoreProductClient fakeStoreProductClient;

    @Autowired
    public ProductFakeStoreServiceImpl(FakeStoreProductClient fakeStoreProductClient) {
        this.fakeStoreProductClient = fakeStoreProductClient;
    }

    @Override
    public List<GenericProductDTO> getAllProducts() {
        List<FakeStoreProductDTO> fakeStoreProductDTOS =
                fakeStoreProductClient.getAllProductsFromFakeStore();

        List<GenericProductDTO> genericProductList = new ArrayList<>();

        for (FakeStoreProductDTO fakeStoreProduct : fakeStoreProductDTOS) {
            GenericProductDTO genericProductDTO = convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProduct);
            genericProductList.add(genericProductDTO);
        }

        return genericProductList;
    }

    @Override
    public GenericProductDTO getProductsById(String id) throws IdNotFoundException {

        GenericProductDTO genericProductDTO =
                convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductClient.getProductById(id));

        return genericProductDTO;
    }

    @Override
    public List<GenericProductDTO> getProductByIdWithLimit(int limit) {
        List<FakeStoreProductDTO> fakeStoreProductDTOS =
                fakeStoreProductClient.getAllProductsWithLimitFromFakeStore(limit);

        List<GenericProductDTO> genericProductList = new ArrayList<>();

        for (FakeStoreProductDTO fakeStoreProduct : fakeStoreProductDTOS) {
            GenericProductDTO genericProductDTO = convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProduct);
            genericProductList.add(genericProductDTO);
        }

        return genericProductList;
    }

    @Override
    public List<GenericProductDTO> getProductByIdWithSort(String sortType) {
        List<FakeStoreProductDTO> fakeStoreProductDTOS =
                fakeStoreProductClient.getAllProductsWithSortingFromFakeStore(sortType);

        List<GenericProductDTO> genericProductList = new ArrayList<>();

        for (FakeStoreProductDTO fakeStoreProduct : fakeStoreProductDTOS) {
            GenericProductDTO genericProductDTO = convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProduct);
            genericProductList.add(genericProductDTO);
        }

        return genericProductList;
    }

    @Override
    public List<String> getAllCategories() {
        return fakeStoreProductClient.getAllCategoriesFromFakeStore();
    }

    @Override
    public List<GenericProductDTO> getAllProductsByCategory(String category) throws CategoryNotFoundException {
        List<FakeStoreProductDTO> fakeStoreProductDTOS =
                fakeStoreProductClient.getAllProductsByCategoryFromFakeStore(category);

        List<GenericProductDTO> genericProductsList = new ArrayList<>();
        for (FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProductDTOS) {
            GenericProductDTO genericProductDTO =
                    convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductDTO);

            genericProductsList.add(genericProductDTO);
        }

        return genericProductsList;
    }

    private GenericProductDTO convertFakeStoreProductDTOToGenericProductDTO (FakeStoreProductDTO fakeStoreProductDTO) {
        GenericProductDTO genericProductDTO = new GenericProductDTO();

        genericProductDTO.setId(fakeStoreProductDTO.getId().toString());
        genericProductDTO.setTitle(fakeStoreProductDTO.getTitle());
        genericProductDTO.setDescription(fakeStoreProductDTO.getDescription());
        genericProductDTO.setCategory_name(fakeStoreProductDTO.getCategory());
        genericProductDTO.setImage(fakeStoreProductDTO.getImage());
        genericProductDTO.setPriceVal(fakeStoreProductDTO.getPrice());

        return genericProductDTO;
    }
}
