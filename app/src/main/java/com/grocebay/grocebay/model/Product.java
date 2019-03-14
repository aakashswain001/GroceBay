package com.grocebay.grocebay.model;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name, description, category_id, price, vegtype, image;
    int count;

    public Product() {
    }

    public Product(int id, String name, String description, String category_id, String price, String vegtype, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
        this.price = price;
        this.vegtype = vegtype;
        this.image = image;
        this.count = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVegtype() {
        return vegtype;
    }

    public void setVegtype(String vegtype) {
        this.vegtype = vegtype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
