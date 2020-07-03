package com.galaplat.comprehensive.bidding.netty;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.pojo.RequestMessage;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class BidHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用来保存所有的客户端连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM");

    AdminChannelMap adminChannelMap = SpringUtil.getBean(AdminChannelMap.class);;
    UserChannelMap userChannelMapBean = SpringUtil.getBean(UserChannelMap.class);

    // 当Channel中有新的事件消息会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 当接收到数据后会自动调用

        // 获取客户端发送过来的文本消息
        String text = msg.text();
        System.out.println("接收到消息数据为：" + text);

        RequestMessage message = JSON.parseObject(text, RequestMessage.class);

        // 通过SpringUtil工具类获取Spring上下文容器
        //ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);



        switch (message.getType()) {
            // 建立供应商客户端连接的消息
            case 101: {
                String userCode = message.getData().get("userCode");
                userChannelMapBean.put(userCode, ctx.channel());
                System.out.println("建立用户:" + userCode + "与通道" + ctx.channel().id() + "的关联");
                userChannelMapBean.print();
            }
            break;

            // 建立管理员客户端连接的消息
            case 102: {
                String adminCode = message.getData().get("adminCode");
                String focusActivity = "";
                AdminInfo adminInfo = new AdminInfo(focusActivity,ctx.channel());
                adminChannelMap.put(adminCode, adminInfo);
                System.out.println("建立（admin）用户:" + adminCode + "与通道" + ctx.channel().id() + "的关联");
                adminChannelMap.print();
            }
             break;

                //处理管理端主动请求
            case 300: {
                //handler300problem();
                String activityCode = message.getData().get("activityCode");
                String adminId = adminChannelMap.getAdminIdByChannelId(ctx.channel().id());
                AdminInfo tAdminInfo1 = adminChannelMap.get(adminId);
                tAdminInfo1.setFocusActivity(activityCode); //设置当前管理员聚焦的活动

                //处理返回数据
                Message tmsg = new Message(300, handler300Problem());
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(tmsg)));
            }
            break;
        }

    }

    private Res300 handler300Problem() {
        return null;
    }


    // 当有新的客户端连接服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将新的通道加入到clients
        clients.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭通道");
        //UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        //UserChannelMap.print();
    }
}