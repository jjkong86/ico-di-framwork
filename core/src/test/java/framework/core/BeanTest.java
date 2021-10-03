package framework.core;

import framework.core.annotation.Autowired;
import framework.core.annotation.Component;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class BeanTest {

    @Test
    public void ioc_di_test() {
        Scanner.getInstance(); // 빈등록을 위해서 기동이 필요함
        Map<String, Object> beans = Container.getBeans();
        beans.forEach((bean, instance) -> {
            System.out.println("bean name : " + bean);
            Arrays.stream(instance.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                try {
                    System.out.println("bean name : " + bean + ", field bean : " + field.get(instance));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            Arrays.stream(instance.getClass().getDeclaredMethods()).forEach(method -> {
                try {
                    System.out.println("method invoke, bean name : " + bean);
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
    }


    @Component
    public static class Bean1 {

    }

    @Component
    public static class Bean2 {

        @Autowired
        private Bean1 bean1;
    }
}
