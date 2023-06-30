package org.yearup.data.mysql;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class MySqlCategoryDaoTest extends BaseDaoTestClass{
    private MySqlCategoryDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlCategoryDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectCategory()
    {
        //arrange
        int productId = 2;

        Category expected = new Category()
        {{
            setCategoryId(2);
            setDescription("Discover trendy clothing and accessories for men and women");
            setName("Fashion");
        }};

        //act
        var actual = dao.getById(productId);

        //assert
        assertEquals(expected.getName(), actual.getName(),"Did this work?");


    }
}