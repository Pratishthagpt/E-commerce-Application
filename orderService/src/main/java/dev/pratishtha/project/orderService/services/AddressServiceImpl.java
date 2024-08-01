package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.exceptions.AddressIdNotFoundException;
import dev.pratishtha.project.orderService.exceptions.AddressNotFoundException;
import dev.pratishtha.project.orderService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.orderService.exceptions.UnAuthorizedUserAccessException;
import dev.pratishtha.project.orderService.models.Address;
import dev.pratishtha.project.orderService.repositories.AddressRepository;
import dev.pratishtha.project.orderService.security.JwtData;
import dev.pratishtha.project.orderService.security.TokenValidator;
import dev.pratishtha.project.orderService.security.UserDto;
import dev.pratishtha.project.orderService.security.UserRole;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;
    private TokenValidator tokenValidator;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, TokenValidator tokenValidator) {
        this.addressRepository = addressRepository;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public List<AddressDTO> getAllAddresses(String token) {

        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<UserRole> userRoles = userData.getRoles();

//        Only user with role of "ADMIN" can see all addresses of all users
        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                List<Address> addresses = addressRepository.findAll();

                List<AddressDTO> addressDTOS = new ArrayList<>();

                for (Address address : addresses) {
                    AddressDTO addressDTO = convertAddressToAddressDto(address);
                    addressDTOS.add(addressDTO);
                }
                return addressDTOS;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to access the addresses.");
    }

    @Override
    public AddressDTO addNewAddress(String token, AddressDTO requestDto) {

        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        Address address = convertAddressDtoToAddress(requestDto);
        address.setUserId(userId);

        Address savesAddress = addressRepository.save(address);

        AddressDTO addressResponse = convertAddressToAddressDto(savesAddress);

        return addressResponse;
    }

    @Override
    public AddressDTO getAddressById(String token, String id) {
        JwtData userData = validateUserByToken(token);

        UUID uuid = UUID.fromString(id);

        List<UserRole> userRoles = userData.getRoles();

//        Only user with role of "ADMIN" can see all addresses of all users
        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                Optional<Address> addressOptional = addressRepository.findById(uuid);

                if (addressOptional.isEmpty()) {
                    throw new AddressIdNotFoundException("Address with id - " + id + " not found.");
                }

                Address address = addressOptional.get();

                AddressDTO addressDTO = convertAddressToAddressDto(address);
                return addressDTO;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to access the addresses.");

    }

    @Override
    public List<AddressDTO> getAddressByUser(String token) {

        JwtData userData =  validateUserByToken(token);
        String userId = userData.getUserId();

        List<Address> addresses = addressRepository.findAllByUserId(userId);

        if (addresses.size() == 0) {
            throw new AddressNotFoundException("There are no addresses added for user - " + userData.getUsername());
        }

        List<AddressDTO> addressDTOS = new ArrayList<>();

        for (Address address : addresses) {
            AddressDTO addressDTO = convertAddressToAddressDto(address);
            addressDTOS.add(addressDTO);
        }

        return addressDTOS;
    }


    private Address convertAddressDtoToAddress (AddressDTO addressDTO) {
        Address address = new Address();

        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setCountry(addressDTO.getCountry());
        address.setUserId(addressDTO.getUserId());
        address.setHouseNo(addressDTO.getHouseNo());

        return address;
    }

    private AddressDTO convertAddressToAddressDto (Address address) {
        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setAddressId(String.valueOf(address.getUuid()));
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPincode(address.getPincode());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setUserId(address.getUserId());
        addressDTO.setHouseNo(address.getHouseNo());

        return addressDTO;
    }

    public JwtData validateUserByToken (String token) {
        Optional<JwtData> userData = tokenValidator.validateToken(token);
        if (userData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        JwtData userJwtData = userData.get();

        return userJwtData;
    }

}
