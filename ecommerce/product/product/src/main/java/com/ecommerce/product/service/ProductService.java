package com.ecommerce.product.service;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.ProductDTO;
import com.ecommerce.product.exception.BussinessException;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(productDTO.getCategory())
                .rating(productDTO.getRating())
                .quantity(productDTO.getQuantity())
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();
        productRepository.save(product);
        return product;
    }

    public Product findProduct(Long id) throws BussinessException {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BussinessException("Product with id " + id + " does not exist");
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> findAllProductByCategory(String category, boolean inStock) {
        List<Product> productList = productRepository.findByCategoryLikeIgnoreCase(category);
        if (inStock) {
            productList = productList.stream().filter(Product::getInStock).toList();
        }
        return productList;
    }

    public Product updateQuantity(long productId, int quantity) throws BussinessException {
        Product product = findProduct(productId);
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }

    public Product updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findByName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        return productRepository.save(product);
    }

}
