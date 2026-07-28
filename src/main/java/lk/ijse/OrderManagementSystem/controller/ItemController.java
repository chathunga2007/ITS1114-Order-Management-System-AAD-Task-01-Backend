package lk.ijse.OrderManagementSystem.controller;

import lk.ijse.OrderManagementSystem.constant.CommonResponse;
import lk.ijse.OrderManagementSystem.dto.ItemDTO;
import lk.ijse.OrderManagementSystem.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static lk.ijse.OrderManagementSystem.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.OrderManagementSystem.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping(value = "api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveItem(@RequestBody ItemDTO itemDTO) {
        itemService.saveItem(itemDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return new CommonResponse(OPERATION_SUCCESS, items, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterItems(@RequestParam(value = "itemName", required = false) String itemName) {
        List<ItemDTO> filterItemDTOList = itemService.filterItems(itemName);
        return new CommonResponse(OPERATION_SUCCESS, filterItemDTOList, SUCCESS_MESSAGE);
    }

    @GetMapping (value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getItemDetail(@PathVariable long itemId) {
        ItemDTO itemDTO = itemService.getItemDetail(itemId);
        return new CommonResponse(OPERATION_SUCCESS, itemDTO, SUCCESS_MESSAGE);
    }

    @PutMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateItem(@RequestBody ItemDTO itemDTO) {
        itemService.updateItem(itemDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteItem(@PathVariable long itemId) {
        itemService.deleteItem(itemId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
}