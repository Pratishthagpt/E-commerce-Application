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
public class FakeStoreProductServiceImpl implements ProductService{

    private FakeStoreProductClient fakeStoreProductClient;

    @Autowired
    public FakeStoreProductServiceImpl(FakeStoreProductClient fakeStoreProductClient) {
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
    public GenericProductDTO getProductsById(String token, String id) throws IdNotFoundException {

        GenericProductDTO genericProductDTO =
                convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductClient.getProductById(id));

        return genericProductDTO;
    }

    @Override
    public List<GenericProductDTO> getProductsWithLimit(int limit) {
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
    public List<GenericProductDTO> getAllProductsWithSortById(String sortType) {
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

    @Override
    public GenericProductDTO createNewProduct(GenericProductDTO genericProductRequest) {
        FakeStoreProductDTO fakeStoreProductRequest =
                convertGenericProductDTOToFakeStoreProductDTO(genericProductRequest);

        FakeStoreProductDTO fakeStoreProductDTO = fakeStoreProductClient.createNewProductInFakeStore(fakeStoreProductRequest);
//        Remember that nothing in real will insert into the fakestore database. so if we access the new id we will get a 404 error.

        GenericProductDTO genericProductDTO = convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductDTO);

        return genericProductDTO;
    }

    @Override
    public GenericProductDTO updateProductById(String id, GenericProductDTO genericProductRequest) {

        FakeStoreProductDTO fakeStoreProductRequest =
                convertGenericProductDTOToFakeStoreProductDTO(genericProductRequest);

//        remember that nothing in real will update in the database of fake store API
        FakeStoreProductDTO fakeStoreProductDTO =
                fakeStoreProductClient.updateProductById(id, fakeStoreProductRequest);

        GenericProductDTO genericProductDTO =
                convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductDTO);

        return genericProductDTO;
    }

    @Override
    public GenericProductDTO updateSubProductById(String id, GenericProductDTO genericProductRequest) {

        FakeStoreProductDTO fakeStoreProductRequest =
                convertGenericProductDTOToFakeStoreProductDTO(genericProductRequest);

//        remember that nothing in real will update in the database of fake store API
        FakeStoreProductDTO fakeStoreProductDTO =
                fakeStoreProductClient.updateSubProductById(id, fakeStoreProductRequest);

        GenericProductDTO updatedGenericProduct =
                convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductDTO);

        return updatedGenericProduct;
    }

    @Override
    public GenericProductDTO deleteProductById(String id) throws IdNotFoundException {
        FakeStoreProductDTO fakeStoreProductDTO =
                fakeStoreProductClient.deleteProductById(id);

        if (fakeStoreProductDTO == null) {
            throw new IdNotFoundException("Product with id - " + id + " not found. So, it cannot be deleted");
        }

        GenericProductDTO deletedGenericProduct =
                convertFakeStoreProductDTOToGenericProductDTO(fakeStoreProductDTO);

        return deletedGenericProduct;
    }

    @Override
    public List<GenericProductDTO> getAllProductsWithSortByTitle(String sortType) {
        return List.of();
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

    private FakeStoreProductDTO convertGenericProductDTOToFakeStoreProductDTO (GenericProductDTO genericProductDTO) {
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();

        fakeStoreProductDTO.setId(Long.valueOf(genericProductDTO.getId()));
        fakeStoreProductDTO.setTitle(genericProductDTO.getTitle());
        fakeStoreProductDTO.setDescription(genericProductDTO.getDescription());
        fakeStoreProductDTO.setCategory(genericProductDTO.getCategory_name());
        fakeStoreProductDTO.setImage(genericProductDTO.getImage());
        fakeStoreProductDTO.setPrice(genericProductDTO.getPriceVal());

        return fakeStoreProductDTO;
    }
}
