package com.example.spring.batch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long comapnyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "description")
    private String description;

    @Column(name = "website")
    private String website;

    @Column(name = "contact")
    private String contact;

    @Column(name = "date")
    private String date;

    @Column(name = "price")
    private String price;

    @Column(name = "percentage")
    private String percentage;

    @Column(name = "discounted_price")
    private double discounted_price;

}
