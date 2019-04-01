package com.dlong.rep.dl10sidebar.bean;

import java.io.Serializable;
import java.util.Comparator;

public class AreaPhoneBean implements Serializable {
    public String name;         //地区名称
    public String name_py;      //地区名称拼音
    public String fisrtSpell;   //地区名称首字母
    public String code;         //地区代码
    public String locale;       //地区时区
    public String en_name;      //英文名


    /**
     * 按拼音进行排序
     */
    public static class ComparatorPY implements Comparator<AreaPhoneBean> {
        @Override
        public int compare(AreaPhoneBean lhs, AreaPhoneBean rhs) {
            String str1 = lhs.name_py;
            String str2 = rhs.name_py;
            return str1.compareToIgnoreCase(str2);
        }
    }
}
