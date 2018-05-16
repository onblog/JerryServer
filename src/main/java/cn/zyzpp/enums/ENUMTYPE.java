package cn.zyzpp.enums;

/**
 * Create by yster@foxmail.com 2018-05-04
**/
public enum ENUMTYPE {
	HTML("html", "text/html"),
	CSS("css", "text/css"),
	JS("js", "application/x-javascript"),
	PNG("png", "image/png"),
	JPG("jpg", "image/jpeg"),
	GIF("gif", "image/gif"),
	ICO("ico", "image/ico");
	
	private String name;
	private String type;

	
    private ENUMTYPE(String name, String type) {
		this.setName(name);
		this.setType(type);
	}

	/** 
     * 根据类型的名称，返回类型的枚举实例。 
     * 
     * @param typeName 类型名称 
     */  
    public static ENUMTYPE getType(String typeName) {  
        for (ENUMTYPE type : ENUMTYPE.values()) {  
            if (type.getName().equalsIgnoreCase(typeName)) {  //不区分大小写
                return type;  
            }  
        }  
        return null;  
    } 

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
