package lk.ijse.OrderManagementSystem.service.impl;

import lk.ijse.OrderManagementSystem.dto.FilterOrderDTO;
import lk.ijse.OrderManagementSystem.dto.ItemDTO;
import lk.ijse.OrderManagementSystem.dto.PlaceOrderDTO;
import lk.ijse.OrderManagementSystem.entity.Customer;
import lk.ijse.OrderManagementSystem.entity.Item;
import lk.ijse.OrderManagementSystem.entity.Order;
import lk.ijse.OrderManagementSystem.entity.OrderItem;
import lk.ijse.OrderManagementSystem.exception.CustomException;
import lk.ijse.OrderManagementSystem.repository.CustomerRepository;
import lk.ijse.OrderManagementSystem.repository.ItemRepository;
import lk.ijse.OrderManagementSystem.repository.OrderItemRepository;
import lk.ijse.OrderManagementSystem.repository.OrderRepository;
import lk.ijse.OrderManagementSystem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            ItemRepository itemRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void placeOrder(PlaceOrderDTO placeOrderDTO) {
        log.info("Placing order");
        if (placeOrderDTO == null) {
            throw new CustomException(400, "Place order data cannot be null!");
        }
        if (placeOrderDTO.getCustomerId() <= 0) {
            throw new CustomException(400, "Customer ID is required to place an order!");
        }
        if (placeOrderDTO.getItemIdList() == null || placeOrderDTO.getItemIdList().isEmpty()) {
            throw new CustomException(400, "Order must contain at least one item!");
        }

        Order order = new Order();
        order.setTotal(placeOrderDTO.getTotal());
        order.setOrderDate(new java.util.Date());

        Optional<Customer> optionalCustomer = customerRepository.findById(placeOrderDTO.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new CustomException(404, "Customer not found with id: " + placeOrderDTO.getCustomerId());
        }

        Customer customer = optionalCustomer.get();
        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);

        for (Long itemId : placeOrderDTO.getItemIdList()) {
            if (itemId == null || itemId <= 0) {
                throw new CustomException(400, "Invalid item ID in order!");
            }
            OrderItem orderItem = new OrderItem();

            Optional<Item> optionalItem = itemRepository.findById(itemId);
            if (optionalItem.isEmpty()) {
                throw new CustomException(404, "Item not found with id: " + itemId);
            }

            Item item = optionalItem.get();

            orderItem.setOrders(savedOrder);
            orderItem.setItem(item);
            orderItem.setOrderItemQTY(1);
            
            int price = 0;
            if (item.getItemPrice() != null) {
                try {
                    price = (int) Double.parseDouble(item.getItemPrice());
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse item price: {}", item.getItemPrice());
                }
            }
            orderItem.setOrderItemPrice(price);

            orderItemRepository.save(orderItem);
        }

        log.info("Order placed successfully");
    }

    @Override
    public List<FilterOrderDTO> filterOrders(String customerName) {
        log.info("Filtering orders");
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new CustomException(400, "Customer name parameter cannot be empty!");
        }

        List<FilterOrderDTO> filterOrderDTOS = new ArrayList<>();
        List<Order> orderList = orderRepository.filterOrders(customerName);

        if (orderList.isEmpty()) {
            throw new CustomException(404, "No orders found for customer: " + customerName);
        }

        for (Order order : orderList) {
            FilterOrderDTO filterOrderDTO = new FilterOrderDTO();

            filterOrderDTO.setOrderId(order.getOrderId());
            filterOrderDTO.setCustomerName(order.getCustomer().getCustomerName());

            List<ItemDTO> itemDTOList = new ArrayList<>();
            List<OrderItem> orderItemList = order.getOrderItems();

            for (OrderItem orderItem : orderItemList) {
                Item item = orderItem.getItem();
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItemId(item.getItemId());
                itemDTO.setItemName(item.getItemName());
                itemDTO.setItemQTY(item.getItemQTY());
                itemDTO.setItemPrice(item.getItemPrice());

                itemDTOList.add(itemDTO);
            }

            filterOrderDTO.setItemList(itemDTOList);
            filterOrderDTOS.add(filterOrderDTO);
        }
        return filterOrderDTOS;
    }

    @Override
    public List<FilterOrderDTO> getOrdersByCustomerId(Long customerId) {
        log.info("Retrieving orders for customer: {}", customerId);
        if (customerId == null || customerId <= 0) {
            throw new CustomException(400, "Customer ID is required!");
        }

        List<FilterOrderDTO> filterOrderDTOS = new ArrayList<>();
        List<Order> orderList = orderRepository.getOrdersByCustomerId(customerId);

        if (orderList.isEmpty()) {
            throw new CustomException(404, "No orders found for customer id: " + customerId);
        }

        for (Order order : orderList) {
            FilterOrderDTO filterOrderDTO = new FilterOrderDTO();

            filterOrderDTO.setOrderId(order.getOrderId());
            filterOrderDTO.setCustomerName(order.getCustomer().getCustomerName());

            List<ItemDTO> itemDTOList = new ArrayList<>();
            List<OrderItem> orderItemList = order.getOrderItems();

            for (OrderItem orderItem : orderItemList) {
                Item item = orderItem.getItem();
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItemId(item.getItemId());
                itemDTO.setItemName(item.getItemName());
                itemDTO.setItemQTY(item.getItemQTY());
                itemDTO.setItemPrice(item.getItemPrice());

                itemDTOList.add(itemDTO);
            }

            filterOrderDTO.setItemList(itemDTOList);
            filterOrderDTOS.add(filterOrderDTO);
        }
        return filterOrderDTOS;
    }
}