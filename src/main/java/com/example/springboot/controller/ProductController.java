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
        return ResponseEntity.status(HttpStatus.OK).body(productRepositories.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> product0 = productRepositories.findById(id);
        if(product0.isEmpty()){//
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(product0.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());

    }
}
