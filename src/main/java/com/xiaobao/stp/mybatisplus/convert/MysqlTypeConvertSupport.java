package com.xiaobao.stp.mybatisplus.convert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.google.common.base.CaseFormat;
import com.xiaobao.stp.mybatisplus.helper.GenerateHelper;
import com.xiaobao.stp.mybatisplus.constant.TemplateConstant;
import com.xiaobao.stp.mybatisplus.factory.enums.IColumnTypeFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

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
@NoArgsConstructor
public class MysqlTypeConvertSupport
        extends MySqlTypeConvert implements ITypeConvert {

    /**
     * 类型映射生成工厂
     */
    private static final IColumnTypeFactory I_COLUMN_TYPE_FACTORY = new IColumnTypeFactory();

    /**
     * .
     */
    private static final String POINT = ".";

    @Getter
    @Setter
    private GenerateHelper generateHelper;

    // 父包名
    @Setter
    private String parentName;

    // 作者名
    @Setter
    private String author;

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
        if(typeName.contains("tinyint")) {
            return getIColumnType(columnName, generateHelper.getEnumSuffix(),
                    generateHelper.getEnumList(), generateHelper.getEnumPackage());
        }

        // json 转实体类
        if(typeName.contains("json")) {
            return getIColumnType(columnName, generateHelper.getJsonBoSuffix(),
                    generateHelper.getJsonList(), generateHelper.getJsonBoPackage());
        }

        return processTypeConvert(globalConfig, tableField.getType());
    }

    /**
     * 通过表列名和后缀生成一个 IColumnType
     * @param columnName 列名
     * @param name 后缀
     * @param targetList 生成的代码的必要信息储存
     * @param packageName 生成代码的包名
     * @return
     *  IColumnType
     */
    @NonNull
    private IColumnType getIColumnType(@NonNull String columnName, @NonNull String name,
                                       @NonNull List<Map<String, String>> targetList, @NonNull String packageName) {
        //列名+相应后缀
        String useName = columnName + name;
        // 包全路径
        String packagePath = splicingString(parentName, POINT, packageName);

        // 添加生成目标信息
        addGeneratorTargetInfo(useName, packagePath, targetList);

        return I_COLUMN_TYPE_FACTORY.create(useName, splicingString(packagePath, POINT, useName));
    }

    /**
     * 添加需要生成的类的信息
     * @param typeName 类名
     * @param path 包的路径
     * @param targetList 生成什么类型
     * @see com.xiaobao.stp.mybatisplus.helper.GenerateHelper#getEnumList()
     * @see com.xiaobao.stp.mybatisplus.helper.GenerateHelper#getJsonList()
     */
    private void addGeneratorTargetInfo(@NonNull String typeName, @NonNull String path, List<Map<String, String>> targetList) {
        Map<String, String> templateMap = new HashMap<>();

        templateMap.put(TemplateConstant.DATE, nowDate());
        templateMap.put(TemplateConstant.AUTHOR, author);
        templateMap.put(TemplateConstant.NAME, typeName);
        templateMap.put(TemplateConstant.PACKAGE, path);

        targetList.add(templateMap);
    }

    @NonNull
    private static String splicingString(String ... args) {
        StringBuilder builder = new StringBuilder();
        Stream.of(args).forEach(builder :: append);
        return builder.toString();
    }

    @NonNull
    private static String nowDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

}
