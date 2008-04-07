package org.cougaar.test.knode.experiment.bette;

import java.io.File;
import java.io.FilenameFilter;

import org.cougaar.core.component.ServiceBroker;
import org.cougaar.core.component.ServiceProvider;
import org.cougaar.core.plugin.AnnotatedSubscriptionsPlugin;
import org.cougaar.util.annotations.Cougaar;

public class TimedImageBasePlugin extends AnnotatedSubscriptionsPlugin implements TimedImageService {
	
	@Cougaar.Arg(name = "cacheImages", defaultValue = "true", 
    		description = "Should Images be cached in memory after first read from disk ")
    public boolean  isCacheImages;
	
	@Cougaar.Arg(name = "imageDirectory", defaultValue = "/org/cougaar/test/knode/experiment/bette/images", 
    		description = "Directory where images are stored. Use the appropriate format based on where the images are stored")
    public String  imageDirectory;
	
	@Cougaar.Arg(name = "imageFileExtension", defaultValue = ".jpg", 
    		description = "File extension of image files")
    public String  imageFileExtension;
	
	private String[] imageNames;
	private byte[][] imageCache;
    private ServiceProvider provider;

    public void load() {
        super.load();
        // Find the Image Files
        imageNames=listImages(imageDirectory);
        if (isCacheImages) {
			imageCache= new byte [imageNames.length][];	
		}
        // Advertise the Timed Image service
        provider = new MyServiceProvider();
        getServiceBroker().addService(TimedImageService.class, provider);
    }
    
    public void start() {
    	super.start();
    	for(String imageName : imageNames) {
			log.shout(imageName);
		}
    }

    public void unload() {
        if (provider != null) {
        	getServiceBroker().revokeService(TimedImageService.class, provider);
        }
        super.unload();
    }
    
    // Timed Image Service Interface
	public byte[] getImage(long time) {
		if (imageNames.length==0) {
			return new byte[0];
		}
		int imageNumber= (int) time % imageNames.length;
		log.shout("Requested Image #" + imageNumber + " at " + time);
		if (isCacheImages && imageCache[imageNumber] != null) {
			return imageCache[imageNumber];
		} else {
			byte[] replyPayload = null;
			try {
				replyPayload = readImage(imageNumber);
				if (isCacheImages) {
					imageCache[imageNumber] = replyPayload;
				}
			} catch (Exception e) {
				log.error("Could not get image #" + imageNumber + "message="+ e.getMessage());
			}
			return replyPayload;
		}
	}
	
	// This method should be overridden by childern classes
	protected byte[] readImage(int imageNumber) throws Exception {
		return new byte[0];
	}
	
	// This method should be overridden by childern classes
	protected String[] listImages(String directory) {
		return new String[0];
	}
	
	protected class ImageFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
            return name.endsWith(imageFileExtension);
        }
	}
	
    private class MyServiceProvider implements ServiceProvider {
        public MyServiceProvider() {
        }

        public Object getService(ServiceBroker sb, Object req, Class<?> cl) {
            return TimedImageBasePlugin.this;
        }

        public void releaseService(ServiceBroker arg0, Object arg1, Class<?> arg2, Object arg3) {
        }
    }
    
	public String[] getImageNames() {
		return imageNames;
	}

}
