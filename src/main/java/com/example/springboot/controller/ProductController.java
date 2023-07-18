package com.example.springboot.controller;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepositories;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepositories productRepositories;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);//fazendo a converção de proproductRecordDto para productModel
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepositories.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProduct(){//METODO vai RETONAR UMA LIST DO TIPO ProductModel
        List<ProductModel> productList = productRepositories.findAll();
        if(!productList.isEmpty()){
            for(ProductModel product : productList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);

    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> product0 = productRepositories.findById(id);
        if(product0.isEmpty()){//esse product0 esta vazio ser:
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(product0.get());
        }//ser não:
        product0.get().add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    @PutMapping("/products/{id}")
    public  ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                 @RequestBody @Valid ProductRecordDto productRecordDto){
        Optional<ProductModel> product0 = productRepositories.findById(id);
        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var productModel = product0.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return  ResponseEntity.status(HttpStatus.OK).body(productRepositories.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> product0 = productRepositories.findById(id);
        if(product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found");
        }
        productRepositories.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deletd successfully");

    }
}
