package com.xiaobao.stp.mybatisplus.factory.enums;

import com.xiaobao.stp.mybatisplus.factory.EnumTypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import sun.reflect.ConstructorAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 一个枚举类型通用的工厂
 * 继承并实现 apply 方法
 * @author 庞亚彬
 * @since 2021-08-16 13:05
 */
@Slf4j
public abstract class AbstractEnumTypeFactory<E, T extends Enum<?>>
        implements EnumTypeFactory<E, T> {

    /**
     * 该类及其不安全, 在 1.8 版本中提供给外部使用, 更高版本 jdk 中不对外使用, 需添加
     * 运行时参数 --add-exports java.base/jdk.internal.reflect=ALL-UNNAMED 才可使用
     * jdk 1.8 中为：
     * @see sun.reflect.ConstructorAccessor
     * @see sun.reflect.ReflectionFactory
     * 9及以上版本为：
     * @see jdk.internal.reflect.ConstructorAccessor
     * @see jdk.internal.reflect.ReflectionFactory
     */
    @SuppressWarnings("all")
    private static final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory.getReflectionFactory();

    /**
     * 获取构造器存取器
     * @param clazz 需要获取构造器的类
     * @param classList 参数集合
     * @return
     *  对应的构造器存取器
     * @throws NoSuchMethodException
     *  没有找到构造器
     */
    @NonNull
    private ConstructorAccessor getConstructor(@NonNull Class<T> clazz, @NonNull List<Class<?>> classList) throws NoSuchMethodException {
        Class<?> [] valueTypes = new Class<?>[classList.size() + 2];
        valueTypes[0] = String.class;
        valueTypes[1] = int.class;

        for (int i = 0; i < classList.size(); i++) {
            valueTypes[i + 2] = classList.get(i);
        }

        Constructor<T> constructor = clazz.getDeclaredConstructor(valueTypes);
        constructor.setAccessible(true);

        return REFLECTION_FACTORY.newConstructorAccessor(constructor);
    }

    /**
     * 创建一个枚举对象
     * @param clazz 需要创建枚举对象的枚举类
     * @param name 枚举成员的名字
     * @param index 枚举成员的索引
     * @param paramTypes 枚举的构造器参数类型
     * @param args 枚举的构造器参数
     * @return
     *  一个名为 <code>name</code> 的 <code>clazz</code> 类的枚举成员
     * @throws NoSuchMethodException
     *  没找到构造器
     * @throws java.lang.reflect.InvocationTargetException
     *  构造器调用错误
     * @throws InstantiationException
     *  构造器调用错误
     */
    @NonNull
    private T createEnum(@NonNull Class<T> clazz, @NonNull String name, @NonNull Integer index
            , @NonNull List<Class<?>> paramTypes, Object [] args)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException {
        Object [] useArgs = new Object[args.length + 2];

        useArgs[0] = name;
        useArgs[1] = index;
        System.arraycopy(args, 0, useArgs, 2, args.length);

        return clazz.cast(getConstructor(clazz, paramTypes).newInstance(useArgs));
    }

    /**
     * 获取一个 <code>clazz</code> 类型的枚举对象
     * @param clazz 需要创建枚举对象的枚举类
     * @param enumName 枚举成员的名字
     * @param args 枚举的构造器参数
     * @return
     *   <p>一个名为 <code>enumName</code> 的 <code>clazz</code> 类的枚举成员
     *   <p>如果 <code>enumName</code> 在枚举成员中已存在, 则返回 <code>null</code></p>
     */
    @SuppressWarnings("unchecked")
    @Nullable
    protected T getEnumByClass(@NonNull Class<T> clazz, @NonNull String enumName, Object ... args) {
        if(!clazz.isEnum()) {
            throw new RuntimeException("该类型不是枚举类型");
        }

        Field valuesField = null;
        Field[] fields = clazz.getDeclaredFields();
        List<Class<?>> paramTypes = new ArrayList<>();

        for (Field field : fields) {
            if (field.isEnumConstant() && field.getName().equals(enumName)) {
                log.warn("该枚举成员已存在");
                return null;
            }

            // 是否为合成类型
            if(field.isSynthetic()) {
                valuesField = field;
            }

            if(!field.isSynthetic() && !field.isEnumConstant()) {
                paramTypes.add(field.getType());
            }
        }

        if(Objects.isNull(valuesField)) {
            throw new RuntimeException("未找到该类的合成类型");
        }

        AccessibleObject.setAccessible(new Field[] {valuesField}, true);

        try {
            T[] previousValues = (T[]) valuesField.get(clazz);

            return createEnum(clazz, enumName, previousValues.length, paramTypes, args);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
