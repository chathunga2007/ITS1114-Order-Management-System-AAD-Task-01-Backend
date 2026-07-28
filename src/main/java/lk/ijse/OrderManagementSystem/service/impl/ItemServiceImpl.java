package lk.ijse.OrderManagementSystem.service.impl;

import lk.ijse.OrderManagementSystem.dto.ItemDTO;
import lk.ijse.OrderManagementSystem.entity.Item;
import lk.ijse.OrderManagementSystem.exception.CustomException;
import lk.ijse.OrderManagementSystem.repository.ItemRepository;
import lk.ijse.OrderManagementSystem.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void saveItem(ItemDTO item) {
        log.info("Execute Method saveItem");
        if (item == null) {
            throw new CustomException(400, "Item data cannot be null!");
        }
        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            throw new CustomException(400, "Item name cannot be empty!");
        }
        if (item.getItemQTY() == null || item.getItemQTY().trim().isEmpty()) {
            throw new CustomException(400, "Item quantity cannot be empty!");
        }
        if (item.getItemPrice() == null || item.getItemPrice().trim().isEmpty()) {
            throw new CustomException(400, "Item price cannot be empty!");
        }

        Item item1 = new Item();
        item1.setItemName(item.getItemName());
        item1.setItemQTY(item.getItemQTY());
        item1.setItemPrice(item.getItemPrice());

        itemRepository.save(item1);
    }

    @Override
    public List<ItemDTO> getAllItems() {
        log.info("Execute Method getAllItems");
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = itemRepository.findAll();

        if (itemList.isEmpty()) {
            throw new CustomException(404, "No items found!");
        }

        for (Item item : itemList) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemQTY(item.getItemQTY());
            itemDTO.setItemPrice(item.getItemPrice());

            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    @Override
    public ItemDTO getItemDetail(long id) {
        log.info("Execute Method getItemDetail");
        if (id <= 0) {
            throw new CustomException(400, "Invalid item ID: " + id);
        }
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new CustomException(404, "Item not found with id: " + id);
        }
        Item item = itemOptional.get();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(item.getItemId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setItemQTY(item.getItemQTY());
        itemDTO.setItemPrice(item.getItemPrice());

        return itemDTO;
    }

    @Override
    public void updateItem(ItemDTO itemDTO) {
        log.info("Execute Method updateItem");
        if (itemDTO == null || itemDTO.getItemId() <= 0) {
            throw new CustomException(400, "Invalid item ID!");
        }
        if (itemDTO.getItemName() == null || itemDTO.getItemName().trim().isEmpty()) {
            throw new CustomException(400, "Item name cannot be empty!");
        }
        if (itemDTO.getItemQTY() == null || itemDTO.getItemQTY().trim().isEmpty()) {
            throw new CustomException(400, "Item quantity cannot be empty!");
        }
        if (itemDTO.getItemPrice() == null || itemDTO.getItemPrice().trim().isEmpty()) {
            throw new CustomException(400, "Item price cannot be empty!");
        }

        Optional<Item> itemOptional = itemRepository.findById(itemDTO.getItemId());
        if (itemOptional.isEmpty()) {
            throw new CustomException(404, "Item not found with id: " + itemDTO.getItemId());
        }
        Item item = itemOptional.get();
        item.setItemName(itemDTO.getItemName());
        item.setItemQTY(itemDTO.getItemQTY());
        item.setItemPrice(itemDTO.getItemPrice());

        itemRepository.save(item);
    }

    @Override
    public List<ItemDTO> filterItems(String itemName) {
        log.info("Execute Method filterItems");
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new CustomException(400, "Search query item name cannot be empty!");
        }

        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = itemRepository.filterItems(itemName);

        if (itemList.isEmpty()) {
            throw new CustomException(404, "No items found matching: " + itemName);
        }

        for (Item item : itemList) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemQTY(item.getItemQTY());
            itemDTO.setItemPrice(item.getItemPrice());

            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    @Override
    public void deleteItem(long itemId) {
        log.info("Execute Method deleteItem");
        if (itemId <= 0) {
            throw new CustomException(400, "Invalid item ID: " + itemId);
        }
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            log.error("Item with id {} does not exist", itemId);
            throw new CustomException(404, "Item not found with id: " + itemId);
        }
        itemRepository.deleteById(itemId);
        log.info("Item deleted successfully with id: {}", itemId);
    }
}