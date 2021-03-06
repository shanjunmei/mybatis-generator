package com.lanhun.framework.generator.model;

public class Column {

	private String lowerProperty;

	private String property;

	private String column;

	private String type;

	private String jdbcType;

	private String remark;

	private int dataType;
	

	public String getLowerProperty() {
		return lowerProperty;
	}

	public void setLowerProperty(String lowerProperty) {
		this.lowerProperty = lowerProperty;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}


	
}
