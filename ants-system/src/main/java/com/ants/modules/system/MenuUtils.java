package com.ants.modules.system;


import com.ants.modules.system.entity.SysPermission;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ChenShuang
 * @date: 2020/6/29 13:35
 * @Description: 递归菜单
 */

public class MenuUtils {

    public static List<SysPermission> getChildPerms(List<SysPermission> allMenuList) {
        List<SysPermission> rootList = new ArrayList<>();
        for (Iterator<SysPermission> lt = allMenuList.iterator(); lt.hasNext(); ) {
            SysPermission menu = lt.next();
            if (StringUtils.isBlank(menu.getParentId())) {
                rootList.add(menu);
                lt.remove();
            }
        }

        for (SysPermission menu : rootList) {
            menu.setChildren(getChild(allMenuList, menu.getId()));
        }
        return rootList;
    }

    public static List<SysPermission> getChild(List<SysPermission> allMenuList, String id) {
        List<SysPermission> childList = new ArrayList<>();

        for (Iterator<SysPermission> lt = allMenuList.iterator(); lt.hasNext(); ) {
            SysPermission menu = lt.next();
            if (menu.getParentId().equals(id)) {
                childList.add(menu);
                lt.remove();
            }
        }

        for (SysPermission menu : childList) {
            menu.setChildren(getChild(allMenuList, menu.getId()));
        }
        return childList;
    }
}
