package com.xiaobao.stp.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.xiaobao.stp.mybatisplus.convert.MysqlTypeConvertSupport;
import com.xiaobao.stp.mybatisplus.freemarker.FreemarkerUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 庞亚彬
 * @since 2021-08-17 17:14
 */
@Slf4j
public class AutoGeneratorPlus extends AutoGenerator {

    /**
     * 该方案会无法使用户使用自己的 convert
     * 回调
     * @see #execute() 方法之前进行调用, 进行默认的 TypeConvert 配置
     */
    private void initTypeConvert() {
        getDataSource().setTypeConvert(new MysqlTypeConvertSupport(getPackageInfo().getParent()
                , getGlobalConfig().getAuthor()));
    }

    @Override
    public void execute() {
        // 设置默认的 TypeConvert
        initTypeConvert();

        super.execute();

        log.debug("==========================准备生成枚举、JsonBo...==========================");

        MysqlTypeConvertSupport typeConvert = (MysqlTypeConvertSupport) getDataSource().getTypeConvert();
        String path = getGlobalConfig().getOutputDir() + "/" +
                getPackageInfo().getParent().replaceAll("[.]", "/");

        // 生成相应的枚举
        FreemarkerUtils.writeFile(typeConvert.getEnumList(), path + "/enums", "enum.java.ftl");
        // 生成相应的 bo
        FreemarkerUtils.writeFile(typeConvert.getJsonList(), path + "/bo", "jsonBo.java.ftl");

        log.debug("==========================枚举、JsonBo生成完成！！！==========================");
    }

}
