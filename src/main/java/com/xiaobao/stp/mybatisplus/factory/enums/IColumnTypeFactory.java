package com.xiaobao.stp.mybatisplus.factory.enums;

import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import org.springframework.lang.NonNull;

/**
 * @author 庞亚彬
 * @since 2021-08-16 13:07
 * <p>仅适用于 jdk1.8</p>
 */
public class IColumnTypeFactory extends AbstractEnumTypeFactory<DbColumnType> {

    @Override
    public DbColumnType apply(@NonNull Class<?> targetClass) {
        return create(targetClass.getSimpleName().toUpperCase()
                , targetClass.getSimpleName(), targetClass.getName());
    }

    @Override
    public DbColumnType create(@NonNull String enumName, Object ... args) {
        return getEnumByClass(DbColumnType.class, enumName, args);
    }

    @NonNull
    public IColumnType create(@NonNull final String type, @NonNull final String packageName) {
        return new IColumnType() {
            @Override
            public String getType() {
                return type;
            }

            @Override
            public String getPkg() {
                return packageName;
            }
        };
    }

}
