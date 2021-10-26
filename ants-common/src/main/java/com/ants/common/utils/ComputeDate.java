package com.ants.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * TODO
 * Author Chen
 * Date   2021/10/26 17:51
 */
public class ComputeDate {

    private static Calendar calS = Calendar.getInstance();

    private static Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");//定义整则表达式

    /**
     * 计算剩余时间
     *
     * @param startDateStr yyyy-MM-dd
     * @param endDateStr   yyyy-MM-dd
     * @return ？年？个月？天
     */

    public static String remainDateToString(String startDateStr, String endDateStr) {
        java.util.Date startDate = null;

        java.util.Date endDate = null;

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);

        } catch (ParseException e) {
            e.printStackTrace();

            return "";

        }

        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);

        } catch (ParseException e) {
            e.printStackTrace();

            return "";

        }

        calS.setTime(startDate);

        int startY = calS.get(Calendar.YEAR);

        int startM = calS.get(Calendar.MONTH);

        int startD = calS.get(Calendar.DATE);

        int startDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

        calS.setTime(endDate);

        int endY = calS.get(Calendar.YEAR);

        int endM = calS.get(Calendar.MONTH);

//处理2011-01-10到2011-01-10，认为服务为一天

        int endD = calS.get(Calendar.DATE) + 1;

        int endDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

        StringBuilder sBuilder = new StringBuilder();

        if (endDate.compareTo(startDate) < 0) {
            return sBuilder.append("过期").toString();

        }

        int lday = endD - startD;

        if (lday < 0) {
            endM = endM - 1;

            lday = startDayOfMonth + lday;

        }

//处理天数问题，如：2011-01-01 到2013-12-31 2年11个月31天 实际上就是3年

        if (lday == endDayOfMonth) {
            endM = endM + 1;

            lday = 0;

        }

        int mos = (endY - startY) * 12 + (endM - startM);

        int lyear = mos / 12;

        int lmonth = mos % 12;

        if (lyear > 0) {
            sBuilder.append(lyear + "年");

        }

        if (lmonth > 0) {
            sBuilder.append(lmonth + "个月");

        }

// if(lyear==0)//满足项目需求 满一年不显示天数

        if (lday > 0) {
            sBuilder.append(lday + "天");

        }

        return sBuilder.toString();

    }

    public static void main(String[] args) {
        String s = remainDateToString("2019-09-09", "2021-04-09");
        System.out.println(s);
    }
}
