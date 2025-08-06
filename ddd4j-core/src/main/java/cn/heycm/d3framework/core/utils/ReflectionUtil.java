package cn.heycm.d3framework.core.utils;

import org.springframework.core.ResolvableType;

/**
 * 反射工具类
 * 提供基于Spring的反射相关工具方法，主要用于处理AOP代理对象的泛型类型解析
 * @author heycm
 * @version 1.0
 * @since 2025/8/6 21:48
 */
public class ReflectionUtil {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private ReflectionUtil() {
    }

    /**
     * 获取指定类指定索引的泛型类型
     * @param clazz 类
     * @param index 泛型参数索引
     * @return 泛型参数类型
     */
    public static Class<?> getClassGenericType(Class<?> clazz, int index) {
        // 使用 Spring 的 ResolvableType 解析类的泛型类型
        ResolvableType resolvableType = ResolvableType.forClass(clazz);
        // 获取类的泛型数组
        ResolvableType[] generics = resolvableType.getGenerics();

        // 索引位置检查
        if (index < 0 || index >= generics.length) {
            throw new IndexOutOfBoundsException(StrKit.format("索引 {} 超出泛型参数范围, 该类共有 {} 个泛型参数", index, generics.length));
        }

        // 解析指定位置的泛型参数
        Class<?> resolve = generics[index].resolve();
        if (resolve == null) {
            throw new IllegalArgumentException(StrKit.format("无法解析类 {} 的第 {} 个泛型参数", clazz.getName(), index));
        }
        return resolve;
    }

    /**
     * 获取指定对象泛型参数的 Class
     * @param object 对象
     * @param index  泛型参数索引
     * @return 泛型参数类型
     */
    public static Class<?> getObjectGenericType(Object object, int index) {
        return getClassGenericType(object.getClass(), index);
    }

    /**
     * 获取指定类继承的父类或实现接口的泛型类型
     * @param clazz      当前类
     * @param superclass 父类或接口
     * @param index      泛型参数索引
     * @return 泛型参数类型
     */
    public static Class<?> getSuperclassGenericType(Class<?> clazz, Class<?> superclass, int index) {
        // 使用 Spring 的 ResolvableType 将当前类视为目标类来解析泛型
        ResolvableType resolvableType = ResolvableType.forClass(clazz).as(superclass);
        if (resolvableType == ResolvableType.NONE) {
            throw new IllegalArgumentException(StrKit.format("无法解析类型: {} 为 {}", clazz.getName(), superclass.getName()));
        }

        // 解析指定索引位置的泛型参数
        Class<?> resolve = resolvableType.getGeneric(index).resolve();
        if (resolve == null) {
            throw new IllegalArgumentException(StrKit.format("无法解析 {} 的第 {} 个泛型参数", clazz.getName(), index));
        }
        return resolve;
    }

    /**
     * 获取指定对象类型继承的父类或实现接口的泛型类型
     * @param object     对象
     * @param superclass 父类或接口
     * @param index      泛型参数索引
     * @return 泛型参数类型
     */
    public static Class<?> getObjectSuperclassGenericType(Object object, Class<?> superclass, int index) {
        return getSuperclassGenericType(object.getClass(), superclass, index);
    }
}

