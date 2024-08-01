package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {

    public List<AddressDTO> getAllAddresses(String token);

    public AddressDTO addNewAddress(String token, AddressDTO requestDto);

    AddressDTO getAddressById(String token, String id);

    List<AddressDTO> getAddressByUser(String token);

    AddressDTO updateAddressByUser(String token, AddressDTO requestDto);
}
