package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
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

    private GenericProductDTO convertFakeStoreProductDTOToGenericProductDTO (FakeStoreProductDTO fakeStoreProductDTO) {
        GenericProductDTO genericProductDTO = new GenericProductDTO();

        genericProductDTO.setId(fakeStoreProductDTO.getId().toString());
        genericProductDTO.setTitle(fakeStoreProductDTO.getTitle());
        genericProductDTO.setDescription(fakeStoreProductDTO.getDescription());
        genericProductDTO.setCategory_name(fakeStoreProductDTO.getCategory_name());
        genericProductDTO.setImage(fakeStoreProductDTO.getImage());
        genericProductDTO.setPriceVal(fakeStoreProductDTO.getPriceVal());

        return genericProductDTO;
    }
}
