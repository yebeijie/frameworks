package edu.nf;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean转换工具,将查询结果集转换成Bean对象
 *
 * @author lenovo
 */
public class BeanUtil {

    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

    /**
     * 当数据库取出null值时,给基本数据类型默认值
     */
    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }

    /**
     * 创建Bean实例
     *
     * @param rs
     * @param type
     * @return Object
     */
    public static Object createBean(ResultSet rs, Class<?> type) throws SQLException {
        try {
            //创建实体的实例
            Object bean = type.newInstance();
            //获取结果集的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //根据查询的总列数进行遍历
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //获取列名
                String columnName = metaData.getColumnLabel(i);
                //根据列名将值通过反射赋值给实体的属性
                setField(columnName, type, bean, rs);
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Bean赋值操作，将column的值映射到具体的Bean属性中
     *
     * @param columnName
     * @throws Exception
     */
    private static void setField(String columnName, Class<?> type, Object bean,
                                    ResultSet rs) throws Exception {
        //获取所有的属性
        Field[] fields = type.getDeclaredFields();
        //循环遍历
        for (Field field : fields) {
            //打开访问开关
            field.setAccessible(true);
            //获取相应的列名
            String fieldName = (field.isAnnotationPresent(Column.class)) ? field.getAnnotation(Column.class).value() : field.getName();
            //进行判断匹配，如果属性的名称和列名匹配，那么就进行赋值
            if(columnName.equalsIgnoreCase(fieldName)){
                Object value = ColumnUtil.columnConvert(rs, columnName, field.getType());
                //如果值为空，并且field是基本数据类型，则赋初始值
                if(value == null && field.getType().isPrimitive()){
                    value = primitiveDefaults.get(field.getType());
                }
                field.set(bean, value);
                break;
            }
        }
    }

}
