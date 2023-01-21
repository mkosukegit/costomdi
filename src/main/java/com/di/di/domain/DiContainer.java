package com.di.di.domain;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.di.di.domain.annotation.BeanCustom;
import com.di.di.domain.annotation.Inject;

import ch.qos.logback.core.Context;

public class DiContainer {

    static Map<String, Class> types = new HashMap<>();

    static Map<String, Object> beans = new HashMap<>();

    private static void register(String name, Class type) {

        types.put(name, type);
    }

    public static Object getBean(String name) {

        return beans.computeIfAbsent(name, key -> {
            Class type = types.get(name);
            Objects.requireNonNull(type, name + "not found.");

            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static <T> T createObject(Class<T> type) throws Exception {
        T object = type.getDeclaredConstructor().newInstance();

        for (Field field : type.getDeclaredFields()) {

            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            field.setAccessible(true);
            field.set(object, getBean(field.getName()));
        }

        return object;
    }

    /**
     * DIコンテナに登録
     */
    public static void autoRegister(Class mainClass) throws Exception {
        // Beanを取得する範囲はmainクラス配下とする
        URL res = mainClass.getResource(
                "/" + mainClass.getName().replace('.', '/') + ".class");
        Path classPath = new File(res.toURI()).toPath();

        Files.walk(classPath.getParent())
                // ファイルのみ取得
                .filter(p -> !Files.isDirectory(p))
                // .classファイルのみ取得
                .filter(p -> p.toString().endsWith(".class"))
                // 空を削除
                .filter(p -> !StringUtils.isEmpty(p.toString()))
                // 「/」を「.」へ変換
                .map(p -> p.toString().replace(File.separatorChar, '.'))
                .map(n -> n.substring(n.indexOf("com")))
                .map(n -> n.substring(0, n.length() - 6))
                .forEach(n -> {
                    try {
                        Class c = Class.forName(n);
                        if (c.isAnnotationPresent(BeanCustom.class)) {
                            String simpleName = c.getSimpleName();
                            register(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1), c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
