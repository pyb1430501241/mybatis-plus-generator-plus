package com.xiaobao.stp.mybatisplus.factory;

import java.util.function.Function;

/**
 * @author 庞亚彬
 * @since 2021-08-16 13:04
 */
public interface TypeFactory<T> extends Function<Class<?>, T> {

}
