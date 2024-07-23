package com.skawuma.service;

import com.skawuma.entity.Product;
import com.skawuma.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@CacheConfig(cacheNames = "products")//configuring cache globally to avoid repetitive calls on each method
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @PostConstruct
    public void initDB(){
      List<Product>  products = IntStream.rangeClosed(1,1000)
                .mapToObj(i-> new Product("product"+i,new Random().nextInt(5000),
                "desc"+i,"type"+i))
                        .collect(Collectors.toList());
      repository.saveAll(products);
    }
    @CachePut(key = "#product.id")
    public Product saveProduct(Product product) {

        return repository.save(product);
    }
    @Cacheable
    public List<Product> getProducts() {


        return repository.findAll();
    }
    @CachePut(key = "#id")
    public Product getProductById(int id) {

        return repository.findById(id).get();
    }

    public Product getProductByName(String name) {

        return repository.findByName(name);
    }

    public List<Product> getProductsByType(String productType) {
        return repository.findByProductType(productType);
    }

    public List<Product> getProductWithPriceAndType(double price, String productType) {
        return repository.findByPriceAndProductType(price, productType);
    }

    public List<Product> getProductsByPrice(double price) {
        return repository.getProductByPrice(price);
    }
    @CachePut(key = "#id")
    public Product updateProduct(int id, Product productRequest) {
        // get the product from DB by id
        // update with new value getting from request
        Product existingProduct = repository.findById(id).get(); // DB
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setProductType(existingProduct.getProductType());
        return repository.save(existingProduct);
    }
@CacheEvict(key = "#id")
    public long deleteProduct(int id) {
        repository.deleteById(id);
        return repository.count();
    }

    //OPERATOR

    public List<Product> getProductsByMultiplePriceValue(List<Double> prices) {
        return repository.findByPriceIn(prices);
    }

    public List<Product> getProductsWithinPriceRange(double minPrice, double maxPrice) {
        return repository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getProductsWithHigherPrice(double price) {
        return repository.findByPriceGreaterThan(price);
    }

    public List<Product> getProductsWithLessPrice(double price) {
        return repository.findByPriceLessThan(price);
    }

    public List<Product> getProductsWithLike(String name) {
        return repository.findByNameIgnoreCaseContaining(name);
    }

    //sorting
    public List<Product> getProductsWithSorting(String fieldName) {
        return repository.findAll(Sort.by(Sort.Direction.ASC, fieldName));
    }

    //pagination
    public Page<Product> getProductsWithPageResponse(int offset, int limit) {
        return repository.findAll(PageRequest.of(offset, limit));
    }

    public Page<Product> getProductsWithSortingAndPagination(String fieldName, int offset, int limit) {
        return repository
                .findAll(
                        PageRequest.of(offset, limit)
                                .withSort(Sort.by(fieldName)
                                ));
    }


}
