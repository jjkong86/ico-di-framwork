package framework.core;


import framework.core.annotation.Autowired;
import framework.core.annotation.Component;
import framework.core.support.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

public class Scanner {
    private static final Scanner scanner = new Scanner();

    public static Scanner getInstance() {
        return scanner;
    }

    private Scanner() {
        URL resource = Scanner.class.getResource("/");
        assert resource != null;
        List<File> list = FileUtils.getFiles(resource.getPath());
        for (File file : list) {
            this.registerBean(this.getClassPath(file, resource));
        }
    }

    public String getClassPath(File file, URL resource) {
        return file.getAbsolutePath()
                .replace(resource.getPath(), "") // root path 제거
                .replace(".class", "") // .class 확장자 제거
                .replace("/", "."); // / -> . 으로 변경
    }

    public void registerBean(String classPath) {
        try {
            Class<?> cls = Class.forName(classPath);
            if (cls.isAnnotationPresent(Component.class)) {
                this.getBean(cls);
                this.beanInjection(cls);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(Class<?> cls) {
        return Container.getBeans().computeIfAbsent(cls.getName(), key -> {
            try {
                return cls.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void beanInjection(Class<?> cls) {
        try {
            Object bean = Container.getBeans().get(cls.getName());
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Class<?> classType = field.getType();
                    Object fieldBean = this.getBean(classType);
                    field.set(bean, fieldBean);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
