package com.xiaobao.stp.mybatisplus.factory.enums;

import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

/**
 * @author 庞亚彬
 * @since 2021-08-16 13:07
 * <p>仅适用于 jdk1.8</p>
 */
public class DbColumnTypeFactory extends AbstractEnumTypeFactory<DbColumnType> {

    @Override
    public DbColumnType apply(Class<?> targetClass) {
        return create(targetClass.getSimpleName().toUpperCase()
                , targetClass.getSimpleName(), targetClass.getName());
    }

    @Override
    public DbColumnType create(String enumName, Object ... args) {
        return getEnumByClass(DbColumnType.class, enumName, args);
    }

}
