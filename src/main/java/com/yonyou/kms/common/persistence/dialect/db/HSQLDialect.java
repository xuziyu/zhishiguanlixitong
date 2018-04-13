/**
 * 
 */
package com.yonyou.kms.common.persistence.dialect.db;

import com.yonyou.kms.common.persistence.dialect.Dialect;

/**
 * Dialect for HSQLDB
 *
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 涓嬪崍12:31
 * @since JDK 1.5
 */
public class HSQLDialect implements Dialect {
    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset),
                Integer.toString(limit));
    }

    /**
     * 灏唖ql鍙樻垚鍒嗛〉sql璇彞,鎻愪緵灏唎ffset鍙妉imit浣跨敤鍗犱綅绗﹀彿(placeholder)鏇挎崲.
     * <pre>
     * 濡俶ysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 灏嗚繑鍥�
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               瀹為檯SQL璇彞
     * @param offset            鍒嗛〉寮�濮嬬邯褰曟潯鏁�
     * @param offsetPlaceholder 鍒嗛〉寮�濮嬬邯褰曟潯鏁帮紞鍗犱綅绗﹀彿
     * @param limitPlaceholder  鍒嗛〉绾綍鏉℃暟鍗犱綅绗﹀彿
     * @return 鍖呭惈鍗犱綅绗︾殑鍒嗛〉sql
     */
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        boolean hasOffset = offset > 0;
        return
                new StringBuffer(sql.length() + 10)
                        .append(sql)
                        .insert(sql.toLowerCase().indexOf("select") + 6, hasOffset ? " limit " + offsetPlaceholder + " " + limitPlaceholder : " top " + limitPlaceholder)
                        .toString();
    }

}
