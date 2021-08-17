package com.xiaobao.stp.mybatisplus.convert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.google.common.base.CaseFormat;
import com.xiaobao.stp.mybatisplus.constant.TemplateConstant;
import com.xiaobao.stp.mybatisplus.factory.enums.DbColumnTypeFactory;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * 数据类型映射
 * 记录需要生成的枚举或json的信息
 *
 * @author 庞亚彬
 * @since 2021-08-17 13:51
 */
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

    /**
     * 不应该记录在这里
     * 记录需要生成的枚举类信息, 提供给代码生成器使用
     */
    @Getter
    private List<Map<String, String>> enumList;

    /**
     * 不应该记录在这里
     * 记录需要生成的 Json 对应 bo 的信息, 提供给代码生成器使用
     */
    @Getter
    private List<Map<String, String>> jsonList;

    // 父包名
    private String parentName;

    // 作者名
    private String author;

    public MysqlTypeConvertSupport(String parentName, String author) {
        this.author = author;
        this.parentName = parentName;
        this.enumList = new ArrayList<>();
        this.jsonList = new ArrayList<>();
    }

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, TableField tableField) {
        String typeName = tableField.getType().toLowerCase();

        if(typeName.contains("tinyint(1)")) {
            return DbColumnType.BOOLEAN;
        }

        if(typeName.contains("datetime")) {
            return DbColumnType.DATE;
        }

        String columnName = tableField.getName();
        // 驼峰
        columnName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName);

        // 非 tinyint(1) 转为枚举
        // 枚举类名为：字段名+Enum
        if(typeName.contains("tinyint")) {
            return getIColumnType(columnName, ENUM);
        }

        // json 转实体类
        // 类名为：字段名+JsonBo
        if(typeName.contains("json")) {
            return getIColumnType(columnName, JSON_BO);
        }

        return processTypeConvert(globalConfig, tableField.getType());
    }

    /**
     * 通过表列名和后缀生成一个 IColumnType
     * @param columnName 列名
     * @param name 后缀
     * @return
     *  名为：columnName + name 的枚举对象
     */
    private IColumnType getIColumnType(String columnName, String name) {
        //列名+相应后缀
        String useName = columnName + name;
        Map<String, String> templateMap = new HashMap<>();
        String path = parentName;

        templateMap.put(TemplateConstant.DATE, nowDate());
        templateMap.put(TemplateConstant.AUTHOR, author);

        // 如果是枚举
        if(ENUM.equals(name)) {
            templateMap.put(TemplateConstant.ENUM_NAME, useName);
            path += ".enums";
            templateMap.put(TemplateConstant.PACKAGE, path);
            enumList.add(templateMap);
        } else {
            templateMap.put(TemplateConstant.JSON_NAME, useName);
            path += ".bo";
            templateMap.put(TemplateConstant.PACKAGE, path);
            jsonList.add(templateMap);
        }

        return DB_COLUMN_TYPE_FACTORY.create(useName.toUpperCase(), useName
                , splicingString(path, POINT, useName));
    }

    private static String splicingString(String ... args) {
        StringBuilder builder = new StringBuilder();
        Stream.of(args).forEach(builder :: append);
        return builder.toString();
    }

    private static String nowDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

}
