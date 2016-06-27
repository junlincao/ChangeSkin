package com.zhy.changeskin.attr;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;

import com.zhy.changeskin.constant.SkinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 15/9/23.
 */
public class SkinAttrSupport {
    //Android命名空间
    final static String ANDROID_NAME_SPACE = "http://schemas.android.com/apk/res/android";

    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {
        List<SkinAttr> skinAttrs = new ArrayList<SkinAttr>();
        SkinAttr skinAttr = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            // 非Android命名空间的就不要解析属性了，反正也不支持。。。
            if (!ANDROID_NAME_SPACE.equals(((XmlResourceParser) attrs).getAttributeNamespace(i))) {
                continue;
            }

            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            SkinAttrType attrType = getSupportAttrType(attrName);
            if (attrType == null)
                continue;

            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                if (id == 0) {
                    continue;
                }
                String entryName = context.getResources().getResourceEntryName(id);

                if (entryName.startsWith(SkinConfig.ATTR_PREFIX)) {
                    skinAttr = new SkinAttr(attrType, entryName);
                    skinAttrs.add(skinAttr);
                }
            }
        }
        return skinAttrs;

    }

    private static SkinAttrType getSupportAttrType(String attrName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getAttrType().equals(attrName))
                return attrType;
        }
        return null;
    }

}
