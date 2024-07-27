package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    public List<GenericCartDTO> getAllCarts();

    GenericCartDTO addNewCart(GenericCartDTO requestDto);

    GenericCartDTO getCartById(String cartId);

    List<GenericCartDTO> getCartsByLimit(int limit);

    List<GenericCartDTO> getCartsBySort(String sortType);

    List<GenericCartDTO> getCartsInDateRange(DateRangeDTO dateRangeDTO);

    List<GenericCartDTO> getSortedCartsInDateRangeWithLimit(DateRangeDTO dateRangeDTO, int limit, String sortType);

    List<GenericCartDTO> getCartsByUser(String userId);

    List<GenericCartDTO> getCartsByUserInDateRange(String userId, DateRangeDTO dateRangeDTO);

    GenericCartDTO updateCartById(String id, GenericCartDTO requestDto);

    GenericCartDTO updateSubCartById(String id, GenericCartDTO requestDto);

    GenericCartDTO deleteCartById(String id);
}
