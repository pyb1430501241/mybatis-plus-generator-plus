package com.xiaobao.stp.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.xiaobao.stp.mybatisplus.convert.MysqlTypeConvertSupport;

/**
 * @author 庞亚彬
 * @since 2021-08-17 17:14
 */
public class AutoGeneratorPlus extends AutoGenerator {

    @Override
    public AutoGenerator setPackageInfo(PackageConfig packageInfo) {
        AutoGenerator generator = super.setPackageInfo(packageInfo);

        getDataSource().setTypeConvert(new MysqlTypeConvertSupport(packageInfo.getParent()));

        return generator;
    }

}
