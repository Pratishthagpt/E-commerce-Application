package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    public List<GenericCartDTO> getAllCarts();
}
