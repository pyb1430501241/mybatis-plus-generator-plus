package com.xiaobao.stp.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.xiaobao.stp.mybatisplus.convert.MysqlTypeConvertSupport;

/**
 * @author 庞亚彬
 * @since 2021-08-17 17:14
 */
public class AutoGeneratorPlus extends AutoGenerator {

    /**
     * 回调类, 在执行
     * @see #execute() 方法之前进行调用, 进行默认的 TypeConvert 配置
     */
    private void initTypeConvert() {
        getDataSource().setTypeConvert(new MysqlTypeConvertSupport(getPackageInfo().getParent()));
    }

    @Override
    public void execute() {
        // 设置默认的 TypeConvert
        initTypeConvert();

        super.execute();
    }

}
