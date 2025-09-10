# Orders with Customer and Product Details

## Overview
This document provides information about the implemented solution for retrieving orders with complete customer and product details. The solution enables joining the orders, customers, and products tables to get comprehensive order information in a single API call.

## Implementation Details

### Data Model Relationships
The solution leverages the following entity relationships:
- `OrderEntity` has a many-to-one relationship with `Customer`
- `OrderEntity` has a one-to-many relationship with `OrderItem`
- `OrderItem` has a many-to-one relationship with `Product`

### Components Implemented

1. **Data Transfer Object (DTO)**
   - Created `OrderDetailDto` with nested `OrderItemDetailDto` to represent the combined data
   - Includes order information, customer details, and product information for each order item
   - Provides calculated subtotals for each order item (quantity × price)

2. **Repository Query**
   - Enhanced `OrderEntityRepository` with a custom JPQL query using JOIN FETCH
   - The query eagerly loads all related entities (customers, order items, products) in a single database call
   - Prevents the N+1 query problem for better performance

3. **Service Method**
   - Implemented `getOrdersWithCustomerAndProductDetails()` in `OrderEntityService`
   - Maps entity data to the DTO structure with all required information
   - Handles error cases and provides appropriate responses

4. **API Endpoint**
   - Added a new GET endpoint at `/getOrdersWithDetails`
   - Returns a comprehensive view of all orders with their associated customer and product details

## How to Use

### API Request
```
GET /getOrdersWithDetails
```

### Example Response
```json
{
  "success": true,
  "message": "Orders with details retrieved successfully",
  "data": [
    {
      "orderId": 1,
      "orderDate": "2025-09-10T10:30:00",
      "customerId": 101,
      "customerName": "Jane Smith",
      "customerEmail": "jane.smith@example.com",
      "items": [
        {
          "orderItemId": 501,
          "quantity": 2,
          "productId": 201,
          "productName": "Smartphone X",
          "productPrice": 599.99,
          "subtotal": 1199.98
        },
        {
          "orderItemId": 502,
          "quantity": 1,
          "productId": 202,
          "productName": "Wireless Headphones",
          "productPrice": 89.99,
          "subtotal": 89.99
        }
      ]
    },
    {
      "orderId": 2,
      "orderDate": "2025-09-09T15:45:00",
      "customerId": 102,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "items": [
        {
          "orderItemId": 503,
          "quantity": 1,
          "productId": 203,
          "productName": "Laptop Pro",
          "productPrice": 1299.99,
          "subtotal": 1299.99
        }
      ]
    }
  ]
}
```

## Testing the Solution

1. Start the application
2. Use a REST client (Postman, cURL, etc.) to make a GET request to `/getOrdersWithDetails`
3. Verify that the response contains all orders with their associated customer and product details
4. Check that the calculated subtotals are correct (quantity × price)

## Benefits of the Solution

1. **Efficiency**: Gets all required data in a single API call
2. **Performance**: Uses JOIN FETCH to prevent N+1 query issues
3. **Completeness**: Provides all customer and product details for each order
4. **Calculated Fields**: Includes subtotals for each order item
5. **Maintainability**: Clean separation of concerns with appropriate DTOs, repository methods, and service logic