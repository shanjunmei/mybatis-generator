package com.lanhun.framework.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.lanhun.framework.generator.core.Configure;
import com.lanhun.framework.generator.core.DataProcessor;
import com.lanhun.framework.generator.model.Table;
import com.lanhun.framework.generator.utils.BeanUtils;

@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
public class Generator {

	protected Configure config;

	public Generator() {
		config = new Configure();
	}

	public Generator(Configure config) {
		this.config = config;
	}

	public void generate(Table table) {
		generateModel(table);
		generateExample(table);
		generateMapper(table);
		generateXml(table);
	}

	public Writer createWriter(String path) {
		path = config.getTargetDir() + path;
		File file = new File(path);
		String dir = file.getParent();
		File pd = new File(dir);
		if (!pd.exists()) {
			pd.mkdirs();
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// StringWriter writer=new StringWriter();
		return writer;
	}

	private VelocityEngine createVelocityEngine() {
		Properties props = new Properties();
		props.setProperty("resource.loader", "class");
		props.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// properties.setProperty("input.encoding", "UTF-8");
		// properties.setProperty("output.encoding", "UTF-8");
		props.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		props.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		VelocityEngine velocityEngine = new VelocityEngine(props);
		return velocityEngine;
	}

	public void generateModel(Table table) {
		VelocityEngine velocityEngine = createVelocityEngine();
		VelocityContext context = createContext(table);
		Writer writer = createWriter(config.getModelPackage().replace(".", "/")
				+ "/" + table.getBeanName() + ".java");
		velocityEngine.mergeTemplate(config.templeteBase + "beanTemplate.vm",
				context, writer);
		flushWriter(writer);
	}

	private void flushWriter(Writer writer) {
		try {
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generateExample(Table table) {
		VelocityEngine velocityEngine = createVelocityEngine();
		VelocityContext context = createContext(table);
		Writer writer = createWriter(config.getExamplePackage().replace(".",
				"/")
				+ "/" + table.getBeanName() + "Example.java");
		velocityEngine.mergeTemplate(
				config.templeteBase + "exampleTemplate.vm", context, writer);
		flushWriter(writer);
	}

	public void generateMapper(Table table) {
		VelocityEngine velocityEngine = createVelocityEngine();

		VelocityContext context = createContext(table);

		Writer writer = createWriter(config.getMapperPackage()
				.replace(".", "/") + "/" + table.getBeanName() + "Mapper.java");
		velocityEngine.mergeTemplate(config.templeteBase
				+ "mapperTemplate.vm", context, writer);
		flushWriter(writer);

	}

	public void generateXml(Table table) {
		VelocityEngine velocityEngine = createVelocityEngine();
		VelocityContext context = createContext(table);
		Writer writer = createWriter(config.getMapperPackage()
				.replace(".", "/") + "/" + table.getBeanName() + "Mapper.xml");
		velocityEngine.mergeTemplate(config.templeteBase + "xmlTemplate.vm",
				context, writer);
		flushWriter(writer);

	}

	private VelocityContext createContext(Table table) {
		Map map = BeanUtils.getValueMap(table);
		Map configMap = BeanUtils.getValueMap(config);
		configMap.put("author", System.getProperty("user.name"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		configMap.put("dateTime", sdf.format(new Date()));
		map.putAll(configMap);
		VelocityContext context = new VelocityContext(map);

		return context;
	}

	public static void main(String[] args) {
		Configure config = new Configure();
		config.setTargetDir("d:/hz/generator/");
		config.setModelPackage("com.lanhun.domain.model");
		config.setExamplePackage("com.lanhun.domain.example");
		config.setMapperPackage("com.lanhun.domain.mapper");
		config.setPrimaryKey("id");
		Generator generator = new Generator(config);

		String tableNamePattern = "%";
		DataProcessor t = new DataProcessor();

		List<Table> tableInfos = t.getTableInfos(tableNamePattern);

		try {
			for (Table table : tableInfos) {
				generator.generate(table);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
