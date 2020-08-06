package com.fast.fastrpc.transporter;

import com.fast.fastrpc.ChannelHandler;
import com.fast.fastrpc.Codec;
import com.fast.fastrpc.Peer;
import com.fast.fastrpc.common.Constants;
import com.fast.fastrpc.common.URL;
import com.fast.fastrpc.common.logger.Logger;
import com.fast.fastrpc.common.logger.LoggerFactory;
import com.fast.fastrpc.common.spi.ExtensionLoader;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yiji
 * @version : AbstractPeer.java, v 0.1 2020-08-04
 */
public abstract class AbstractPeer implements Peer, ChannelHandler {

    public static final String CODEC_KEY = "codec";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AtomicBoolean closed = new AtomicBoolean();

    protected Codec codec;

    protected int shutdownTimeout;

    private URL url;
    private ChannelHandler handler;

    public AbstractPeer(URL url, ChannelHandler handler) {
        this.url = url;
        this.handler = handler;
        this.shutdownTimeout = url.getParameter(Constants.SHUTDOWN_KEY, 0);
        this.codec = getProtocolCodec();
    }

    public Codec getCodec() {
        return codec;
    }

    protected Codec getProtocolCodec() {
        String codec = url.getParameter(CODEC_KEY, url.getProtocol());
        return ExtensionLoader.getExtensionLoader(Codec.class).getExtension(url.getParameter(CODEC_KEY, codec));
    }

    @Override
    public URL getUrl() {
        return this.url;
    }
}
