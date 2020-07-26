package learningtest.spring.embeddeddb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

public class EmbeddedDbTest {
    EmbeddedDatabase db;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("learningtest/spring/embeddeddb/schema.sql")
                .addScript("learningtest/spring/embeddeddb/data.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(db);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData() {
        Assert.assertEquals(jdbcTemplate.queryForObject("select count(*) from sqlmap", Integer.class).intValue(), 2);

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from sqlmap order by key_");
        Assert.assertEquals(list.get(0).get("key_"), "KEY1");
        Assert.assertEquals(list.get(0).get("sql_"), "SQL1");
        Assert.assertEquals(list.get(1).get("key_"), "KEY2");
        Assert.assertEquals(list.get(1).get("sql_"), "SQL2");

    }

    @Test
    public void insert() {
        jdbcTemplate.update("insert into sqlmap(key_, sql_) values(?,?)", "KEY3", "SQL3");

        Assert.assertEquals(jdbcTemplate.queryForObject("select count(*) from sqlmap", Integer.class).intValue(), 3);
    }


}
