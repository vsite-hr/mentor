package hr.vsite.mentor.servlet.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

import javax.ws.rs.core.StreamingOutput;

public class NioMediaStreamer implements StreamingOutput {

    public NioMediaStreamer(Path path, long from, long to) throws IOException {
    	this.path = path;
    	this.from = from;
        this.length = to - from + 1;
    }

    @Override
    public void write(OutputStream ostream) throws IOException {
		try (
			FileChannel inChannel = FileChannel.open(path);
			WritableByteChannel outChannel = Channels.newChannel(ostream);
		) {
			long transferred;
			long transferredTotal = 0;
			while (transferredTotal < length && (transferred = inChannel.transferTo(from + transferredTotal, 2 << 20, outChannel)) > 0)
				transferredTotal += transferred;
    	}
    }

    public long getLenth() {
        return length;
    }
   
    private Path path;
	private long from;
	private long length;

}
