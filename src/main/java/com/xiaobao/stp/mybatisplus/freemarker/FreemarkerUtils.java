package com.xiaobao.stp.mybatisplus.freemarker;

import com.xiaobao.stp.mybatisplus.constant.TemplateConstant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author 庞亚彬
 * @create 2021-08-17 21:10
 */
@Slf4j
public abstract class FreemarkerUtils {

    private static final String TEMPLATE_PATH = "src/main/resources/templates";

    public static void writeFile(List<Map<String, String>> templateList, String path, String templateName) {
        Configuration configuration = new Configuration();
        try {
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            Template template = configuration.getTemplate(templateName);

            templateList.forEach(map -> {
                try {
                    Writer out = null;

                    String name = map.get(TemplateConstant.NAME);

                    // 先创建文件夹
                    File parent = new File(path);
                    if(!parent.exists()) {
                        parent.mkdirs();
                    }

                    File docFile = new File(path + "/" + name + ".java");
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));

                    log.debug("模板:{}, 文件:{}", template.getName(), docFile.getPath());

                    template.process(map, out);
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
