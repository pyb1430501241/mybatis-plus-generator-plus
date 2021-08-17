package com.xiaobao.stp.mybatisplus.convert;


import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.xiaobao.stp.mybatisplus.factory.enums.DbColumnTypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author 庞亚彬
 * @since 2021-08-17 13:51
 */
@AllArgsConstructor
public class MysqlTypeConvertSupport
        extends MySqlTypeConvert implements ITypeConvert {

    /**
     * 类型映射工厂
     */
    private static final DbColumnTypeFactory DB_COLUMN_TYPE_FACTORY = new DbColumnTypeFactory();

    /**
     * .
     */
    private static final String POINT = ".";

    /**
     * 枚举后缀
     */
    private static final String ENUM = "Enum";


    /**
     * JsonBo 后缀
     */
    private static final String JSON_BO = "JsonBo";

    @Getter
    private String parentName;

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, TableField tableField) {
        if(tableField.getType().contains("tinyint(1)")) {
            return DbColumnType.BOOLEAN;
        }

        // 非 tinyint(1) 转为枚举
        // 枚举类名为：字段名+Enum
        if(tableField.getType().contains("tinyint")) {
            String columnName = tableField.getName();
            // 第一个字符转大写
            columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            String enumName = columnName + ENUM;
            return DB_COLUMN_TYPE_FACTORY.create(enumName.toUpperCase(), enumName
                    , splicingString(parentName, POINT, enumName));
        }

        return processTypeConvert(globalConfig, tableField.getType());
    }

    private static String splicingString(String ... args) {
        StringBuilder builder = new StringBuilder();
        Stream.of(args).forEach(builder :: append);
        return builder.toString();
    }

}
