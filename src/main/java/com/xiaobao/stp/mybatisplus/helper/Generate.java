package com.xiaobao.stp.mybatisplus.helper;

/**
 * @author 庞亚彬
 * @email yabin.pang@xiaobao100.com
 * @since 2021-09-03 14:07
 */
public interface Generate {

    default String getEnumSuffix() {
        return null;
    }

    default String getJsonBoSuffix(){
        return null;
    }

    default String getEnumPackage() {
        return null;
    }

    default String getJsonBoPackage() {
        return null;
    }

    void setEnumSuffix(String suffix);

    void setJsonBoSuffix(String suffix);

    void setEnumPackage(String enumPackage);

    void setJsonBoPackage(String jsonBoPackage);

}
