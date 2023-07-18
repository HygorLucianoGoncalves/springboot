package com.example.springboot.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@EqualsAndHashCode(of = "UUID")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_products")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID idProduct;
    private String name;
    private BigDecimal valor;
}
