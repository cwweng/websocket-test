package net.cwweng.websocket.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    @Autowired
    private ChannelGroup group;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);
            //ctx.channel().writeAndFlush(new TextWebSocketFrame(request));
            group.writeAndFlush(new TextWebSocketFrame(request));

        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //ctx.channel().writeAndFlush(new TextWebSocketFrame("channel established"));
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, event);
        }
    }
}
