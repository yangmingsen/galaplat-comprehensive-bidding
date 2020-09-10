package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 发送短信和邮件结结果状态
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/7 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAndEmialDVO {

    /*短信发送状态（发送失败-0； 发送成功-1； 部分发送成功-2）*/
   private int messageSendStatus;
   /*发送结果（发送失败-0； 发送成功-1； 部分发送成功-2）*/
   private int emailSeandStatus;

}
