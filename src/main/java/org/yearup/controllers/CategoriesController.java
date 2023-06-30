package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller (DONE)
// add the annotation to make this controller the endpoint for the following url (DONE)
// http://localhost:8080/categories
// add annotation to allow cross site origin requests (DONE)
@RestController
@RequestMapping("/categories")
@CrossOrigin
//@PreAuthorize("permitAll()")
public class CategoriesController
{

    private CategoryDao categoryDao;
    private ProductDao productDao;



    @Autowired
    public CategoriesController(CategoryDao catDao, ProductDao proDao)  {
        this.categoryDao = catDao;
        this.productDao = proDao;
    }

    // add the appropriate annotation for a get action (DONE)
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        // find and return all categories(DONE)
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action (DONE)
    @GetMapping("/{categoryId}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int categoryId)
    {
        if(categoryDao.getById(categoryId)== null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Incorrect");
        }
        try {
            return categoryDao.getById(categoryId);
        }catch(HttpClientErrorException.NotFound e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Try Again!");
        }
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("/{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId (DONE)
        return productDao.listByCategoryId(categoryId);

    }

    // add annotation to call this method for a POST action (DONE)
    // add annotation to ensure that only an ADMIN can call this function (DONE)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category (DONE)
        return categoryDao.create(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int categoryId, @RequestBody Category category)
    {
        // update the category by id (DONE)
        categoryDao.update(categoryId,category);

    }

    // add annotation to call this method for a DELETE action - the url path must include the categoryId (DONE)
    // add annotation to ensure that only an ADMIN can call this function (DONE)
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int categoryId)
    {
        // delete the category by id (DONE)
        categoryDao.delete(categoryId);
    }
}