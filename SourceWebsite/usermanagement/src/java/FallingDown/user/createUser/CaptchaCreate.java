package FallingDown.user.createUser;

import com.octo.captcha.service.CaptchaServiceException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;



import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import me.FallingDownLib.CommonClasses.www.overlay.PrintQuickLogin;

/**
 *
 * @author victork
 */
public class CaptchaCreate extends HttpServlet {
   
    @Override
      public void init(ServletConfig servletConfig) throws ServletException {

        super.init(servletConfig);

    }


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

       byte[] captchaChallengeAsJpeg = null;
       // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
        // get the session id that will identify the generated captcha.
        //the same id must be used to validate the response, the session id is a good candidate!
        String captchaId = getSessionId(httpServletRequest);
        // call the ImageCaptchaService getChallenge method
            BufferedImage challenge =
                    CaptchaServiceSingleton.getInstance().getImageChallengeForID(captchaId,
                            httpServletRequest.getLocale());

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder =
                    JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    private String getSessionId(HttpServletRequest request) {
        Boolean isQuick = Boolean.parseBoolean(request.getParameter(PrintQuickLogin.QUICK_FORM));
        if(isQuick.booleanValue()){
            return request.getParameter(PrintQuickLogin.ASSOCIATED_REQUEST);
        }else{
            return request.getSession().getId();
        }
    }

}
