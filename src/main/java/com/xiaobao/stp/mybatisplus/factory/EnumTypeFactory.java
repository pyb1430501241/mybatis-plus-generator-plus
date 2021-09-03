package com.xiaobao.stp.mybatisplus.factory;

/**
 * @author 庞亚彬
 * @since 2021-08-17 14:51
 * @param <E> 枚举父接口
 * @param <T> 枚举类
 */
public interface EnumTypeFactory<E, T extends Enum<?>> extends TypeFactory<T> {

    /**
     * 创建一个枚举对象
     * @param enumName 枚举名
     * @param args 枚举参数
     * @return
     *  枚举类型
     */
    T create(String enumName, Object... args);

    E create(String enumName, String packageName);

}
