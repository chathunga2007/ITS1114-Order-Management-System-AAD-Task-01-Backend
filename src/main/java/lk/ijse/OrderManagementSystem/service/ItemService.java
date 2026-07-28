package lk.ijse.OrderManagementSystem.service;

import lk.ijse.OrderManagementSystem.dto.ItemDTO;
import java.util.List;

public interface ItemService {
    void saveItem(ItemDTO item);
    List<ItemDTO> getAllItems();
    ItemDTO getItemDetail(long id);
    void updateItem(ItemDTO itemDTO);
    List<ItemDTO> filterItems(String itemName);
    void deleteItem(long itemId);
}