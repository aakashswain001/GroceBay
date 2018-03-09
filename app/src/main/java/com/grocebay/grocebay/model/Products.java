package com.grocebay.grocebay.model;

import com.grocebay.grocebay.R;

/**
 * Created by aakas on 3/8/2018.
 */

public class Products {

    String id,name,description,price,category_id;//image_url;
    int image_url,count;
    public Products() {
        count = 0;
    }

    public Products(String id, String name, String description, String price, String category_id, int image_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.image_url = image_url;
        count = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = R.mipmap.food1;
    }
}
