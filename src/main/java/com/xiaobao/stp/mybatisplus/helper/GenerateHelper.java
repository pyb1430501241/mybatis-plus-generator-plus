package com.xiaobao.stp.mybatisplus.helper;

import com.xiaobao.stp.mybatisplus.constant.GenerateConstant;
import com.xiaobao.stp.mybatisplus.freemarker.FreemarkerUtils;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 庞亚彬
 * @since 2021-08-18 9:38
 */
@Data
public class GenerateHelper {

    /**
     * 记录需要生成的枚举类信息, 提供给代码生成器使用
     */
    private List<Map<String, String>> enumList;

    /**
     * 记录需要生成的 Json 对应 bo 的信息, 提供给代码生成器使用
     */
    private List<Map<String, String>> jsonList;

    private String enumSuffix;

    private String jsonBoSuffix;

    private String enumPackage;

    private String jsonBoPackage;

    public String getEnumSuffix() {
        if(enumSuffix == null) {
            enumSuffix = GenerateConstant.ENUM;
        }
        return enumSuffix;
    }

    public String getJsonBoSuffix() {
        if(jsonBoSuffix == null) {
            jsonBoSuffix = GenerateConstant.JSON_BO;
        }
        return jsonBoSuffix;
    }

    public String getEnumPackage() {
        if(enumPackage == null) {
            enumPackage = GenerateConstant.ENUM_PACKAGE;
        }
        return enumPackage;
    }

    public String getJsonBoPackage() {
        if(jsonBoPackage == null) {
            jsonBoPackage = GenerateConstant.JSON_BO_PACKAGE;
        }
        return jsonBoPackage;
    }

    public GenerateHelper() {
        this.enumList = new ArrayList<>();
        this.jsonList = new ArrayList<>();
    }

    public void batchOutput(String path) {
        // 生成相应的枚举
        FreemarkerUtils.writeFile(this.getEnumList(),
                getUsePath(path, this.getEnumPackage()), "enum.java.ftl");
        // 生成相应的 bo
        FreemarkerUtils.writeFile(this.getJsonList(),
                getUsePath(path, this.getJsonBoPackage()), "jsonBo.java.ftl");
    }

    private String getUsePath(String parentPath, String packageName) {
        return parentPath +
                "/" +
                packageName;
    }

}
