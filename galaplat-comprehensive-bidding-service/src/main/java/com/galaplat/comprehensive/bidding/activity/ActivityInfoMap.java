package com.galaplat.comprehensive.bidding.activity;

import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 不使用，因为如果临时添加竞标数据，无法实时更新
 *
 */
@Deprecated
@Component
public class ActivityInfoMap {
    private Map<String, JbxtActivityDVO> activityDVOMap = new HashMap<>();

    public JbxtActivityDVO get(String activityCode) {
        return this.activityDVOMap.get(activityCode);
    }

    public void put(String activityCode, JbxtActivityDVO value) {
        this.activityDVOMap.put(activityCode, value);
    }

}
