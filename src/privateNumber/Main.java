/*
 * **************************************************************************
 * *                                                                        *
 * * Ericsson hereby grants to the user a royalty-free, irrevocable,        *
 * * worldwide, nonexclusive, paid-up license to copy, display, perform,    *
 * * prepare and have prepared derivative works based upon the source code  *
 * * in this sample application, and distribute the sample source code and  *
 * * derivative works thereof and to grant others the foregoing rights.     *
 * *                                                                        *
 * * ERICSSON DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,        *
 * * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.       *
 * * IN NO EVENT SHALL ERICSSON BE LIABLE FOR ANY SPECIAL, INDIRECT OR      *
 * * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS    *
 * * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE  *
 * * OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE *
 * * OR PERFORMANCE OF THIS SOFTWARE.                                       *
 * *                                                                        *
 * **************************************************************************
 */
package privateNumber;

import com.ericsson.hosasdk.api.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;
import com.ericsson.hosasdk.utility.framework.FWproxy;
import com.ericsson.hosasdk.utility.log.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JPanel
{
    private HelpdeskFeature theFeature; 
    private FWproxy itsFramework;
    private GUI theGUI;

    public static void main(String[] args)
        throws Exception
    {
        new Main();
    }

    public Main()
        throws Exception
    {
        Properties p = new Properties();
        p.load(new FileInputStream("config/config.ini"));
        initClient();
        initServer(p);
        theGUI.setActivated(true);
    }

    void initServer(Properties p)
    {
        SimpleTracer.SINGLETON.PRINT_STACKTRACES = false;
        HOSAMonitor.addListener(SimpleTracer.SINGLETON);

        itsFramework = new FWproxy(p);
        theFeature = new HelpdeskFeature(itsFramework, p);
        theFeature.connect();
    }

    void stopServer(Properties p)
    {
        theFeature.release();

        // Dispose itsFramework 
        // Release all framework interfaces
        if (itsFramework != null)
        {
            itsFramework.endAccess(null);
            itsFramework.dispose();
            itsFramework = null;
        }
    }

    void initClient()
    {
        JFrame f = new JFrame();
        f.setTitle("Privacy Number");
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(theGUI = new GUI(this),
            BorderLayout.CENTER);
        f.pack();
        f.setLocation(100, 100);
        f.setVisible(true);
    }

    void addPrivateNumber(String aPublicNumber)
    {
        PrivateNumber privateNumber = DataBase.INSTANCE.addPrivateNumber(aPublicNumber);
        theFeature.activate(privateNumber);
    }

    void deletePrivateNumber(String aPublicNumber, PrivateNumber aPrivateNumber)
    {
        theFeature.deactivate(aPrivateNumber);
        DataBase.INSTANCE.removePrivateNumber(aPublicNumber,
            aPrivateNumber);
    }
}

