package media;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import userInterface.ApplicationContextVariables;

public class AudioSenderReceiver extends Thread{

	 TargetDataLine line;
	 SourceDataLine selfLine;
	 byte[] data = null;
	 byte[] aecReceivedData=new byte[500];
	ByteBuffer byteBuffer=ByteBuffer.allocate(500);
	SocketChannel socketChannel;
	MediaConnector mediaConnector;
	ApplicationContextVariables applicationContextVariables;
	boolean isLoggedIn=false;
	boolean loginMessageSent=false;
	ByteBuffer loginMessageReturnBuffer=ByteBuffer.allocate(500);

	
	public AudioSenderReceiver(SocketChannel socketChannel,MediaConnector mediaConnector){
		this.mediaConnector=mediaConnector;
		this.socketChannel=socketChannel;
		try {
			initAudioLine();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		   while(socketChannel!=null){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			   
				
			   if(!mediaConnector.getApplicationContextVariables().isReadyToStartMedia()){
				   try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 continue;
			   }
			   
			   
			   if(!isLoggedIn){
				   try{
				   if(!loginMessageSent){
//					   System.out.println(new String(mediaConnector.getLoginMessage()));
						socketChannel.write(ByteBuffer.wrap(mediaConnector.getLoginMessage()));
					   loginMessageSent=true;
				   }
				   if(socketChannel.read(loginMessageReturnBuffer)>0){
					   loginMessageReturnBuffer.flip();
						Charset charset = Charset.forName("UTF-8");
						CharsetDecoder decoder = charset.newDecoder();
						CharBuffer charBuffer = decoder.decode(loginMessageReturnBuffer);
						isLoggedIn=charBuffer.toString().equals("OK");
				   }
				   continue;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			   }
			   

			   try { 
				   
					  int numBytesRead=line.read(data, 0, data.length);
						 
				      if(aecReceivedData!=null){
						   socketChannel.write(ByteBuffer.wrap(aecProcess(aecReceivedData,data)));
					   }else 
						   socketChannel.write(ByteBuffer.wrap(data));

				     
				   
				   byteBuffer.clear();

				   if(socketChannel.read(byteBuffer) > 0 && byteBuffer.position()>0) {
					   aecReceivedData=new byte[data.length];
	                  System.arraycopy(byteBuffer.array(), 0, aecReceivedData, 0, aecReceivedData.length);
	                    int numBytesRemaining = byteBuffer.position();
	                    while (numBytesRemaining > 0 ) {
	                        numBytesRemaining -= selfLine.write(byteBuffer.array(), 0, numBytesRemaining);
	                    }
				  }else {
					  aecReceivedData=null;
				  }


                 
                 
			   }catch(Exception e){
				   e.printStackTrace();
			   }

		   }

	
	}

	
	public void initAudioLine() throws LineUnavailableException{
	     float sampleRate = 8000;
	     int sampleSizeInBits = 8;
	     int channels = 1;
	     boolean signed = true;
	     boolean bigEndian = true;
	     AudioFormat  format =  new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	     DataLine.Info info = new DataLine.Info( TargetDataLine.class, format);
	     DataLine.Info selfInfo=new DataLine.Info( SourceDataLine.class, format);
	    line = (TargetDataLine) AudioSystem.getLine(info);
	     selfLine=(SourceDataLine)AudioSystem.getLine(selfInfo);
	     int frameSizeInBytes = format.getFrameSize();
	     int bufferLengthInFrames = line.getBufferSize() / 8;
	     int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
	     data = new byte[bufferLengthInBytes];
	     line.open(format);
	     line.start();
	     selfLine.open(format);
	     selfLine.start();
	}
	
	/*
	public  byte[] removeEcho(int iDelaySamples, float fDecay, byte[] aySamples) {
	    short[] m_awDelayBuffer = new short[iDelaySamples];
	    byte[] m_aySamples = new byte[aySamples.length];
	    float m_fDecay = (float) fDecay;
	    System.out.println("Removing echo");
	    int m_iDelayIndex = 0;

	    System.out.println("Sample length:\t" + aySamples.length);
	    for (int i = 0; i < aySamples.length; i += 2)
	    {
	      // update the sample
	      short wOldSample = getSample(aySamples, i);

	      // remove the echo
	      short wNewSample = (short) (wOldSample - fDecay * m_awDelayBuffer[m_iDelayIndex]);
	      setSample(m_aySamples, i, wNewSample);

	      // update the delay buffer
	      m_awDelayBuffer[m_iDelayIndex] = wNewSample;
	      m_iDelayIndex++;

	      if (m_iDelayIndex == m_awDelayBuffer.length)
	      {
	        m_iDelayIndex = 0;
	      }
	    }

	    return m_aySamples;
	  }
	  
	  
	  
	  public static byte[] removeEcho(int iDelaySamples, float fDecay, byte[] aySamples)
  {
    m_awDelayBuffer = new short[iDelaySamples];
    m_aySamples = new byte[aySamples.length];
    m_fDecay = (float) fDecay;
    System.out.println("Removing echo");
    m_iDelayIndex = 0;

    System.out.println("Sample length:\t" + aySamples.length);
    for (int i = 0; i < aySamples.length; i += 2)
    {
      // update the sample
      short wOldSample = getSample(aySamples, i);

      // remove the echo
      short wNewSample = (short) (wOldSample - fDecay * m_awDelayBuffer[m_iDelayIndex]);
      setSample(m_aySamples, i, wNewSample);

      // update the delay buffer
      m_awDelayBuffer[m_iDelayIndex] = wNewSample;
      m_iDelayIndex++;

      if (m_iDelayIndex == m_awDelayBuffer.length)
      {
        m_iDelayIndex = 0;
      }
    }

    return m_aySamples;
  }
*/
	public byte[] aecProcess(byte[] out,byte[] in){
//		System.out.println("");
//		for(int i=0;i<in.length;i++){
//			System.out.print(in[i]+"   ");
//		}
//		System.out.println("");
        return in;
		//if(in.length!=out.length)System.out.println(in.length+"  -- "+ out.length);
		//return aec.process(out,in);
		
		
//		byte[] returnValue=new byte[in.length];
//		System.arraycopy(in, 0,returnValue, 0,in.length-1 );
//		for(int i=0;i<in.length;i++){
//			returnValue[i]-=out[i];
//		}
//		return returnValue;
		
		
		
		//return aec.process(in,out);
	}
}
