package com.xiaobao.stp.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
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

        log.debug("==========================准备生成文件...==========================");
        // 初始化配置
        if (null == config) {
            config = new ConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getTemplate(), getGlobalConfig());
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }

        if (null == getTemplateEngine()) {
            // 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
            setTemplateEngine(new VelocityTemplateEngine());
        }

        // 模板引擎初始化执行文件输出
        getTemplateEngine().init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();

        MysqlTypeConvertSupport typeConvert = (MysqlTypeConvertSupport) getDataSource().getTypeConvert();

        String path = getGlobalConfig().getOutputDir() + "/" +
                getPackageInfo().getParent().replaceAll("[.]", "/");

        // 生成相应的枚举
        FreemarkerUtils.writeFile(typeConvert.getEnumList(), path + "/enums", "enum.java.ftl");
        // 生成相应的 bo
        FreemarkerUtils.writeFile(typeConvert.getJsonList(), path + "/bo", "jsonBo.java.ftl");

        log.debug("==========================文件生成完成！！！==========================");
    }

}
