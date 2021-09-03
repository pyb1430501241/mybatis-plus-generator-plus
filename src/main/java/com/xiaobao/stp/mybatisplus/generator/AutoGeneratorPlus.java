package com.xiaobao.stp.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.google.common.base.CaseFormat;
import com.xiaobao.stp.mybatisplus.helper.Generate;
import com.xiaobao.stp.mybatisplus.helper.GenerateHelper;
import com.xiaobao.stp.mybatisplus.convert.MysqlTypeConvertSupport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 * @author 庞亚彬
 * @since 2021-08-17 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class AutoGeneratorPlus extends AutoGenerator implements Generate {

    private MysqlTypeConvertSupport mysqlTypeConvertSupport;

    public AutoGeneratorPlus() {
        this.mysqlTypeConvertSupport = new MysqlTypeConvertSupport();
        this.mysqlTypeConvertSupport.setGenerateHelper(new GenerateHelper());
    }

    /**
     * 必须优先设置 TypeConvert, 如果后设置可能会导致配置失效
     * 回调
     * @see #execute() 方法之前进行调用, 进行默认的 TypeConvert 配置
     */
    private void initTypeConvert() {
        this.mysqlTypeConvertSupport.setAuthor(getGlobalConfig().getAuthor());
        this.mysqlTypeConvertSupport.setParentName(getPackageInfo().getParent());
        getDataSource().setTypeConvert(this.mysqlTypeConvertSupport);
    }

    /**
     * 设置枚举类型生成时后缀
     * @param suffix 后缀名, 会自动转驼峰
     */
    public void setEnumSuffix(@NonNull String suffix) {
        suffix = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, suffix);
        this.mysqlTypeConvertSupport.getGenerateHelper().setEnumSuffix(suffix.trim());
    }

    /**
     * 设置 jsonBo 生成时的后缀
     * @param suffix 后缀名, 会自动转驼峰
     */
    public void setJsonBoSuffix(@NonNull String suffix) {
        suffix = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, suffix);
        this.mysqlTypeConvertSupport.getGenerateHelper().setJsonBoSuffix(suffix.trim());
    }

    /**
     * 设置生成的枚举包名
     * @param enumPackage 枚举包名, 不要带 `.`
     */
    public void setEnumPackage(@NonNull String enumPackage) {
        this.mysqlTypeConvertSupport.getGenerateHelper().setEnumPackage(enumPackage.trim().toLowerCase());
    }

    /**
     * 设置生成的 jsonBo 包名
     * @param jsonBoPackage jsonBo 包名, 不要带 `.`
     */
    public void setJsonBoPackage(@NonNull String jsonBoPackage) {
        this.mysqlTypeConvertSupport.getGenerateHelper().setJsonBoPackage(jsonBoPackage.trim().toLowerCase());
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

        typeConvert.getGenerateHelper().batchOutput(path);

        log.debug("==========================枚举、JsonBo生成完成！！！==========================");
    }

}
