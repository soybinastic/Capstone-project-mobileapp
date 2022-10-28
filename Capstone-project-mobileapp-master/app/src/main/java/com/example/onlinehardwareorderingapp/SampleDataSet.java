package com.example.onlinehardwareorderingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlinehardwareorderingapp.models.BranchModel;
import com.example.onlinehardwareorderingapp.models.CategoryModel;
import com.example.onlinehardwareorderingapp.models.ProductModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SampleDataSet {
    public static ArrayList<ProductModel> getProducts() {
        ArrayList<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel("Door Knob", 300.50));
        products.add(new ProductModel("Nails 1kg", 50.50));
        products.add(new ProductModel("Electrical Tape", 60.50));
        products.add(new ProductModel("Boysen paint", 350.50));
        products.add(new ProductModel("Cable Wire", 350.50));
        products.add(new ProductModel("Door Knob", 300.50));
        products.add(new ProductModel("Nails 1kg", 50.50));
        products.add(new ProductModel("Electrical Tape", 60.50));
        products.add(new ProductModel("Boysen paint", 350.50));
        products.add(new ProductModel("Extension Wire", 350.50));
        products.add(new ProductModel("Door Knob", 300.50));
        products.add(new ProductModel("Nails 1kg", 50.50));
        products.add(new ProductModel("Electrical Tape", 60.50));
        products.add(new ProductModel("Boysen paint", 350.50));
        products.add(new ProductModel("Cable Wire", 350.50));
        products.add(new ProductModel("Door Knob", 300.50));
        products.add(new ProductModel("Nails 1kg", 50.50));
        products.add(new ProductModel("Electrical Tape", 60.50));
        products.add(new ProductModel("Boysen paint", 350.50));
        products.add(new ProductModel("Extension Wire", 350.50));
        return products;
    }
    public static ArrayList<CategoryModel> getProductCategories(){
        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel(1,1,"Steel"));
        categories.add(new CategoryModel(1,1,"Paint"));
        categories.add(new CategoryModel(1,1,"Electrical Equipment"));
        categories.add(new CategoryModel(1,1,"Tools"));
        categories.add(new CategoryModel(1,1,"Roofs"));
        categories.add(new CategoryModel(1,1,"Doors"));
        categories.add(new CategoryModel(1,1,"Cements"));
        categories.add(new CategoryModel(1,1,"Tiles"));
        categories.add(new CategoryModel(1,1,"Woods"));
        categories.add(new CategoryModel(1,1,"Shovels"));

        return  categories;
    }

    public static ArrayList<BranchModel> getBranches(){
        ArrayList<BranchModel> branches = new ArrayList<>();
        branches.add(new BranchModel(1, "Citi Hardware Branch A", "Carmen Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Quandro Ocho Branch A", "Bulua Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Citi Hardware Branch B", "Nazareth Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Micabani Hardware", "Balulang Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Quadro Ocho Hardware Branch B", "Carmen Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Mucan Hardware Branch A", "Cogon Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Mucan Hardware Branch B", "Bulua Cagayan de Oro City"));
        branches.add(new BranchModel(1, "Citi Hardware Branch C", "Taguanao Cagayan de Oro City"));
        branches.add(new BranchModel(1, "EXB Hardware Branch A", "Cogon Cagayan de Oro City"));

        return branches;
    }
}
