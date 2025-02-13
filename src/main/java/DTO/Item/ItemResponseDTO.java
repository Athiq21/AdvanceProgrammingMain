package DTO.Item;

import model.Item;

import java.util.List;

public interface ItemResponseDTO {
        Item save(Item item);
        Item getById(Long id);
        List<Item> getAll();
        Item update(Item item);
        boolean delete(Long id);
}
