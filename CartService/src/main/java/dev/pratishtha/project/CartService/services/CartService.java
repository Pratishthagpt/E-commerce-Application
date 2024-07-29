package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CartService {

    public List<GenericCartDTO> getAllCarts(String token);

    GenericCartDTO addNewCart(String token, GenericCartDTO requestDto);

    GenericCartDTO getCartById(String cartId);

    List<GenericCartDTO> getCartsByLimit(String token, int limit);

    List<GenericCartDTO> getCartsBySort(String sortType);

    List<GenericCartDTO> getCartsInDateRange(DateRangeDTO dateRangeDTO);

    List<GenericCartDTO> getSortedCartsInDateRangeWithLimit(DateRangeDTO dateRangeDTO, int limit, String sortType);

    List<GenericCartDTO> getCartsByUser(String userId);

    List<GenericCartDTO> getCartsByUserInDateRange(String userId, DateRangeDTO dateRangeDTO);

    GenericCartDTO updateCartById(String token, String id, GenericCartDTO requestDto);

    GenericCartDTO updateSubCartById(String token, String id, GenericCartDTO requestDto);

    GenericCartDTO deleteCartById(String token, String id);

    List<GenericCartDTO> getCartsBySortAndLimit(String sortType, int limit);

    List<GenericCartDTO> getCartsByUserByToken(String token);

    List<GenericCartDTO> getCartsByUserTokenInDateRange(String token, DateRangeDTO dateRangeDTO);
}
