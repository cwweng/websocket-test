package net.cwweng.websocket.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${ws.path}")
    private String wsPath = "/ws";
    @Autowired
    private TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();

        cp.addLast(new HttpServerCodec());
        cp.addLast(new HttpObjectAggregator(65536));
        cp.addLast(new WebSocketServerProtocolHandler(wsPath, null, true));
        cp.addLast(textWebSocketFrameHandler);
    }
}
