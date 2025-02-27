package services;

import model.Item;

import java.io.InputStream;
import java.util.List;

public interface ItemServiceImpl {
    Item saveItem(Item item);
//    Item getItem(Long id);
    List<Item>getItemsBySubcategory(int subcategoryId);
    List<Item> getAllItems();
//    Item updateItem(Item item);
//    boolean deleteItem(Long id);
}
