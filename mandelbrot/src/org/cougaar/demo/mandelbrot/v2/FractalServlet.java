package org.cougaar.demo.mandelbrot.v2;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cougaar.core.servlet.ComponentServlet;
import org.cougaar.demo.mandelbrot.util.Arguments;
import org.cougaar.demo.mandelbrot.util.ImageOutput;

/**
 * This servlet uses the {@link FractalService} to calculate the image data. 
 */
public class FractalServlet extends ComponentServlet {

  private FractalService svc;

  public void load() {
    // get the FractalService
    svc = (FractalService) 
      getServiceBroker().getService(this, FractalService.class, null);
    if (svc == null) {
      throw new RuntimeException("Unable to obtain the FractalService");
    }

    super.load();
  }

  public void unload() {
    // shutting down, release the FractalService
    if (svc != null) {
      getServiceBroker().releaseService(this, FractalService.class, svc);
      svc = null;
    }

    super.unload();
  }

  protected void doGet(
      HttpServletRequest req, HttpServletResponse res
      ) throws ServletException, IOException {

    // parse the URL parameters
    Arguments args = new Arguments(req.getParameterMap());

    // calculate the image data
    byte[] data = svc.calculate(
          args.getInt("width"), args.getInt("height"),
          args.getDouble("x_min"), args.getDouble("x_max"),
          args.getDouble("y_min"), args.getDouble("y_max"));

    // write the data as a JPEG
    res.setContentType("image/jpeg");
    OutputStream out = res.getOutputStream();
    ImageOutput.writeJPG(
        args.getInt("width"), args.getInt("height"),
        data, out);
    out.close();
  }
}