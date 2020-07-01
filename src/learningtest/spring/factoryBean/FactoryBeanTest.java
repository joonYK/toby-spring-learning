package learningtest.spring.factoryBean;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        //bean 이름 앞에 "&"를 붙이면 팩토리 빈 자체를 가져올 수 있음.
        //Object message = context.getBean("&message");

        Object message = context.getBean("message");
        Assert.assertEquals(message.getClass(), Message.class);
        Assert.assertEquals(((Message)message).getText(), "Factory Bean");
    }
}
